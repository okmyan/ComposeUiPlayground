package com.okmyan.composeuiplayground.features.instagram.screens.accountownerstory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.GetSelfStoriesUseCase
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.SeeStoriesUseCase
import com.okmyan.composeuiplayground.features.instagram.utils.getActiveStoryIndex
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class AccountOwnerStoryViewModel(
    private val getSelfStoriesUseCase: GetSelfStoriesUseCase,
    private val seeStoriesUseCase: SeeStoriesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountOwnerStoryState())
    val uiState = _uiState.asStateFlow()

    init {
        Timber.d("Init block")

        viewModelScope.launch(CoroutineName("AccountOwnerStoryViewModel - getSelfStories")) {
            getSelfStories()
        }
    }

    private suspend fun getSelfStories() {
        val userWithStories = getSelfStoriesUseCase()

        // If the user opens the stories that already watched, we show them again
        val stories = if (userWithStories.allStoriesSeen) {
            userWithStories.stories.map {
                it.copy(isSeen = false)
            }
        } else {
            userWithStories.stories
        }.toImmutableList()

        _uiState.value = _uiState.value.copy(
            storyOwner = userWithStories.user,
            stories = stories
        )

        updateActiveStoryIndex()
    }

    fun onStorySeen(storyId: Long) {
        viewModelScope.launch(CoroutineName("AccountOwnerStoryViewModel - onStorySeen ($storyId)")) {
            seeStoriesUseCase(storyId)
        }
    }

    fun onStoryEnded(seenStoryIndex: Int) {
        val stories = _uiState.value.stories.mapIndexed { index, story ->
            if (index == seenStoryIndex) {
                story.copy(isSeen = true)
            } else {
                story
            }
        }
        _uiState.value = _uiState.value.copy(
            stories = stories.toPersistentList(),
        )
        updateActiveStoryIndex()
    }

    private fun updateActiveStoryIndex() {
        val activeStoryIndex = getActiveStoryIndex(_uiState.value.stories)

        _uiState.value = _uiState.value.copy(
            activeStoryIndex = activeStoryIndex,
        )
    }

    override fun onCleared() {
        Timber.d("onCleared ${_uiState.value.storyOwner}")
    }
}
