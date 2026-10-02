package com.okmyan.composeuiplayground.features.instagram.domain.model

import kotlinx.collections.immutable.ImmutableList

data class UserWithStories(
    val user: InstagramUser,
    val allStoriesSeen: Boolean,
    val stories: ImmutableList<InstagramStory>,
)
