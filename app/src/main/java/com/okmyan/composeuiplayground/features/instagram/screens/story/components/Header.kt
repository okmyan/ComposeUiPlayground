package com.okmyan.composeuiplayground.features.instagram.screens.story.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.size.Precision
import com.okmyan.composeuiplayground.R
import com.okmyan.composeuiplayground.features.instagram.domain.model.InstagramStory
import com.okmyan.composeuiplayground.features.instagram.domain.model.InstagramUser

@Composable
fun Header(
    modifier: Modifier = Modifier,
    user: InstagramUser,
    activeStory: InstagramStory,
    onOptionsClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Avatar(
                user = user,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
            )

            val title = if (user.isCurrentUser) {
                stringResource(R.string.instagram_your_story)
            } else {
                user.username
            }
            Text(text = title, color = Color.White, fontWeight = FontWeight.Bold)

            Text(text = activeStory.publishedAt)
        }

        val haptics = LocalHapticFeedback.current
        Icon(
            imageVector = InstagramMenu,
            contentDescription = stringResource(R.string.instagram_story_options),
            modifier = Modifier.clickable(onClick = {
                haptics.performHapticFeedback(HapticFeedbackType.Confirm)
                onOptionsClick()
            }),
            tint = Color.White
        )
    }
}

@Composable
fun Avatar(
    user: InstagramUser,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) = user.run {
    val contentDescription = if (isCurrentUser) {
        stringResource(R.string.instagram_your_avatar_description)
    } else {
        stringResource(R.string.instagram_avatar_description, username)
    }
    with(sharedTransitionScope) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(avatarPreviewUrl)
                .memoryCacheKey("user_${id}")
                .diskCacheKey("user_${id}")
                .diskCachePolicy(CachePolicy.ENABLED)
                .precision(Precision.INEXACT)
                .build(),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(35.dp)
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(key = "user_${id}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    clipInOverlayDuringTransition = OverlayClip(CircleShape)
                )
                .clip(CircleShape)
        )
    }
}
