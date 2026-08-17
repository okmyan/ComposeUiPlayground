package com.okmyan.composeuiplayground.features.instagram

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class InstagramState(
    val stories: ImmutableList<StoryState> = persistentListOf(),
)

data class StoryState(
    val id: Long,
    val title: String,
    val avatarPreviewUrl: String,
    val hasStory: Boolean,
    val usersAvatar: Boolean,
)
