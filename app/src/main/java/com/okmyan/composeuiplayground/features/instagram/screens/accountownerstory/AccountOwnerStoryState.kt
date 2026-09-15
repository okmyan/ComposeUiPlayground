package com.okmyan.composeuiplayground.features.instagram.screens.accountownerstory

import com.okmyan.composeuiplayground.features.instagram.domain.model.InstagramStory
import com.okmyan.composeuiplayground.features.instagram.domain.model.InstagramUser
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class AccountOwnerStoryState(
    val storyOwner: InstagramUser = InstagramUser(),
    val stories: ImmutableList<InstagramStory> = persistentListOf(),
    val activeStoryIndex: Int = 0,
) {
    val storyOwnerId = storyOwner.id

    val activeStory: InstagramStory
        get() = stories.getOrNull(activeStoryIndex)
            ?: error("AccountOwnerStoryState is broken, $this")

    val isActiveStoryLastOne: Boolean
        get() = activeStoryIndex == stories.size - 1
}
