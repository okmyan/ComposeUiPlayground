package com.okmyan.composeuiplayground.features.instagram.screens.home.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import coil3.imageLoader
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.size.Precision
import com.okmyan.composeuiplayground.features.instagram.domain.model.UserWithStories
import com.okmyan.composeuiplayground.features.instagram.utils.getActiveStoryIndex

@Composable
fun PreloadStories(usersWithStories: List<UserWithStories>) {
    val context = LocalContext.current
    var isPreloaded by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(usersWithStories.isNotEmpty()) {
        if (!isPreloaded && usersWithStories.isNotEmpty()) {
            isPreloaded = true
            usersWithStories
                .take(6) // Preload only first 6 users
                .forEach { userWithStories ->
                    userWithStories.user.run {
                        val avatarRequest = ImageRequest.Builder(context)
                            .data(avatarPreviewUrl)
                            .memoryCacheKey("user_${id}")
                            .diskCacheKey("user_${id}")
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .precision(Precision.INEXACT)
                            .build()
                        context.imageLoader.enqueue(avatarRequest)
                    }

                    val activeStoryIndex = getActiveStoryIndex(userWithStories.stories)
                    userWithStories.stories.getOrNull(activeStoryIndex)?.run {
                        val firstStoryRequest = ImageRequest.Builder(context)
                            .data(pictureUrl)
                            .memoryCacheKey("story_${id}")
                            .diskCacheKey("story_${id}")
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .precision(Precision.INEXACT)
                            .build()
                        context.imageLoader.enqueue(firstStoryRequest)
                    }
                }
        }
    }
}
