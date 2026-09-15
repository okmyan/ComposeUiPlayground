package com.okmyan.composeuiplayground.features.instagram.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.GetFeedStoriesUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class FeedViewModel(
    private val getFeedStoriesUseCase: GetFeedStoriesUseCase,
) : ViewModel() {

    private var _uiState = MutableStateFlow(FeedState())
    val uiState = _uiState.asStateFlow()

    init {
        Timber.d("Init block")
        viewModelScope.launch(CoroutineName("FeedViewModel - getFeedStories")) {
            getFeedStories()
        }
    }

    private suspend fun getFeedStories() {
        getFeedStoriesUseCase().collect { usersWithStories ->
            _uiState.value = _uiState.value.copy(
                feedStories = usersWithStories.toImmutableList()
            )
        }
    }

    override fun onCleared() {
        Timber.d("onCleared")
    }
}
