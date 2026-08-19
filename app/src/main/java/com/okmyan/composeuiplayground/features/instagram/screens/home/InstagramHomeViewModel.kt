package com.okmyan.composeuiplayground.features.instagram.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okmyan.composeuiplayground.features.instagram.domain.model.UserWithStories
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.GetUsersWithStoriesUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class InstagramHomeViewModel(
    private val getUsersWithStoriesUseCase: GetUsersWithStoriesUseCase,
) : ViewModel() {

    private var _uiState = MutableStateFlow(InstagramHomeState())
    val uiState = _uiState.asStateFlow()

    init {
        Timber.d("Init block")
        viewModelScope.launch(CoroutineName("InstagramHomeViewModel - getUsersWithStories")) {
            getUsersWithStories()
        }
    }

    private suspend fun getUsersWithStories() {
        getUsersWithStoriesUseCase().collect { usersWithStories ->
            _uiState.value = _uiState.value.copy(
                usersWithStories = usersWithStories.toImmutableList()
            )
        }
    }

    fun isItFirstUserInHighlights(userWithStories: UserWithStories): Boolean =
        _uiState.value.usersWithStories.getOrNull(0)?.user?.id != userWithStories.user.id


    override fun onCleared() {
        Timber.d("onCleared")
    }
}
