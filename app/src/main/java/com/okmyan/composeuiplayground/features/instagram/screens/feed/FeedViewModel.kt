package com.okmyan.composeuiplayground.features.instagram.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okmyan.composeuiplayground.features.instagram.domain.model.FeedStory
import com.okmyan.composeuiplayground.features.instagram.domain.model.UserWithStories
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.GetUsersWithStoriesUseCase
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.ObserveFeedStoriesUseCase
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.ObserveSortedFeedStoriesUseCase
import com.okmyan.composeuiplayground.features.instagram.utils.STORY_COMPARATOR
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class FeedViewModel(
    private val observeFeedStoriesUseCase: ObserveFeedStoriesUseCase,
    private val observeSortedFeedStoriesUseCase: ObserveSortedFeedStoriesUseCase,
    private val getUsersWithStoriesUseCase: GetUsersWithStoriesUseCase,
) : ViewModel() {

    private var _uiState = MutableStateFlow(FeedState())
    val uiState = _uiState.asStateFlow()

    private val _preloadUserWithStories = MutableStateFlow(emptyList<UserWithStories>())
    val preloadUserWithStories = _preloadUserWithStories.asStateFlow()

    private var isInitiallySorted = false
    private var sortedStories = listOf<FeedStory>()
    private var sortedIds = listOf<Long>()

    init {
        Timber.d("Init block")
        viewModelScope.launch(CoroutineName("FeedViewModel - observeFeedStories")) {
            observeFeedStories()
        }

        viewModelScope.launch(CoroutineName("FeedViewModel - observeSortedFeedStories")) {
            observeSortedFeedStories()
        }
    }

    private suspend fun observeFeedStories() {
        var userWithStoriesPreloaded = false
        observeFeedStoriesUseCase().collect { usersWithStories ->

            val sortedUsersWithStories = if (isInitiallySorted) {
                usersWithStories.sortedBy { story ->
                    sortedIds.indexOf(story.storyOwnerId).let { if (it >= 0) it else Int.MAX_VALUE }
                }
            } else {
                isInitiallySorted = true
                usersWithStories.sortedWith(STORY_COMPARATOR).also {
                    sortedIds = it.map { story -> story.storyOwnerId }
                }
            }

            _uiState.value = _uiState.value.copy(
                feedStories = sortedUsersWithStories.toImmutableList()
            )

            if (!userWithStoriesPreloaded && sortedUsersWithStories.isNotEmpty()) {
                userWithStoriesPreloaded = true

                // Preload only first 7 users including the account owner
                preloadUserWithStories(
                    userIds = sortedUsersWithStories
                        .take(7)
                        .map { it.storyOwnerId })
            }
        }
    }

    private suspend fun observeSortedFeedStories() {
        observeSortedFeedStoriesUseCase().collect {
            sortedStories = it
        }
    }

    private suspend fun preloadUserWithStories(userIds: List<Long>) {
        _preloadUserWithStories.value = getUsersWithStoriesUseCase(userIds)
    }

    fun sortStories() {
        Timber.d("sortStories")
        _uiState.value = _uiState.value.copy(
            feedStories = sortedStories.toImmutableList()
        )

        sortedIds = sortedStories.map { it.storyOwnerId }
    }

    override fun onCleared() {
        Timber.d("onCleared")
    }
}
