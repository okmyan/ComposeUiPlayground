package com.okmyan.composeuiplayground.features.instagram

import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

class InstagramViewModel(
    repository: InstagramRepository,
) : ViewModel() {

    private var _uiState = MutableStateFlow(
        InstagramState(
            stories = repository.stories.toImmutableList()
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        Timber.d("Init block")
    }

}
