package com.okmyan.composeuiplayground.features.instagram.screens.story

import com.okmyan.composeuiplayground.features.instagram.domain.model.UserWithStories
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class StoryPagerState(
    val usersWithStories: ImmutableList<UserWithStories> = persistentListOf(),
)
