package com.okmyan.composeuiplayground.features.instagram.home

import androidx.lifecycle.ViewModel
import com.okmyan.composeuiplayground.features.instagram.InstagramRepository
import com.okmyan.composeuiplayground.utils.extensions.findNext
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import java.util.LinkedList

class InstagramHomeViewModel(
    repository: InstagramRepository,
) : ViewModel() {

    private var _uiState = MutableStateFlow(InstagramHomeState())
    val uiState = _uiState.asStateFlow()

    val usersWithHighlights: LinkedList<UserWithStories>

    init {
        Timber.d("Init block")
        val userWithStories = repository.usersWithStories

        usersWithHighlights = LinkedList(
            userWithStories.filter { it.stories.isNotEmpty() }
        )

        _uiState.value = _uiState.value.copy(
            usersWithStories = userWithStories.toImmutableList()
        )
    }

    fun getPreviousUserWithStories(userWithStories: UserWithStories): UserWithStories? {
        val iterator = usersWithHighlights.reversed().iterator()
        return iterator.findNext(userWithStories)
    }

    fun getNextUserWithStories(userWithStories: UserWithStories): UserWithStories? {
        val iterator = usersWithHighlights.iterator()
        return iterator.findNext(userWithStories)
    }

    fun isItFirstUserInHighlights(userWithStories: UserWithStories): Boolean =
        usersWithHighlights.getOrNull(0) != userWithStories


    override fun onCleared() {
        Timber.d("onCleared")
    }
}
