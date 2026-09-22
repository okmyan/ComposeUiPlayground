package com.okmyan.composeuiplayground.features.instagram.screens.story.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.okmyan.composeuiplayground.features.instagram.screens.components.PreloadStory
import com.okmyan.composeuiplayground.utils.extensions.mirror
import com.okmyan.composeuiplayground.utils.extensions.noRippleClickable
import kotlinx.collections.immutable.ImmutableList

@Composable
fun Stories(
    stories: ImmutableList<InstagramStory>,
    activeStoryIndex: Int,
    isPageActive: Boolean,
    onGoToPrevStory: () -> Unit,
    onGoToNextStory: () -> Unit,
    isContinuous: Boolean,
    onStoryOpened: (Long) -> Unit,
    onStoryEnded: () -> Unit
) {
    val activeStory = stories[activeStoryIndex]

    var isStoryLoading by remember(activeStory.id) { mutableStateOf(true) }
    val storyInPause = !isContinuous || isStoryLoading

    // Preload the next story in advance
    stories.getOrNull(activeStoryIndex + 1)?.let {
        PreloadStory(it)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        StoryGestures(
            onLeftClick = { onGoToPrevStory() },
            onRightClick = { onGoToNextStory() },
        )

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
                        onStoryOpened(story.id)
                    },
                    onStop = {
                        onStoryEnded()
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
fun StoryGestures(
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.33f)
                .noRippleClickable(
                    onClick = onLeftClick
                )
        )

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.67f)
                .noRippleClickable(
                    onClick = onRightClick
                )
        )
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
