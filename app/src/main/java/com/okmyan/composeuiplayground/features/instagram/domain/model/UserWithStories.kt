package com.okmyan.composeuiplayground.features.instagram.domain.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.Serializable

@Serializable
data class UserWithStories(
    val user: InstagramUser,
    val stories: ImmutableList<InstagramStory>,
) {
    val hasStories = stories.isNotEmpty()
    val hasNonSeenStories = stories.any { !it.isSeen }
}
