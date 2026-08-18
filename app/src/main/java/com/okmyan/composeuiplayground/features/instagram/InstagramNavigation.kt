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

    val entryProvider = entryProvider {
        entry<Instagram> {
            InstagramScreen(
                viewModel = viewModel,
                onGoToStory = { id ->
                    navigator.navigate(InstagramStory(id))
                }
            )
        }
        entry<InstagramStory> { instagramStory ->
            InstagramStoryScreen(
                viewModel = viewModel,
                id = instagramStory.storyId,
            )
        }
    }

    NavDisplay(
        entries = navigator.state.toEntries(entryProvider),
        modifier = modifier,
        onBack = { navigator.goBack() },
        sceneStrategies = remember { listOf(DialogSceneStrategy()) },
    )
}
