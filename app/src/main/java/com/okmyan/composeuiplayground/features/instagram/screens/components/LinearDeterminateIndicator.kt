package com.okmyan.composeuiplayground.features.instagram.screens.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import timber.log.Timber
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

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
    var currentProgress by remember(storyId, isLoaded, isActive) {
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

    Timber.d("storyId: $storyId - restartTrigger: $restartTrigger")

    val currentIsContinuous by rememberUpdatedState(isContinuous)

    LaunchedEffect(isActive, restartTrigger) {
        if (isActive) {
            // Force reset if starting a fresh story that isn't loaded yet
            if (currentProgress >= 1f && !isLoaded) {
                Timber.d("storyId: $storyId - force reset")
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
        modifier = modifier
            .height(2.dp),
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
