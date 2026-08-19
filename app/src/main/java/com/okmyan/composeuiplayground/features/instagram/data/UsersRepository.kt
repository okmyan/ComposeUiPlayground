package com.okmyan.composeuiplayground.features.instagram.data

import com.okmyan.composeuiplayground.features.instagram.domain.model.InstagramUser

class UsersRepository {

    val users = listOf(
        InstagramUser(
            id = 1L,
            username = "john950808",
            isCurrentUser = true,
            avatarPreviewUrl = "https://picsum.photos/500",
        ),
        InstagramUser(
            id = 2L,
            username = "anna2522",
            isCurrentUser = false,
            avatarPreviewUrl = "https://picsum.photos/500",
        ),
        InstagramUser(
            id = 3L,
            username = "alex_stylist",
            isCurrentUser = false,
            avatarPreviewUrl = "https://picsum.photos/500",
        ),
        InstagramUser(
            id = 4L,
            username = "Stephan123",
            isCurrentUser = false,
            avatarPreviewUrl = "https://picsum.photos/500",
        ),
        InstagramUser(
            id = 5L,
            username = "Dev17",
            isCurrentUser = false,
            avatarPreviewUrl = "https://picsum.photos/500",
        ),
        InstagramUser(
            id = 6L,
            username = "shar_228",
            isCurrentUser = false,
            avatarPreviewUrl = "https://picsum.photos/500",
        ),
        InstagramUser(
            id = 7L,
            username = "peppi",
            isCurrentUser = false,
            avatarPreviewUrl = "https://picsum.photos/500",
        ),
        InstagramUser(
            id = 8L,
            username = "vesna",
            isCurrentUser = false,
            avatarPreviewUrl = "https://picsum.photos/500",
        ),
        InstagramUser(
            id = 9L,
            username = "longtimenosee",
            isCurrentUser = false,
            avatarPreviewUrl = "https://picsum.photos/500",
        ),
        InstagramUser(
            id = 10L,
            username = "Stanislau_ll",
            isCurrentUser = false,
            avatarPreviewUrl = "https://picsum.photos/500",
        ),
    )
}
