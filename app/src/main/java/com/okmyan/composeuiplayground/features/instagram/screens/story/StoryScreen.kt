package com.okmyan.composeuiplayground.features.instagram.screens.story

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.okmyan.composeuiplayground.features.instagram.screens.story.components.BlackoutStory
import com.okmyan.composeuiplayground.features.instagram.screens.story.components.Header
import com.okmyan.composeuiplayground.features.instagram.screens.story.components.MessageTextField
import com.okmyan.composeuiplayground.features.instagram.screens.story.components.Stories
import com.okmyan.composeuiplayground.features.instagram.screens.story.components.StoryBottomSheet
import com.okmyan.composeuiplayground.features.instagram.screens.story.components.StoryNotificationPopup
import com.okmyan.composeuiplayground.features.instagram.screens.story.components.Tail
import com.okmyan.composeuiplayground.features.instagram.utils.HIDE_STORY_ELEMENTS_DELAY
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

@Composable
fun StoryScreen(
    selectedStoryOwnerId: Long,
    viewModel: StoryViewModel = koinViewModel(key = selectedStoryOwnerId.toString()) {
        parametersOf(selectedStoryOwnerId)
    },
    isContinuous: Boolean,
    isPageActive: Boolean = true,
    onGoToPrevUserStories: () -> Unit,
    onGoToNextUserStories: () -> Unit,
    onDragDown: (Float) -> Unit,
    onDragDownRelease: () -> Unit,
    onScrollAbilityChange: (Boolean) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val notification by viewModel.notifications.collectAsStateWithLifecycle(StoryNotification())

    val activeStoryId = state.activeStoryId
    val enteredMessage = state.enteredMessage[activeStoryId] ?: TextFieldValue("")

    var isMessageEditing by remember { mutableStateOf(false) }
    val blackoutAlpha by animateFloatAsState(
        targetValue = if (isMessageEditing) 0.3f else 0f,
        animationSpec = tween(300),
    )

    var isStoryMoving by remember(state.activeStoryId) { mutableStateOf(false) }
    var isStoryLongPressingOrZooming by remember(state.activeStoryId) { mutableStateOf(false) }

    var showStoryElements by remember(state.activeStoryId) { mutableStateOf(true) }
    val storyElementsAlpha by animateFloatAsState(
        targetValue = if (showStoryElements) 1f else 0f,
        animationSpec = tween(300),
    )

    LaunchedEffect(isStoryLongPressingOrZooming) {
        if (isStoryLongPressingOrZooming) {
            delay(HIDE_STORY_ELEMENTS_DELAY)
            showStoryElements = false
        } else {
            showStoryElements = true
        }
    }

    val isHorizontalScrollAllowed =
        !isMessageEditing && !isStoryMoving && !isStoryLongPressingOrZooming
    LaunchedEffect(isHorizontalScrollAllowed) {
        onScrollAbilityChange(isHorizontalScrollAllowed)
    }

    var showOptionsBottomSheet by remember { mutableStateOf(false) }

    val isStoryContinuous =
        isContinuous && !isMessageEditing && !showOptionsBottomSheet && !isStoryMoving && !isStoryLongPressingOrZooming

    val messageFieldModifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)

    val onGoToNextStory = {
        if (!viewModel.onGoToNextStory()) {
            Timber.d("go to next user $selectedStoryOwnerId")
            onGoToNextUserStories()
        }
    }

    with(sharedTransitionScope) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(
                        key = "container_${selectedStoryOwnerId}",
                        config = object : SharedTransitionScope.SharedContentConfig {
                            override val SharedTransitionScope.SharedContentState.isEnabled: Boolean
                                get() = isPageActive
                        }
                    ),
                    animatedVisibilityScope = animatedVisibilityScope,
                ),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .skipToLookaheadSize()
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    Stories(
                        stories = state.stories,
                        activeStoryIndex = state.activeStoryIndex,
                        isPageActive = isPageActive,
                        storyElementsAlpha = storyElementsAlpha,
                        onLongPressOrZoom = { isStoryLongPressingOrZooming = true },
                        onLongPressOrZoomRelease = { isStoryLongPressingOrZooming = false },
                        isMovingEnabled = showStoryElements,
                        onMove = { isStoryMoving = true },
                        onMoveRelease = { isStoryMoving = false },
                        onDragUp = { isMessageEditing = true },
                        onDragDown = onDragDown,
                        onDragDownRelease = onDragDownRelease,
                        onGoToPrevStory = {
                            if (!viewModel.onGoToPrevStory()) {
                                onGoToPrevUserStories()
                            }
                        },
                        onGoToNextStory = onGoToNextStory,
                        isContinuous = isStoryContinuous,
                        onStoryOpened = viewModel::onStorySeen,
                        onStoryEnded = onGoToNextStory,
                    )
                    Header(
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 20.dp)
                            .alpha(storyElementsAlpha),
                        user = state.storyOwner,
                        activeStory = state.activeStory,
                        onOptionsClick = { showOptionsBottomSheet = true },
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                        isPageActive = isPageActive,
                    )
                }

                Tail(
                    modifier = messageFieldModifier
                        .alpha(storyElementsAlpha),
                    onMessageEditing = { isMessageEditing = true },
                    message = enteredMessage.text,
                    isLiked = state.activeStory.isLiked,
                    onLike = { viewModel.onStoryLiked(state.activeStoryId) },
                )
            }

            BlackoutStory(blackoutAlpha = blackoutAlpha, onClick = {
                isMessageEditing = false
            })

            StoryNotificationPopup(notification)

            if (showOptionsBottomSheet) {
                StoryBottomSheet(
                    isMuted = state.storyOwner.isMuted,
                    onDismiss = { showOptionsBottomSheet = false },
                    onReport = viewModel::onReport,
                    onMute = viewModel::onMute,
                )
            }

            MessageTextField(
                isMessageEditing = isMessageEditing,
                message = enteredMessage,
                onMessageChange = { viewModel.onMessageChange(activeStoryId, it) },
                onKeyboardHide = { isMessageEditing = false },
                onMessageSent = { viewModel.onMessageSend(activeStoryId) },
                modifier = messageFieldModifier,
            )
        }
    }
}
