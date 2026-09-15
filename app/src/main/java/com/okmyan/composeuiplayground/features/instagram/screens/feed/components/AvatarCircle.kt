package com.okmyan.composeuiplayground.features.instagram.screens.feed.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.size.Precision
import com.okmyan.composeuiplayground.R
import com.okmyan.composeuiplayground.features.instagram.domain.model.InstagramUser
import com.okmyan.composeuiplayground.ui.theme.PurpleGrey40

@Composable
fun AvatarCircle(
    user: InstagramUser,
    hasNonSeenStories: Boolean,
    onClick: () -> Unit,
    onAddStory: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) = user.run {
    with(animatedVisibilityScope) {
        with(sharedTransitionScope) {
            Box {
                val brush = if (hasNonSeenStories) {
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFCAF45), Color(0xFFF77737), Color(0xFFF56040),
                            Color(0xFFFD1D1D), Color(0xFFE1306C), Color(0xFFC13584),
                            Color(0xFF833AB4),
                        )
                    )
                } else {
                    SolidColor(Color(0xFF494949))
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(100.dp)
                        .border(
                            width = 3.dp,
                            brush = brush,
                            shape = CircleShape
                        )
                ) {
                    val contentDescription = if (isAccountOwner) {
                        stringResource(R.string.instagram_your_avatar_description)
                    } else {
                        stringResource(R.string.instagram_avatar_description, username)
                    }

                    // Container for the full-size story
                    Box(
                        modifier = Modifier.sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "container_${id}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                        )
                    ) {}

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
                            .size(83.dp)
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(key = "user_${id}"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                clipInOverlayDuringTransition = OverlayClip(CircleShape)
                            )
                            .border(0.2.dp, PurpleGrey40, CircleShape)
                            .clip(CircleShape)
                            .clickable(onClick = onClick),
                    )

                }

                if (isAccountOwner) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.instagram_add_story),
                        tint = Color.Black,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(2.5.dp)
                            .size(30.dp)
                            .renderInSharedTransitionScopeOverlay()
                            .animateEnterExit()
                            .background(MaterialTheme.colorScheme.background, CircleShape)
                            .padding(2.5.dp)
                            .background(Color.White, CircleShape)
                            .padding(2.5.dp)
                            .clickable(onClick = onAddStory)
                    )
                }
            }
        }
    }
}
