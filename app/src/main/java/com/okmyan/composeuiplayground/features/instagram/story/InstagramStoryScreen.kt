package com.okmyan.composeuiplayground.features.instagram.story

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.okmyan.composeuiplayground.R
import com.okmyan.composeuiplayground.features.instagram.home.InstagramHomeViewModel
import com.okmyan.composeuiplayground.features.instagram.home.Story
import com.okmyan.composeuiplayground.features.instagram.home.User
import com.okmyan.composeuiplayground.features.instagram.home.UserWithStories
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun InstagramStoryScreen(
    userWithStories: UserWithStories,
    viewModel: InstagramHomeViewModel,
    storyViewModel: InstagramStoryViewModel = koinViewModel { parametersOf(userWithStories) },
    hasPreviousStory: Boolean,
    onGoToPrevious: () -> Unit,
    onGoToNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by storyViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        Header(state.user, state.activeStory)

        Text(text = "This is a screen Instagram Story #${userWithStories.user.id}")

        Stories(state.stories, state.activeStoryIndex, onStorySeen = {
            if (state.isActiveStoryLastOne) {
                onGoToNext()
            } else {
                storyViewModel.onStorySeen(it)
            }
        })

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            if (hasPreviousStory) {
                Button(onClick = onGoToPrevious) {
                    Text("Previous")
                }
            }

            Button(onClick = onGoToNext) {
                Text("Next")
            }
        }
    }
}

@Composable
fun Header(user: User, activeStory: Story) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Avatar(user)

            Text(text = user.username, fontWeight = FontWeight.Bold)

            Text(text = activeStory.publishedAt)
        }

        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = stringResource(R.string.instagram_story_options),
            tint = Color.White
        )
    }
}

@Composable
fun Avatar(user: User) = user.run {
    val contentDescription = if (isCurrentUser) {
        stringResource(R.string.instagram_your_avatar_description)
    } else {
        stringResource(R.string.instagram_avatar_description, username)
    }
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(avatarPreviewUrl)
            .memoryCacheKey(id.toString())
            .diskCacheKey(id.toString())
            .build(),
        contentDescription = contentDescription,
        modifier = Modifier
            .size(35.dp)
            .clip(CircleShape)
    )
}

@Composable
fun Stories(
    stories: ImmutableList<Story>,
    activeStoryIndex: Int,
    onStorySeen: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        stories.forEachIndexed { index, story ->
            LinearDeterminateIndicator(
                modifier = Modifier.weight(1f),
                isLoaded = story.isSeen,
                isActive = index == activeStoryIndex,
                onStop = {
                    onStorySeen(index)
                }
            )
            Spacer(Modifier.width(2.dp))
        }
    }
}

@Composable
fun LinearDeterminateIndicator(
    modifier: Modifier,
    isLoaded: Boolean,
    isActive: Boolean,
    onStop: () -> Unit,
) {
    val initialValue = if (isLoaded) 1f else 0f
    var currentProgress by remember { mutableFloatStateOf(initialValue) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(isActive) {
        if (isActive) {
            scope.launch {
                loadProgress { progress ->
                    currentProgress = progress
                }
                onStop()
            }
        }
    }

    LinearProgressIndicator(
        progress = { currentProgress },
        modifier = modifier,
        gapSize = 0.dp,
        drawStopIndicator = {}
    )
}

/** Iterate the progress value */
suspend fun loadProgress(updateProgress: (Float) -> Unit) {
//    for (i in 1..400) {
//        updateProgress(i.toFloat() / 400)
//        delay(25)
//    }
    for (i in 1..100) {
        updateProgress(i.toFloat() / 100)
        delay(25)
    }
}
