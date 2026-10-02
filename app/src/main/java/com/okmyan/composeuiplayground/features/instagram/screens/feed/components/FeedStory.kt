package com.okmyan.composeuiplayground.features.instagram.screens.feed.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.okmyan.composeuiplayground.R
import com.okmyan.composeuiplayground.features.instagram.domain.model.FeedStory

@Composable
fun FeedStory(
    feedStory: FeedStory,
    onClick: () -> Unit,
    onAddStory: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        AvatarCircle(
            user = feedStory.storyOwner,
            hasNonSeenStories = feedStory.hasNonSeenStories,
            hasNonSeenStoriesForClosedFriendsOnly = feedStory.hasNonSeenStoriesForClosedFriendsOnly,
            onClick = onClick,
            onAddStory = onAddStory,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
        )

        val title = if (feedStory.storyOwner.isAccountOwner) {
            stringResource(R.string.instagram_your_story)
        } else {
            feedStory.storyOwner.username
        }
        Text(text = title)
    }
}
