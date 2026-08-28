package com.okmyan.composeuiplayground.features.instagram.screens.story

import androidx.compose.ui.text.input.TextFieldValue
import com.okmyan.composeuiplayground.features.instagram.domain.model.InstagramStory
import com.okmyan.composeuiplayground.features.instagram.domain.model.InstagramUser
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class InstagramStoryState(
    val user: InstagramUser = InstagramUser(),
    val stories: ImmutableList<InstagramStory> = persistentListOf(),
    val activeStoryIndex: Int = 0,
    val enteredMessage: TextFieldValue = TextFieldValue(""),
) {
    val activeStory: InstagramStory
        get() = stories.getOrNull(activeStoryIndex) ?: error("InstagramStoryState is broken, $this")

    val isActiveStoryLastOne: Boolean
        get() = activeStoryIndex == stories.size - 1
}
