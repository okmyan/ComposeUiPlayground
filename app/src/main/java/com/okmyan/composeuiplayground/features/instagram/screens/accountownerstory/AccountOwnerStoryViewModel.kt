package com.okmyan.composeuiplayground.features.instagram.screens.accountownerstory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.GetStoriesByOwnerUseCase
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.SeeStoriesUseCase
import com.okmyan.composeuiplayground.features.instagram.screens.story.StoryNavigationDelegate
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class AccountOwnerStoryViewModel(
    accountOwnerId: Long,
    getStoriesByOwnerUseCase: GetStoriesByOwnerUseCase,
    seeStoriesUseCase: SeeStoriesUseCase,
) : ViewModel() {

    private val storyNavigationDelegate =
        StoryNavigationDelegate(getStoriesByOwnerUseCase, seeStoriesUseCase)

    private val _uiState = MutableStateFlow(AccountOwnerStoryState())
    val uiState = _uiState.asStateFlow()

    init {
        Timber.d("Init block $accountOwnerId")

        viewModelScope.launch(CoroutineName("AccountOwnerStoryViewModel - getSelfStories")) {
            getSelfStories(accountOwnerId)
        }
    }

    private suspend fun getSelfStories(accountOwnerId: Long) {
        val (user, stories, activeIndex) = storyNavigationDelegate.loadStories(accountOwnerId)

        _uiState.value = _uiState.value.copy(
            storyOwner = user,
            stories = stories,
            activeStoryIndex = activeIndex
        )
    }

    fun onStorySeen(storyId: Long) {
        viewModelScope.launch(CoroutineName("AccountOwnerStoryViewModel - onStorySeen ($storyId)")) {
            storyNavigationDelegate.onStorySeen(storyId)
        }
    }

    fun onGoToPrevStory() {
        Timber.d("Go to prev story")
        val (newIndex, updatedStories) = storyNavigationDelegate.handlePrevStory(
            stories = _uiState.value.stories,
            activeStoryIndex = _uiState.value.activeStoryIndex,
            isActiveStoryFirstOne = _uiState.value.isActiveStoryFirstOne
        )

        _uiState.value = _uiState.value.copy(
            activeStoryIndex = newIndex,
            stories = updatedStories.toPersistentList(),
        )
    }

    fun onGoToNextStory(): Boolean {
        if (_uiState.value.isActiveStoryLastOne) {
            return false
        }
        Timber.d("Go to next story")

        val result = storyNavigationDelegate.handleNextStory(
            stories = _uiState.value.stories,
            activeStoryIndex = _uiState.value.activeStoryIndex,
        )

        _uiState.value = _uiState.value.copy(
            activeStoryIndex = result.first,
            stories = result.second.toPersistentList(),
        )
        return true
    }

    override fun onCleared() {
        Timber.d("onCleared ${_uiState.value.storyOwner}")
    }
}
