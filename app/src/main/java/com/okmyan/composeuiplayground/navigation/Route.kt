package com.okmyan.composeuiplayground.navigation

import androidx.navigation3.runtime.NavKey
import com.okmyan.composeuiplayground.features.instagram.home.UserWithStories
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route: NavKey {

    @Serializable
    data object Menu : Route

    @Serializable
    data object InstagramGraph: Route {

        @Serializable
        data object Instagram : Route

        @Serializable
        data class InstagramStory(val userWithStories: UserWithStories) : Route
    }

    @Serializable
    data object A : Route

    @Serializable
    data object B : Route

    @Serializable
    data object C : Route

}
