package com.okmyan.composeuiplayground.features.instagram.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class FeedStory(
    val storyOwner: InstagramUser,
    val hasNonSeenStories: Boolean,
) {
    val storyOwnerId = storyOwner.id
    val isAccountOwner = storyOwner.isAccountOwner
    val isMuted = storyOwner.isMuted
}
