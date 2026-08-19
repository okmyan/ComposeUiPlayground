package com.okmyan.composeuiplayground.features.instagram

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.okmyan.composeuiplayground.features.instagram.domain.model.UserWithStories
import com.okmyan.composeuiplayground.features.instagram.screens.home.InstagramHomeScreen
import com.okmyan.composeuiplayground.features.instagram.screens.home.InstagramHomeViewModel
import com.okmyan.composeuiplayground.features.instagram.screens.story.InstagramStoryScreen
import com.okmyan.composeuiplayground.navigation.Navigator
import com.okmyan.composeuiplayground.navigation.Route
import com.okmyan.composeuiplayground.navigation.Route.InstagramGraph.InstagramHome
import com.okmyan.composeuiplayground.navigation.rememberNavigationState
import com.okmyan.composeuiplayground.navigation.toEntries
import com.okmyan.composeuiplayground.utils.extensions.findNext
import org.koin.androidx.compose.koinViewModel
import com.okmyan.composeuiplayground.navigation.Route.InstagramGraph.InstagramStory as InstagramStoryGraph

@Composable
fun InstagramNavigation(
    modifier: Modifier = Modifier,
) {
    val topLevelRoutes = setOf(InstagramHome)

    val navigationState = rememberNavigationState<Route>(
        startRoute = InstagramHome,
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
            entry<InstagramHome> {
                InstagramHomeScreen(
                    viewModel = viewModel,
                    onGoToStories = { user, stories ->
                        navigator.navigateToUserStory(user, stories)
                    }
                )
            }
            entry<InstagramStoryGraph> { instagramStory ->
                InstagramStoryScreen(
                    userWithStories = instagramStory.userWithStories,
                    viewModel = viewModel,
                    hasPreviousStory = viewModel.isItFirstUserInHighlights(instagramStory.userWithStories),
                    onGoToPrevious = {
                        val iterator = instagramStory.stories.reversed().iterator()
                        val previousUser =
                            iterator.findNext { it.user.id == instagramStory.userWithStories.user.id }

                        navigator.navigateToUserStory(previousUser, instagramStory.stories)
                    },
                    onGoToNext = {
                        val iterator = instagramStory.stories.iterator()
                        val nextUser =
                            iterator.findNext { it.user.id == instagramStory.userWithStories.user.id }

                        navigator.navigateToUserStory(nextUser, instagramStory.stories)
                    }
                )
            }
        }
    }

    NavDisplay(
        entries = navigator.state.toEntries(entryProvider),
        modifier = modifier,
        onBack = {
            if (navigator.isRouteOnTopOfBackStack(InstagramHome)) {
                navigator.goBack()
            } else {
                navigator.resetStack()
            }
        },
        sceneStrategies = remember { listOf(DialogSceneStrategy()) },
    )
}

private fun Navigator<Route>.navigateToUserStory(
    userWithStories: UserWithStories?,
    usersWithStories: List<UserWithStories>,
) {
    if (userWithStories != null) {
        navigate(InstagramStoryGraph(userWithStories, usersWithStories), unique = true)
    } else {
        resetStack()
    }
}
