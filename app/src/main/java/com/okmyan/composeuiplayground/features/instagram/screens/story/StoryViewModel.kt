package com.okmyan.composeuiplayground.features.instagram.screens.story

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.GetStoriesByOwnerUseCase
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.LikeStoriesUseCase
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.MuteUserUseCase
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.SeeStoriesUseCase
import com.okmyan.composeuiplayground.features.instagram.utils.getActiveStoryIndex
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.time.Duration.Companion.seconds

class StoryViewModel(
    private val selectedStoryOwnerId: Long,
    private val getStoriesByOwnerUseCase: GetStoriesByOwnerUseCase,
    private val seeStoriesUseCase: SeeStoriesUseCase,
    private val likeStoriesUseCase: LikeStoriesUseCase,
    private val muteUserUseCase: MuteUserUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(StoryState())
    val uiState = _uiState.asStateFlow()

    private var notificationsJob: Job? = null
    private val _notifications = MutableSharedFlow<StoryNotification>()
    val notifications = _notifications.asSharedFlow()

    init {
        Timber.d("Init block $selectedStoryOwnerId")

        viewModelScope.launch(CoroutineName("StoryViewModel - getStoriesByOwner $selectedStoryOwnerId")) {
            getStoriesByOwner()
        }
    }

    private suspend fun getStoriesByOwner() {
        val userWithStories = getStoriesByOwnerUseCase(selectedStoryOwnerId)

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
        viewModelScope.launch(CoroutineName("InstagramStoryViewModel - onStorySeen (${selectedStoryOwnerId} $storyId)")) {
            seeStoriesUseCase(storyId)
        }
    }

    fun onStoryLiked(storyId: Long) {
        viewModelScope.launch(CoroutineName("InstagramStoryViewModel - onStoryLiked (${selectedStoryOwnerId} $storyId)")) {
            likeStoriesUseCase(storyId)
        }

        _uiState.value = _uiState.value.copy(
            stories = _uiState.value.stories.map { story ->
                story.copy(
                    isLiked = if (story.id == _uiState.value.activeStory.id) {
                        !story.isLiked
                    } else {
                        story.isLiked
                    }
                )
            }.toImmutableList()
        )
    }

    fun onMessageChange(storyId: Long, message: TextFieldValue) {
        _uiState.value = _uiState.value.copy(
            enteredMessage = _uiState.value.enteredMessage.toPersistentMap()
                .putting(storyId, message)
        )
    }

    fun onMessageSend(storyId: Long) {
        Timber.d("The user sent the following message:\n${_uiState.value.enteredMessage}")

        _uiState.value = _uiState.value.copy(
            enteredMessage = _uiState.value.enteredMessage.toPersistentMap().removing(storyId)
        )
        sendNotification(StoryNotificationType.MESSAGE_SENT)
    }

    fun onMute() {
        viewModelScope.launch(CoroutineName("InstagramStoryViewModel - onMute (${selectedStoryOwnerId}")) {
            muteUserUseCase(selectedStoryOwnerId)
        }

        val isMuted = _uiState.value.storyOwner.isMuted
        _uiState.value = _uiState.value.copy(
            storyOwner = _uiState.value.storyOwner.copy(
                isMuted = !isMuted
            )
        )

        if (isMuted) {
            sendNotification(StoryNotificationType.UNMUTED)
        } else {
            sendNotification(StoryNotificationType.MUTED)
        }
    }

    fun onReport() {
        sendNotification(StoryNotificationType.REPORTED)
    }

    private fun sendNotification(notificationType: StoryNotificationType) {
        notificationsJob?.cancel()
        notificationsJob =
            viewModelScope.launch(CoroutineName("InstagramStoryViewModel - sendNotification $notificationType")) {
                delay(1.seconds)
                _notifications.emit(StoryNotification(notificationType, true))
                delay(3.seconds)
                _notifications.emit(StoryNotification(notificationType, false))
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
