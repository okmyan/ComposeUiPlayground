package com.okmyan.composeuiplayground.features.instagram.screens.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.size.Precision
import com.okmyan.composeuiplayground.R
import com.okmyan.composeuiplayground.features.instagram.domain.model.InstagramStory
import com.okmyan.composeuiplayground.features.instagram.domain.model.InstagramUser
import com.okmyan.composeuiplayground.features.instagram.screens.story.components.icons.InstagramMenu
import com.okmyan.composeuiplayground.utils.extensions.hapticNoRippleClickable

@Composable
fun StoryHeader(
    modifier: Modifier = Modifier,
    user: InstagramUser,
    activeStory: InstagramStory,
    onClosedFriendsLabelClicked: () -> Unit,
    onOptionsClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    isPageActive: Boolean = true,
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
                isPageActive = isPageActive,
            )

            val title = if (user.isAccountOwner) {
                stringResource(R.string.instagram_your_story)
            } else {
                user.username
            }
            Text(text = title, color = Color.White, fontWeight = FontWeight.Bold)

            Text(text = activeStory.publishedAt)
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (activeStory.isForClosedFriendsOnly) {
                CloseFriendsLabel(onClick = onClosedFriendsLabelClicked)
            }

            Icon(
                imageVector = InstagramMenu,
                contentDescription = stringResource(R.string.instagram_story_options),
                modifier = Modifier.hapticNoRippleClickable(onClick = {
                    onOptionsClick()
                }),
                tint = Color.White
            )
        }
    }
}

@Composable
fun Avatar(
    user: InstagramUser,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    isPageActive: Boolean,
) = user.run {
    val contentDescription = if (isAccountOwner) {
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
                    sharedContentState = rememberSharedContentState(
                        key = "user_${id}",
                        config = object : SharedTransitionScope.SharedContentConfig {
                            override val SharedTransitionScope.SharedContentState.isEnabled: Boolean
                                get() = isPageActive
                        }
                    ),
                    animatedVisibilityScope = animatedVisibilityScope,
                    clipInOverlayDuringTransition = OverlayClip(CircleShape)
                )
                .clip(CircleShape)
        )
    }
}

@Composable
fun CloseFriendsLabel(
    onClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy((-2).dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .background(Color(0xFF1BD163))
            .padding(horizontal = 5.dp, vertical = 2.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Star,
            contentDescription = stringResource(R.string.instagram_story_closed_friends),
            tint = Color.White
        )
        Icon(
            imageVector = Icons.Rounded.ExpandMore,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(18.dp)
                .offset(y = (-0.5).dp)
        )
    }
}

@Preview
@Composable
private fun CloseFriendsLabelPreview() {
    CloseFriendsLabel(onClick = {})
}
