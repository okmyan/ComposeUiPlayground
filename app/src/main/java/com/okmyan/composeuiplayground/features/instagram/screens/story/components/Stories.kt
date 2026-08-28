package com.okmyan.composeuiplayground.features.instagram.screens.story.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.okmyan.composeuiplayground.features.instagram.domain.model.InstagramStory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import kotlin.math.max

@Composable
fun Stories(
    stories: ImmutableList<InstagramStory>,
    activeStoryIndex: Int,
    isContinuous: Boolean,
    onStorySeen: (Long) -> Unit,
    onStoryEnded: (Int) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        stories[activeStoryIndex].run {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pictureUrl)
                    .memoryCacheKey("story_${id}")
                    .diskCacheKey("story_${id}")
                    .build(),
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
            for (index in 0 until stories.size) {
                val story = stories[index]
                LinearDeterminateIndicator(
                    modifier = Modifier.weight(1f),
                    isLoaded = story.isSeen,
                    isActive = index == activeStoryIndex,
                    isContinuous = isContinuous,
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
fun LinearDeterminateIndicator(
    modifier: Modifier,
    isLoaded: Boolean,
    isActive: Boolean,
    isContinuous: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
) {
    val initialValue = if (isLoaded) 1f else 0f
    var currentProgress by remember { mutableFloatStateOf(initialValue) }

    val currentIsContinuous by rememberUpdatedState(isContinuous)

    LaunchedEffect(isActive) {
        if (isActive) {
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
 * Advances the progress from [startProgress] to completion.
 *
 * Progress is updated only while [isContinuous] returns `true`, allowing the
 * operation to be paused and resumed without restarting the coroutine.
 */
suspend fun loadProgress(
    isContinuous: () -> Boolean,
    startProgress: Float,
    updateProgress: (Float) -> Unit,
) {
    val step = 300
    val start = max(1, (startProgress * step).toInt())
    var i = start

    while (i <= step) {
        delay(30)

        if (!isContinuous()) {
            continue
        }

        updateProgress(i.toFloat() / step)
        i++
    }
}
