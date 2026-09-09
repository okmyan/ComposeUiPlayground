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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.okmyan.composeuiplayground.features.instagram.domain.model.UserWithStories
import com.okmyan.composeuiplayground.features.instagram.screens.story.components.BlackoutStory
import com.okmyan.composeuiplayground.features.instagram.screens.story.components.Header
import com.okmyan.composeuiplayground.features.instagram.screens.story.components.MessageTextField
import com.okmyan.composeuiplayground.features.instagram.screens.story.components.Stories
import com.okmyan.composeuiplayground.features.instagram.screens.story.components.StoryBottomSheet
import com.okmyan.composeuiplayground.features.instagram.screens.story.components.StoryNotificationPopup
import com.okmyan.composeuiplayground.features.instagram.screens.story.components.Tail
import com.okmyan.composeuiplayground.utils.extensions.clearFocusOnTap
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun InstagramStoryScreen(
    userWithStories: UserWithStories,
    viewModel: InstagramStoryViewModel = koinViewModel {
        parametersOf(userWithStories)
    },
    hasPreviousStory: Boolean,
    onGoToPrevious: () -> Unit,
    onGoToNext: () -> Unit,
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

    var showOptions by remember { mutableStateOf(false) }

    val isStoryContinuous = !isMessageEditing && !showOptions

    val messageFieldModifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)

    with(sharedTransitionScope) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(key = "container_${userWithStories.userId}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                )
                .clearFocusOnTap(LocalFocusManager.current),
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
                        onStorySeen = viewModel::onStorySeen,
                        hasPrevStory = hasPreviousStory,
                        onGoToPrevStory = {},
                        onGoToNextStory = {},
                        onGoToPrevUserStories = onGoToPrevious,
                        onGoToNextUserStories = onGoToNext,
                        isContinuous = isStoryContinuous,
                        onStoryEnded = {
                            if (state.isActiveStoryLastOne) {
                                onGoToNext()
                            } else {
                                viewModel.onStoryEnded(it)
                            }
                        },
                    )
                    Header(
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 20.dp
                        ),
                        user = state.user,
                        activeStory = state.activeStory,
                        onOptionsClick = { showOptions = true },
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                    )
                }

                if (!state.user.isCurrentUser) {
                    Tail(
                        modifier = messageFieldModifier,
                        onMessageEditing = { isMessageEditing = true },
                        message = enteredMessage.text,
                        isLiked = state.activeStory.isLiked,
                        onLike = { viewModel.onStoryLiked(state.activeStory.id) },
                    )
                }
            }

            BlackoutStory(blackoutAlpha = blackoutAlpha)

            StoryNotificationPopup(notification)

            if (showOptions) {
                StoryBottomSheet(
                    isMuted = state.user.isMuted,
                    onDismiss = { showOptions = false },
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
