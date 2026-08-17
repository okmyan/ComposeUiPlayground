package com.okmyan.composeuiplayground.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.okmyan.composeuiplayground.features.instagram.InstagramScreen
import com.okmyan.composeuiplayground.screens.AScreen
import com.okmyan.composeuiplayground.screens.BScreen
import com.okmyan.composeuiplayground.screens.CScreen
import com.okmyan.composeuiplayground.screens.MenuScreen

@Composable
fun ComposeUiPlaygroundRoot(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    ComposeUiPlaygroundNavHost(
        navController = navController,
        modifier = modifier
    )
}

@Composable
fun ComposeUiPlaygroundNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Instagram,
        modifier = modifier
    ) {
        composable<Menu> {
            MenuScreen(
                onGoToInstagram = { navController.navigate(Instagram) },
                onGoToA = { navController.navigate(A) },
                onGoToB = { navController.navigate(B) },
                onGoToC = { navController.navigate(C) },
            )
        }
        composable<Instagram> {
            InstagramScreen()
        }
        composable<A> {
            AScreen()
        }
        composable<B> {
            BScreen()
        }
        composable<C> {
            CScreen()
        }
    }
}
