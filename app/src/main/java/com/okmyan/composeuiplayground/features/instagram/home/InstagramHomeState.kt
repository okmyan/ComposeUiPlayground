package com.okmyan.composeuiplayground.features.instagram.home

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

data class InstagramHomeState(
    val usersWithStories: ImmutableList<UserWithStories> = persistentListOf(),
)

@Serializable
data class UserWithStories(
    val user: User,
    val stories: ImmutableList<Story>,
) {
    val hasStories = stories.isNotEmpty()
}

@Serializable
data class Story(
    val id: Long = 0L,
    val publishedAt: String = "",
    val isSeen: Boolean = false,
)

@Serializable
data class User(
    val id: Long = 0L,
    val username: String = "",
    val isCurrentUser: Boolean = false,
    val avatarPreviewUrl: String = "",
)
