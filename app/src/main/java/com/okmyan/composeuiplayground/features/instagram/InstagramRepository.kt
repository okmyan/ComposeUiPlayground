package com.okmyan.composeuiplayground.features.instagram

class InstagramRepository {
    val stories = listOf(
        StoryState(
            id = 1L,
            title = "Your story",
            avatarPreviewUrl = "https://picsum.photos/500",
            hasStory = false,
            usersAvatar = true,
        ),
        StoryState(
            id = 2L,
            title = "anna2020",
            avatarPreviewUrl = "https://picsum.photos/500",
            hasStory = true,
            usersAvatar = false,
        ),
        StoryState(
            id = 3L,
            title = "alex_wow",
            avatarPreviewUrl = "https://picsum.photos/500",
            hasStory = true,
            usersAvatar = false,
        ),
        StoryState(
            id = 4L,
            title = "Stephan123",
            avatarPreviewUrl = "https://picsum.photos/500",
            hasStory = false,
            usersAvatar = false,
        ),
        StoryState(
            id = 5L,
            title = "Devil666",
            avatarPreviewUrl = "https://picsum.photos/500",
            hasStory = true,
            usersAvatar = false,
        ),
        StoryState(
            id = 6L,
            title = "vasilii_d",
            avatarPreviewUrl = "https://picsum.photos/500",
            hasStory = false,
            usersAvatar = false,
        ),
        StoryState(
            id = 7L,
            title = "shar_228",
            avatarPreviewUrl = "https://picsum.photos/500",
            hasStory = true,
            usersAvatar = false,
        ),
        StoryState(
            id = 8L,
            title = "peppi",
            avatarPreviewUrl = "https://picsum.photos/500",
            hasStory = true,
            usersAvatar = false,
        ),
        StoryState(
            id = 9L,
            title = "amazing_man",
            avatarPreviewUrl = "https://picsum.photos/500",
            hasStory = false,
            usersAvatar = false,
        ),
        StoryState(
            id = 10L,
            title = "vesna",
            avatarPreviewUrl = "https://picsum.photos/500",
            hasStory = false,
            usersAvatar = false,
        ),
        StoryState(
            id = 11L,
            title = "cutORcrap",
            avatarPreviewUrl = "https://picsum.photos/500",
            hasStory = true,
            usersAvatar = false,
        ),
        StoryState(
            id = 12L,
            title = "Stanislau_ll",
            avatarPreviewUrl = "https://picsum.photos/500",
            hasStory = false,
            usersAvatar = false,
        ),
    ).sortedWith(
        compareByDescending<StoryState> { it.usersAvatar }
            .thenByDescending { it.hasStory }
    )
}
