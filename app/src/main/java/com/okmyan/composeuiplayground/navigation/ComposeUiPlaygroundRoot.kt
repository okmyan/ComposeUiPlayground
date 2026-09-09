package com.okmyan.composeuiplayground.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.okmyan.composeuiplayground.features.instagram.navigation.InstagramNavigation
import com.okmyan.composeuiplayground.navigation.Route.A
import com.okmyan.composeuiplayground.navigation.Route.B
import com.okmyan.composeuiplayground.navigation.Route.C
import com.okmyan.composeuiplayground.navigation.Route.InstagramGraph
import com.okmyan.composeuiplayground.navigation.Route.Menu
import com.okmyan.composeuiplayground.screens.AScreen
import com.okmyan.composeuiplayground.screens.BScreen
import com.okmyan.composeuiplayground.screens.CScreen
import com.okmyan.composeuiplayground.screens.MenuScreen

@Composable
fun ComposeUiPlaygroundRoot(modifier: Modifier = Modifier) {
    val topLevelRoutes = setOf<Route>(InstagramGraph)

    val navigationState = rememberNavigationState(
        startRoute = InstagramGraph,
        topLevelRoutes = topLevelRoutes,
    )

    val navigator = remember { Navigator(navigationState) }

    ComposeUiPlaygroundNavDisplay(
        navigator = navigator,
        modifier = modifier
    )
}

@Composable
fun ComposeUiPlaygroundNavDisplay(
    navigator: Navigator<Route>,
    modifier: Modifier = Modifier,
) {
    val entryProvider = entryProvider {
        entry<Menu> {
            MenuScreen(
                onGoToInstagram = { navigator.navigate(InstagramGraph) },
                onGoToA = { navigator.navigate(A) },
                onGoToB = { navigator.navigate(B) },
                onGoToC = { navigator.navigate(C) },
            )
        }

        entry<InstagramGraph> {
            InstagramNavigation()
        }

        simpleScreensSection()
    }

    NavDisplay(
        entries = navigator.state.toEntries(entryProvider),
        modifier = modifier,
        onBack = { navigator.goBack() },
        sceneStrategies = remember { listOf(DialogSceneStrategy()) },
    )
}

fun EntryProviderScope<Route>.simpleScreensSection() {
    entry<A> {
        AScreen()
    }
    entry<B> {
        BScreen()
    }
    entry<C> {
        CScreen()
    }
}
