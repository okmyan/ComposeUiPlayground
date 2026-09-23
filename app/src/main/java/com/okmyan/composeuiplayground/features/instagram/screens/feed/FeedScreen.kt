package com.okmyan.composeuiplayground.features.instagram.screens.feed

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.okmyan.composeuiplayground.features.instagram.domain.model.FeedStory
import com.okmyan.composeuiplayground.features.instagram.screens.components.PreloadUsersWithStories
import com.okmyan.composeuiplayground.features.instagram.screens.feed.components.FeedStory
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Duration.Companion.seconds

@Composable
fun FeedScreen(
    viewModel: FeedViewModel = koinViewModel(),
    onGoToSelfStories: (Long) -> Unit,
    onGoToStories: (Long, List<Long>) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val refreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val preloadUserWithStories by viewModel.preloadUserWithStories.collectAsStateWithLifecycle()
    PreloadUsersWithStories(preloadUserWithStories)

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            coroutineScope.launch(CoroutineName("FeedScreen - onRefresh")) {
                isRefreshing = true
                delay(1.seconds)
                isRefreshing = false
            }
        },
        modifier = Modifier.fillMaxSize(),
        state = refreshState,
    ) {
        FeedScreenContent(
            state = state,
            onGoToSelfStories = {
                onGoToSelfStories(it.storyOwnerId)
            },
            onGoToStories = {
                onGoToStories(it.storyOwnerId, state.friendsStoryOwnerIds)
            },
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun FeedScreenContent(
    state: FeedState,
    onGoToSelfStories: (FeedStory) -> Unit,
    onGoToStories: (FeedStory) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items(state.feedStories) { feedStory ->
                FeedStory(
                    feedStory = feedStory,
                    onClick = {
                        if (feedStory.isAccountOwner) {
                            onGoToSelfStories(feedStory)
                        } else {
                            onGoToStories(feedStory)
                        }
                    },
                    onAddStory = {
                        Toast.makeText(context, "add", Toast.LENGTH_SHORT).show()
                    },
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                )
            }
        }

        Spacer(modifier = Modifier.height(13.dp))

        Box(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "This is an Instagram page")
        }
    }
}
