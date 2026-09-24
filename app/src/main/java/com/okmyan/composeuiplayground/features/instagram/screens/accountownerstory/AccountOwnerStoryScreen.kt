package com.okmyan.composeuiplayground.features.instagram.screens.accountownerstory

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.okmyan.composeuiplayground.features.instagram.screens.accountownerstory.components.AccountOwnerStoryBottomSheet
import com.okmyan.composeuiplayground.features.instagram.screens.story.components.Header
import com.okmyan.composeuiplayground.features.instagram.screens.story.components.Stories
import com.okmyan.composeuiplayground.features.instagram.utils.HIDE_STORY_ELEMENTS_DELAY
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

@Composable
fun AccountOwnerStoryScreen(
    accountOwnerId: Long,
    viewModel: AccountOwnerStoryViewModel = koinViewModel(key = accountOwnerId.toString()) {
        parametersOf(accountOwnerId)
    },
    closeStory: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

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

    var showOptionsBottomSheet by remember { mutableStateOf(false) }

    val isStoryContinuous =
        !showOptionsBottomSheet && !isStoryMoving && !isStoryLongPressingOrZooming

    val onGoToNextStory = {
        if (!viewModel.onGoToNextStory()) {
            Timber.d("Close stories")
            closeStory()
        }
    }

    with(sharedTransitionScope) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(key = "container_${state.storyOwnerId}"),
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
                        isPageActive = true,
                        storyElementsAlpha = storyElementsAlpha,
                        onLongPressOrZoom = { isStoryLongPressingOrZooming = true },
                        onLongPressOrZoomRelease = { isStoryLongPressingOrZooming = false },
                        isMovingEnabled = showStoryElements,
                        onMove = { isStoryMoving = true },
                        onMoveRelease = { isStoryMoving = false },
                        onDragUp = {},
                        onGoToPrevStory = { viewModel.onGoToPrevStory() },
                        onGoToNextStory = onGoToNextStory,
                        isContinuous = isStoryContinuous,
                        onStoryOpened = viewModel::onStorySeen,
                        onStoryEnded = onGoToNextStory,
                    )
                    Header(
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 20.dp
                        ),
                        user = state.storyOwner,
                        activeStory = state.activeStory,
                        onOptionsClick = { showOptionsBottomSheet = true },
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                    )
                }
            }

            if (showOptionsBottomSheet) {
                AccountOwnerStoryBottomSheet(
                    onDismiss = { showOptionsBottomSheet = false },
                    onDelete = {}, // TODO implement
                    onArchive = {},
                )
            }
        }
    }
}
