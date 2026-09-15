package com.okmyan.composeuiplayground.features.instagram.screens.feed.components

// TODO rewrite it
//@Composable
//fun PreloadStories(usersWithStories: List<UserWithStories>) {
//    val context = LocalContext.current
//    var isPreloaded by rememberSaveable { mutableStateOf(false) }
//
//    LaunchedEffect(usersWithStories.isNotEmpty()) {
//        if (!isPreloaded && usersWithStories.isNotEmpty()) {
//            isPreloaded = true
//            usersWithStories
//                .take(6) // Preload only first 6 users
//                .forEach { userWithStories ->
//                    userWithStories.user.run {
//                        val avatarRequest = ImageRequest.Builder(context)
//                            .data(avatarPreviewUrl)
//                            .memoryCacheKey("user_${id}")
//                            .diskCacheKey("user_${id}")
//                            .diskCachePolicy(CachePolicy.ENABLED)
//                            .precision(Precision.INEXACT)
//                            .build()
//                        context.imageLoader.enqueue(avatarRequest)
//                    }
//
//                    val activeStoryIndex = getActiveStoryIndex(userWithStories.stories)
//                    userWithStories.stories.getOrNull(activeStoryIndex)?.run {
//                        val firstStoryRequest = ImageRequest.Builder(context)
//                            .data(pictureUrl)
//                            .memoryCacheKey("story_${id}")
//                            .diskCacheKey("story_${id}")
//                            .diskCachePolicy(CachePolicy.ENABLED)
//                            .precision(Precision.INEXACT)
//                            .build()
//                        context.imageLoader.enqueue(firstStoryRequest)
//                    }
//                }
//        }
//    }
//}
