package com.okmyan.composeuiplayground.features.instagram.screens.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okmyan.composeuiplayground.features.instagram.domain.model.UserWithStories
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.UpdateStoriesUseCase
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class InstagramStoryViewModel(
    private val userWithStories: UserWithStories,
    private val updateStoriesUseCase: UpdateStoriesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(InstagramStoryState())
    val uiState = _uiState.asStateFlow()

    init {
        Timber.d("Init block ${userWithStories.user}")

        _uiState.value = _uiState.value.copy(
            user = userWithStories.user,
            stories = userWithStories.stories,
        )

        updateActiveStoryIndex()
    }

    fun onStorySeen(storyId: Long) {
        viewModelScope.launch(CoroutineName("InstagramStoryViewModel - onStorySeen (${userWithStories.user} $storyId)")) {
            updateStoriesUseCase(storyId)
        }
    }

    fun onStoryEnded(seenStoryIndex: Int) {
        val stories = uiState.value.stories.mapIndexed { index, story ->
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
        var activeStoryIndex = 0

        run breaking@{
            _uiState.value.stories.forEachIndexed { index, story ->
                if (!story.isSeen) {
                    activeStoryIndex = index
                    return@breaking
                }
            }
        }

        _uiState.value = _uiState.value.copy(
            activeStoryIndex = activeStoryIndex,
        )
    }

    override fun onCleared() {
        Timber.d("onCleared ${uiState.value.user}")
    }
}
