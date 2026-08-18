package com.okmyan.composeuiplayground.features.instagram

import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

class InstagramViewModel(
    repository: InstagramRepository,
) : ViewModel() {

    private var _uiState = MutableStateFlow(InstagramState())
    val uiState = _uiState.asStateFlow()

    val storyWithContentIds: List<Long>

    init {
        Timber.d("Init block")
        val stories = repository.stories

        storyWithContentIds = stories.filter { it.hasStory }.map { it.id }
        _uiState.value = _uiState.value.copy(
            stories = stories.toImmutableList()
        )
    }

    override fun onCleared() {
        Timber.d("clear")
    }
}
