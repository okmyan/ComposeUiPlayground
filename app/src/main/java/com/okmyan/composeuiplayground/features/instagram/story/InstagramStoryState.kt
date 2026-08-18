package com.okmyan.composeuiplayground.features.instagram.story

import com.okmyan.composeuiplayground.features.instagram.home.Story
import com.okmyan.composeuiplayground.features.instagram.home.User
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class InstagramStoryState(
    val user: User = User(),
    val stories: ImmutableList<Story> = persistentListOf(),
    val activeStoryIndex: Int = 0,
) {
    val activeStory: Story
        get() = stories.getOrNull(activeStoryIndex) ?: error("InstagramStoryState is broken, $this")

    val isActiveStoryLastOne: Boolean
        get() = activeStoryIndex == stories.size - 1
}
