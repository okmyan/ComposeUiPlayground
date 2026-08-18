package com.okmyan.composeuiplayground.features.instagram.story

import androidx.lifecycle.ViewModel
import com.okmyan.composeuiplayground.features.instagram.home.UserWithStories
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

class InstagramStoryViewModel(
    userWithStories: UserWithStories,
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

    fun onStorySeen(seenStoryIndex: Int) {
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

        run breaking@ {
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
