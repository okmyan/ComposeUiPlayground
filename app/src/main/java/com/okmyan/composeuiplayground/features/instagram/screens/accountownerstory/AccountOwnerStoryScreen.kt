package com.okmyan.composeuiplayground.features.instagram.screens.accountownerstory

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
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
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun AccountOwnerStoryScreen(
    onStoriesEnd: () -> Unit,
    viewModel: AccountOwnerStoryViewModel = koinViewModel(),
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    var showOptions by remember { mutableStateOf(false) }

    val isStoryContinuous = !showOptions

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
                        onStorySeen = viewModel::onStorySeen,
                        onGoToPrevStory = {},
                        onGoToNextStory = {},
                        onGoToPrevUserStories = {},
                        onGoToNextUserStories = {},
                        isContinuous = isStoryContinuous,
                        onStoryEnded = {
                            if (state.isActiveStoryLastOne) {
                                Timber.d("Story ended")
                                onStoriesEnd()
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
                        user = state.storyOwner,
                        activeStory = state.activeStory,
                        onOptionsClick = { showOptions = true },
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                    )
                }
            }

            if (showOptions) {
                AccountOwnerStoryBottomSheet(
                    onDismiss = { showOptions = false },
                    onDelete = {}, // TODO implement
                    onArchive = {},
                )
            }
        }
    }
}
