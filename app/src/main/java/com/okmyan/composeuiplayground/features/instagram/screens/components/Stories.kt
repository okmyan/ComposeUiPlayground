package com.okmyan.composeuiplayground.features.instagram.screens.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
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

@Composable
fun Stories(
    stories: ImmutableList<InstagramStory>,
    activeStoryIndex: Int,
    isPageActive: Boolean,
    storyElementsAlpha: Float,
    onLongPressOrZoom: () -> Unit,
    onLongPressOrZoomRelease: () -> Unit,
    isMovingEnabled: Boolean,
    onMove: () -> Unit,
    onMoveRelease: () -> Unit,
    onDragUp: () -> Unit,
    onDragDown: (Float) -> Unit,
    onDragDownRelease: () -> Unit,
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

    var zoom by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

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
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(5.dp))
                    .graphicsLayer {
                        scaleX = zoom
                        scaleY = zoom
                        translationX = -offset.x * zoom
                        translationY = -offset.y * zoom
                        transformOrigin = TransformOrigin(0f, 0f)
                    },
                contentScale = ContentScale.Crop
            )
        }

        StoryGestures(
            onLongPressOrZoom = onLongPressOrZoom,
            onLongPressOrZoomRelease = onLongPressOrZoomRelease,
            isMovingEnabled = isMovingEnabled,
            onMove = onMove,
            onMoveRelease = onMoveRelease,
            onDragUp = onDragUp,
            onDragDown = onDragDown,
            onDragDownRelease = onDragDownRelease,
            onTransformation = { transformedZoom, transformedOffset ->
                zoom = transformedZoom
                offset = transformedOffset
            },
            onLeftClick = onGoToPrevStory,
            onRightClick = onGoToNextStory,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 8.dp)
                .alpha(storyElementsAlpha),
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
