package com.okmyan.composeuiplayground.features.instagram

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
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
    val viewModel: InstagramViewModel = koinViewModel()

    val entryProvider = remember(viewModel, navigator) {
        entryProvider {
            entry<Instagram> {
                InstagramScreen(
                    viewModel = viewModel,
                    onGoToStory = { storyId ->
                        navigator.navigateToStoryId(storyId)
                    }
                )
            }
            entry<InstagramStory> { instagramStory ->
                InstagramStoryScreen(
                    id = instagramStory.storyId,
                    viewModel = viewModel,
                    hasPreviousStory = viewModel.hasPreviousStory(instagramStory.storyId),
                    onGoToPrevious = {
                        val previousStoryId = viewModel.getPreviousStoryId(instagramStory.storyId)
                        navigator.navigateToStoryId(previousStoryId)

                    },
                    onGoToNext = {
                        val nextStoryId = viewModel.getNextStoryId(instagramStory.storyId)
                        navigator.navigateToStoryId(nextStoryId)
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

private fun Navigator<Route>.navigateToStoryId(storyId: Long?) {
    if (storyId != null) {
        navigate(InstagramStory(storyId), unique = true)
    } else {
        resetStack()
    }
}
