package com.okmyan.composeuiplayground.features.instagram

import com.okmyan.composeuiplayground.features.instagram.home.Story
import com.okmyan.composeuiplayground.features.instagram.home.User
import com.okmyan.composeuiplayground.features.instagram.home.UserWithStories
import kotlinx.collections.immutable.persistentListOf

class InstagramRepository {

    val usersWithStories = listOf(
        UserWithStories(
            user = User(
                id = 1L,
                username = "Your story",
                isCurrentUser = true,
                avatarPreviewUrl = "https://picsum.photos/500",
            ),
            stories = persistentListOf(),
        ),
        UserWithStories(
            user = User(
                id = 2L,
                username = "anna2020",
                isCurrentUser = false,
                avatarPreviewUrl = "https://picsum.photos/500",
            ),
            stories = persistentListOf(
                Story(
                    id = 1,
                    publishedAt = "4h",
                    isSeen = false,
                ),
            ),
        ),
        UserWithStories(
            user = User(
                id = 3L,
                username = "alex_wow",
                isCurrentUser = false,
                avatarPreviewUrl = "https://picsum.photos/500",
            ),
            stories = persistentListOf(
                Story(
                    id = 2,
                    publishedAt = "5h",
                    isSeen = false,
                ),
                Story(
                    id = 3,
                    publishedAt = "6h",
                    isSeen = false,
                ),
            ),
        ),
        UserWithStories(
            user = User(
                id = 4L,
                username = "Stephan123",
                isCurrentUser = false,
                avatarPreviewUrl = "https://picsum.photos/500",
            ),
            stories = persistentListOf(),
        ),
        UserWithStories(
            user = User(
                id = 5L,
                username = "Devil666",
                isCurrentUser = false,
                avatarPreviewUrl = "https://picsum.photos/500",
            ),
            stories = persistentListOf(
                Story(
                    id = 4,
                    publishedAt = "1h",
                    isSeen = false,
                ),
            ),
        ),
        UserWithStories(
            user = User(
                id = 6L,
                username = "vasilii_d",
                isCurrentUser = false,
                avatarPreviewUrl = "https://picsum.photos/500",
            ),
            stories = persistentListOf(),
        ),
        UserWithStories(
            user = User(
                id = 7L,
                username = "shar_228",
                isCurrentUser = false,
                avatarPreviewUrl = "https://picsum.photos/500",
            ),
            stories = persistentListOf(
                Story(
                    id = 5,
                    publishedAt = "40m",
                    isSeen = false,
                ),
                Story(
                    id = 6,
                    publishedAt = "45m",
                    isSeen = false,
                ),
                Story(
                    id = 7,
                    publishedAt = "3h",
                    isSeen = false,
                ),
            ),
        ),
        UserWithStories(
            user = User(
                id = 8L,
                username = "peppi",
                isCurrentUser = false,
                avatarPreviewUrl = "https://picsum.photos/500",
            ),
            stories = persistentListOf(
                Story(
                    id = 8,
                    publishedAt = "1h",
                    isSeen = false,
                ),
            ),
        ),
        UserWithStories(
            user = User(
                id = 9L,
                username = "amazing_man",
                isCurrentUser = false,
                avatarPreviewUrl = "https://picsum.photos/500",
            ),
            stories = persistentListOf(),
        ),
        UserWithStories(
            user = User(
                id = 10L,
                username = "vesna",
                isCurrentUser = false,
                avatarPreviewUrl = "https://picsum.photos/500",
            ),
            stories = persistentListOf(),
        ),
        UserWithStories(
            user = User(
                id = 11L,
                username = "cutORcrap",
                isCurrentUser = false,
                avatarPreviewUrl = "https://picsum.photos/500",
            ),
            stories = persistentListOf(
                Story(
                    id = 9,
                    publishedAt = "24h",
                    isSeen = false,
                ),
            ),
        ),
        UserWithStories(
            user = User(
                id = 12L,
                username = "Stanislau_ll",
                isCurrentUser = false,
                avatarPreviewUrl = "https://picsum.photos/500",
            ),
            stories = persistentListOf(),
        ),
    ).sortedWith(
        compareByDescending<UserWithStories> { it.user.isCurrentUser }
            .thenByDescending { it.hasStories }
    )
}
