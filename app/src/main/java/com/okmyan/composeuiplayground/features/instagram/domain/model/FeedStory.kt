package com.okmyan.composeuiplayground.features.instagram.domain.model

data class FeedStory(
    val storyOwner: InstagramUser,
    val hasNonSeenStories: Boolean,
    val hasNonSeenStoriesForClosedFriendsOnly: Boolean,
) {
    val storyOwnerId = storyOwner.id
    val isAccountOwner = storyOwner.isAccountOwner
    val isMuted = storyOwner.isMuted
}
