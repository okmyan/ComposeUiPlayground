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
import com.okmyan.composeuiplayground.features.instagram.screens.accountownerstory.AccountOwnerStoryScreen
import com.okmyan.composeuiplayground.features.instagram.screens.feed.FeedScreen
import com.okmyan.composeuiplayground.features.instagram.screens.story.StoriesPagerScreen
import com.okmyan.composeuiplayground.navigation.Navigator
import com.okmyan.composeuiplayground.navigation.Route
import com.okmyan.composeuiplayground.navigation.Route.InstagramGraph.AccountOwnerStory
import com.okmyan.composeuiplayground.navigation.Route.InstagramGraph.InstagramFeed
import com.okmyan.composeuiplayground.navigation.rememberNavigationState
import com.okmyan.composeuiplayground.navigation.toEntries
import com.okmyan.composeuiplayground.navigation.Route.InstagramGraph.InstagramStory as InstagramStoryGraph

@Composable
fun InstagramNavigation(
    modifier: Modifier = Modifier,
) {
    val topLevelRoutes = setOf(InstagramFeed)

    val navigationState = rememberNavigationState<Route>(
        startRoute = InstagramFeed,
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
                entry<InstagramFeed> {
                    FeedScreen(
                        onGoToSelfStories = { accountOwnerId ->
                            navigator.navigate(AccountOwnerStory(accountOwnerId))
                        },
                        onGoToStories = { selectedStoryOwnerId, storyOwnerIds ->
                            navigator.navigate(
                                InstagramStoryGraph(selectedStoryOwnerId, storyOwnerIds),
                                unique = true,
                            )
                        },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                    )
                }
                entry<InstagramStoryGraph> { instagramStory ->
                    StoriesPagerScreen(
                        selectedStoryOwnerId = instagramStory.selectedStoryOwnerId,
                        storyOwnerIds = instagramStory.storyOwnerIds,
                        onStoriesEnd = { navigator.goBack() },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                        modifier = modifier,
                    )
                }
                entry<AccountOwnerStory> { accountOwner ->
                    AccountOwnerStoryScreen(
                        accountOwnerId = accountOwner.accountOwnerId,
                        closeStory = { navigator.goBack() },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                    )
                }
            }
        }
        NavDisplay(
            entries = navigator.state.toEntries(entryProvider),
            modifier = modifier,
            onBack = {
                if (navigator.isRouteOnTopOfBackStack(InstagramFeed)) {
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
