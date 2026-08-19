package com.okmyan.composeuiplayground.features.instagram.screens.home

import com.okmyan.composeuiplayground.features.instagram.domain.model.UserWithStories
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class InstagramHomeState(
    val usersWithStories: ImmutableList<UserWithStories> = persistentListOf(),
)
