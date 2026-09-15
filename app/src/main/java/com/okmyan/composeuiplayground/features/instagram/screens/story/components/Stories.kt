package com.okmyan.composeuiplayground.features.instagram.screens.story.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.size.Precision
import com.okmyan.composeuiplayground.R
import com.okmyan.composeuiplayground.features.instagram.domain.model.InstagramStory
import com.okmyan.composeuiplayground.utils.extensions.mirror
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import timber.log.Timber
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@Composable
fun Stories(
    stories: ImmutableList<InstagramStory>,
    activeStoryIndex: Int,
    isPageActive: Boolean,
    onGoToPrevStory: () -> Unit,
    onGoToNextStory: () -> Unit,
    onGoToPrevUserStories: () -> Unit,
    onGoToNextUserStories: () -> Unit,
    isContinuous: Boolean,
    onStorySeen: (Long) -> Unit,
    onStoryEnded: (Int) -> Unit
) {
    val activeStory = stories[activeStoryIndex]

    var isStoryLoading by remember(activeStory.id) { mutableStateOf(true) }
    val storyInPause = !isContinuous || isStoryLoading

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        activeStory.run {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pictureUrl)
                    .memoryCacheKey("story_${id}")
                    .diskCacheKey("story_${id}")
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .precision(Precision.INEXACT)
                    .build(),
                onLoading = { isStoryLoading = true },
                onSuccess = { isStoryLoading = false },
                loading = { StoriesLoading() },
                error = { StoriesError { painter.restart() } },
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            for ((index, story) in stories.withIndex()) {
                LinearDeterminateIndicator(
                    modifier = Modifier.weight(1f),
                    storyId = story.id,
                    isLoaded = story.isSeen,
                    isActive = (index == activeStoryIndex) && isPageActive,
                    isContinuous = !storyInPause,
                    onStart = {
                        onStorySeen(story.id)
                    },
                    onStop = {
                        onStoryEnded(index)
                    }
                )

                if (index != stories.size - 1) {
                    Spacer(Modifier.width(0.5.dp))
                }
            }
        }
    }
}

@Composable
fun StoriesLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(35.dp),
            color = Color.LightGray,
            strokeWidth = 1.dp,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF252424)
@Composable
fun StoriesLoadingPreview() {
    StoriesLoading()
}

@Composable
fun StoriesError(
    onRetry: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Replay,
            contentDescription = stringResource(R.string.instagram_story_retry),
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .mirror()
                .clickable(onClick = onRetry),
            tint = Color.LightGray,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF252424)
@Composable
fun StoriesErrorPreview() {
    StoriesError(onRetry = {})
}

@Composable
fun LinearDeterminateIndicator(
    modifier: Modifier,
    storyId: Long,
    isLoaded: Boolean,
    isActive: Boolean,
    isContinuous: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
) {
    var currentProgress by remember(storyId, isLoaded) {
        mutableFloatStateOf(if (isLoaded) 1f else 0f)
    }

    // We use a trigger to restart the animation if the story reached 1.0 
    // but the user interrupted the Pager transition and stayed on the same page
    var restartTrigger by remember { mutableIntStateOf(0) }

    LaunchedEffect(isContinuous) {
        if (isContinuous && isActive && currentProgress >= 1f && !isLoaded) {
            restartTrigger++
        }
    }

    val currentIsContinuous by rememberUpdatedState(isContinuous)

    LaunchedEffect(isActive, restartTrigger) {
        if (isActive) {
            // Force reset if starting a fresh story that isn't loaded yet
            if (currentProgress >= 1f && !isLoaded) {
                Timber.d("LinearDeterminateIndicator storyId: $storyId - force reset")
                currentProgress = 0f
            }

            onStart()

            loadProgress(
                isContinuous = { currentIsContinuous },
                startProgress = currentProgress,
            ) { progress ->
                currentProgress = progress
            }

            onStop()
        }
    }

    LinearProgressIndicator(
        progress = { currentProgress },
        modifier = modifier,
        color = Color.White,
        trackColor = Color.LightGray,
        gapSize = 0.dp,
        drawStopIndicator = {},
    )
}

/**
 * Advances the progress from [startProgress] to completion over [STORY_DURATION].
 *
 * Progress is updated only while [isContinuous] returns `true`, allowing the
 * operation to be paused and resumed without restarting the coroutine.
 */
suspend fun loadProgress(
    isContinuous: () -> Boolean,
    startProgress: Float,
    updateProgress: (Float) -> Unit,
) {
    val totalDurationNanos = STORY_DURATION.inWholeNanoseconds.toDouble()
    var elapsedNanos = startProgress * totalDurationNanos
    val delay = 16.milliseconds

    while (elapsedNanos < totalDurationNanos) {
        val startTime = System.nanoTime()
        delay(delay) // Approx. 60 FPS
        val frameTimeNanos = System.nanoTime() - startTime

        if (isContinuous()) {
            elapsedNanos += frameTimeNanos
            updateProgress((elapsedNanos / totalDurationNanos).coerceAtMost(1.0).toFloat())
        }
    }
}

val STORY_DURATION = 5.seconds
