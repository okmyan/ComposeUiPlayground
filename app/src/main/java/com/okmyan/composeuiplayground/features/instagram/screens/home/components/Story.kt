package com.okmyan.composeuiplayground.features.instagram.screens.home.components

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
import com.okmyan.composeuiplayground.features.instagram.domain.model.UserWithStories

@Composable
fun Story(
    userWithStories: UserWithStories,
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
            user = userWithStories.user,
            hasNonSeenStories = userWithStories.hasNonSeenStories,
            onClick = onClick,
            onAddStory = onAddStory,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
        )

        val title = if (userWithStories.user.isCurrentUser) {
            stringResource(R.string.instagram_your_story)
        } else {
            userWithStories.user.username
        }
        Text(text = title)
    }
}
