package com.okmyan.composeuiplayground.features.instagram

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.okmyan.composeuiplayground.features.instagram.home.InstagramHomeScreen
import com.okmyan.composeuiplayground.features.instagram.home.InstagramHomeViewModel
import com.okmyan.composeuiplayground.features.instagram.home.UserWithStories
import com.okmyan.composeuiplayground.features.instagram.story.InstagramStoryScreen
import com.okmyan.composeuiplayground.navigation.Navigator
import com.okmyan.composeuiplayground.navigation.Route
import com.okmyan.composeuiplayground.navigation.Route.InstagramGraph.Instagram
import com.okmyan.composeuiplayground.navigation.Route.InstagramGraph.InstagramStory
import com.okmyan.composeuiplayground.navigation.rememberNavigationState
import com.okmyan.composeuiplayground.navigation.toEntries
import org.koin.androidx.compose.koinViewModel

@Composable
fun InstagramNavigation(
    modifier: Modifier = Modifier,
) {
    val topLevelRoutes = setOf(Instagram)

    val navigationState = rememberNavigationState<Route>(
        startRoute = Instagram,
        topLevelRoutes = topLevelRoutes,
    )
    val navigator = remember { Navigator(navigationState) }

    InstagramNavDisplay(
        navigator = navigator,
        modifier = modifier,
    )
}

@Composable
fun InstagramNavDisplay(
    navigator: Navigator<Route>,
    modifier: Modifier = Modifier,
) {
    val viewModel: InstagramHomeViewModel = koinViewModel()

    val entryProvider = remember(viewModel, navigator) {
        entryProvider {
            entry<Instagram> {
                InstagramHomeScreen(
                    viewModel = viewModel,
                    onGoToStories = { user ->
                        navigator.navigateToUserStory(user)
                    }
                )
            }
            entry<InstagramStory> { instagramStory ->
                InstagramStoryScreen(
                    userWithStories = instagramStory.userWithStories,
                    viewModel = viewModel,
                    hasPreviousStory = viewModel.isItFirstUserInHighlights(instagramStory.userWithStories),
                    onGoToPrevious = {
                        val previousUser = viewModel.getPreviousUserWithStories(instagramStory.userWithStories)
                        navigator.navigateToUserStory(previousUser)
                    },
                    onGoToNext = {
                        val nextUser = viewModel.getNextUserWithStories(instagramStory.userWithStories)
                        navigator.navigateToUserStory(nextUser)
                    }
                )
            }
        }
    }

    NavDisplay(
        entries = navigator.state.toEntries(entryProvider),
        modifier = modifier,
        onBack = {
            if (navigator.isRouteOnTopOfBackStack(Instagram)) {
                navigator.goBack()
            } else {
                navigator.resetStack()
            }
        },
        sceneStrategies = remember { listOf(DialogSceneStrategy()) },
    )
}

private fun Navigator<Route>.navigateToUserStory(userWithStories: UserWithStories?) {
    if (userWithStories != null) {
        navigate(InstagramStory(userWithStories), unique = true)
    } else {
        resetStack()
    }
}
