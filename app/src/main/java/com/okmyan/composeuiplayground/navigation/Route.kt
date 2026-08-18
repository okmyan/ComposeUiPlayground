package com.okmyan.composeuiplayground.navigation

import androidx.navigation3.runtime.NavKey
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
        data class InstagramStory(val storyId: Long) : Route
    }

    @Serializable
    data object A : Route

    @Serializable
    data object B : Route

    @Serializable
    data object C : Route

}
