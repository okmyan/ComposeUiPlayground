package com.okmyan.composeuiplayground.features.instagram.screens.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.okmyan.composeuiplayground.features.instagram.domain.model.UserWithStories
import com.okmyan.composeuiplayground.features.instagram.screens.home.components.PreloadStories
import com.okmyan.composeuiplayground.features.instagram.screens.home.components.Story
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun InstagramHomeScreen(
    viewModel: InstagramHomeViewModel = koinViewModel(),
    onGoToStories: (UserWithStories, List<UserWithStories>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val refreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    PreloadStories(state.usersWithStories)

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            coroutineScope.launch {
                isRefreshing = true
                delay(1000)
                isRefreshing = false
            }
        },
        modifier = Modifier.fillMaxSize(),
        state = refreshState,
    ) {
        InstagramHomeScreenContent(
            state = state,
            onGoToStories = { userWithStories ->
                onGoToStories(userWithStories, state.usersWithStories)
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun InstagramHomeScreenContent(
    state: InstagramHomeState,
    onGoToStories: (UserWithStories) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            items(state.usersWithStories) { userWithStories ->
                Story(
                    userWithStories = userWithStories,
                    onClick = {
                        onGoToStories(userWithStories)
                    },
                    onAddStory = {
                        Toast.makeText(context, "add", Toast.LENGTH_SHORT).show()
                    },
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
