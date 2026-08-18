package com.okmyan.composeuiplayground.features.instagram

import androidx.lifecycle.ViewModel
import com.okmyan.composeuiplayground.utils.extensions.findNext
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import java.util.LinkedList

class InstagramViewModel(
    repository: InstagramRepository,
) : ViewModel() {

    private var _uiState = MutableStateFlow(InstagramState())
    val uiState = _uiState.asStateFlow()

    val storyWithContentIds: LinkedList<Long>

    init {
        Timber.d("Init block")
        val stories = repository.stories

        storyWithContentIds = LinkedList(
            stories.filter { it.hasStory }.map { it.id }
        )

        _uiState.value = _uiState.value.copy(
            stories = stories.toImmutableList()
        )
    }

    fun getPreviousStoryId(storyId: Long): Long? {
        val iterator = storyWithContentIds.reversed().iterator()
        return iterator.findNext(storyId)
    }

    fun getNextStoryId(storyId: Long): Long? {
        val iterator = storyWithContentIds.iterator()
        return iterator.findNext(storyId)
    }

    fun hasPreviousStory(storyId: Long): Boolean =
        storyWithContentIds.getOrNull(0) != storyId


    override fun onCleared() {
        Timber.d("clear")
    }
}
