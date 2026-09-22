package com.okmyan.composeuiplayground.features.instagram.screens.story

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun StoriesPagerScreen(
    selectedStoryOwnerId: Long,
    storyOwnerIds: List<Long>,
    onStoriesEnd: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val selectedStoryIndex = storyOwnerIds.indexOf(selectedStoryOwnerId)
        val pagerState = rememberPagerState(
            initialPage = selectedStoryIndex,
            pageCount = { storyOwnerIds.size }
        )
        val pagerIsDragged by pagerState.interactionSource.collectIsDraggedAsState()

        val scope = rememberCoroutineScope()
        val goToPrevPage: () -> Unit = {
            Timber.d("go to previous")
            if (pagerState.currentPage != 0) {
                scope.launch(CoroutineName("Scroll to prev page")) {
                    val prevPage = (pagerState.currentPage - 1) % storyOwnerIds.size
                    pagerState.animateScrollToPage(
                        page = prevPage,
                        animationSpec = tween(durationMillis = 300)
                    )
                }
            }
        }
        val goToNextPage: () -> Unit = {
            Timber.d("go to next")
            if (pagerState.currentPage == pagerState.pageCount - 1) {
                onStoriesEnd()
            } else {
                scope.launch(CoroutineName("Scroll to next page")) {
                    val nextPage = (pagerState.currentPage + 1) % storyOwnerIds.size
                    pagerState.animateScrollToPage(
                        page = nextPage,
                        animationSpec = tween(durationMillis = 300)
                    )
                }
            }
        }

        var scrollEnabled by remember { mutableStateOf(true) }
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = scrollEnabled,
        ) { pageIndex ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        // Calculate offset: 0 - center, >0 - leaving left, <0 - entering right
                        val pageOffset =
                            (pagerState.currentPage - pageIndex) + pagerState.currentPageOffsetFraction

                        // Add perspective, 8 * density is a standard value for a nice 3D effect
                        cameraDistance = 8 * density

                        // Set the pivot point
                        // If the pageIndex is leaving to the left (pageOffset > 0), rotate around the right edge (1f)
                        // If the pageIndex is entering from the right (pageOffset < 0), rotate around the left edge (0f)
                        transformOrigin = TransformOrigin(
                            pivotFractionX = if (pageOffset > 0) 1f else 0f,
                            pivotFractionY = 0.5f
                        )

                        // Rotate by an angle of up to 90 degrees
                        rotationY = -90f * pageOffset
                    }
            ) {
                val pageItem = storyOwnerIds[pageIndex]

                StoryScreen(
                    selectedStoryOwnerId = pageItem,
                    isContinuous = !pagerIsDragged,
                    isPageActive = pagerState.currentPage == pageIndex,
                    onGoToPrevUserStories = goToPrevPage,
                    onGoToNextUserStories = goToNextPage,
                    onScrollAbilityChange = { scrollEnabled = it },
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                )
            }
        }
    }
}
