package com.okmyan.composeuiplayground.features.instagram.screens.feed

import com.okmyan.composeuiplayground.features.instagram.domain.model.FeedStory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class FeedState(
    val feedStories: ImmutableList<FeedStory> = persistentListOf(),
) {
    val friendsStoryOwnerIds: List<Long>
        get() = feedStories
            .filter { !it.isAccountOwner }
            .map { it.storyOwnerId }
}
