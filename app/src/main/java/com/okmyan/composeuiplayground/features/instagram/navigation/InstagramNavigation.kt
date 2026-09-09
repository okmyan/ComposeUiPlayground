package com.okmyan.composeuiplayground.features.instagram.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import com.okmyan.composeuiplayground.features.instagram.domain.model.UserWithStories
import com.okmyan.composeuiplayground.features.instagram.screens.home.InstagramHomeScreen
import com.okmyan.composeuiplayground.features.instagram.screens.story.InstagramStoryScreen
import com.okmyan.composeuiplayground.navigation.Navigator
import com.okmyan.composeuiplayground.navigation.Route
import com.okmyan.composeuiplayground.navigation.Route.InstagramGraph.InstagramHome
import com.okmyan.composeuiplayground.navigation.rememberNavigationState
import com.okmyan.composeuiplayground.navigation.toEntries
import com.okmyan.composeuiplayground.utils.extensions.findNext
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
    SharedTransitionLayout {
        val entryProvider = remember(navigator) {
            entryProvider {
                entry<InstagramHome> {
                    InstagramHomeScreen(
                        onGoToStories = { user, stories ->
                            navigator.navigateToUserStory(user, stories)
                        },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                    )
                }
                entry<InstagramStoryGraph> { instagramStory ->
                    val hasPreviousStories =
                        instagramStory.stories.getOrNull(1)?.userId != instagramStory.user.userId
                    InstagramStoryScreen(
                        userWithStories = instagramStory.user,
                        hasPreviousStory = hasPreviousStories,
                        onGoToPrevious = {
                            val iterator = instagramStory.stories.reversed().iterator()
                            val previousUser =
                                iterator.findNext { it.userId == instagramStory.user.userId }

                            navigator.navigateToUserStory(previousUser, instagramStory.stories)
                        },
                        onGoToNext = {
                            val iterator = instagramStory.stories.iterator()
                            val nextUser =
                                iterator.findNext { it.userId == instagramStory.user.userId }

                            navigator.navigateToUserStory(nextUser, instagramStory.stories)
                        },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                        modifier = modifier,
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
            popTransitionSpec = {
                EnterTransition.None togetherWith ExitTransition.None
            },
            predictivePopTransitionSpec = {
                EnterTransition.None togetherWith ExitTransition.None
            }
        )
    }
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
