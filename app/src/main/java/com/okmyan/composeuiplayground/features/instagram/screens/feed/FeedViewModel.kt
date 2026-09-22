package com.okmyan.composeuiplayground.features.instagram.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okmyan.composeuiplayground.features.instagram.domain.model.UserWithStories
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.GetFeedStoriesUseCase
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.GetUsersWithStoriesUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class FeedViewModel(
    private val getFeedStoriesUseCase: GetFeedStoriesUseCase,
    private val getUsersWithStoriesUseCase: GetUsersWithStoriesUseCase,
) : ViewModel() {

    private var _uiState = MutableStateFlow(FeedState())
    val uiState = _uiState.asStateFlow()

    private val _preloadUserWithStories = MutableStateFlow(emptyList<UserWithStories>())
    val preloadUserWithStories = _preloadUserWithStories.asStateFlow()

    init {
        Timber.d("Init block")
        viewModelScope.launch(CoroutineName("FeedViewModel - getFeedStories")) {
            getFeedStories()
        }
    }

    private suspend fun getFeedStories() {
        var userWithStoriesPreloaded = false
        getFeedStoriesUseCase().collect { usersWithStories ->
            _uiState.value = _uiState.value.copy(
                feedStories = usersWithStories.toImmutableList()
            )

            if (!userWithStoriesPreloaded && usersWithStories.isNotEmpty()) {
                userWithStoriesPreloaded = true

                // Preload only first 7 users including the account owner
                preloadUserWithStories(
                    userIds = usersWithStories
                        .take(7)
                        .map { it.storyOwnerId })
            }
        }
    }

    private suspend fun preloadUserWithStories(userIds: List<Long>) {
        _preloadUserWithStories.value = getUsersWithStoriesUseCase(userIds)
    }

    override fun onCleared() {
        Timber.d("onCleared")
    }
}
