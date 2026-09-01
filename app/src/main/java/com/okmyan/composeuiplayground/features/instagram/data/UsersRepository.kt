package com.okmyan.composeuiplayground.features.instagram.data

import com.okmyan.composeuiplayground.features.instagram.domain.model.InstagramUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UsersRepository {

    private val _users = MutableStateFlow(INITIAL_USERS_STATE)

    fun observeUsers(): Flow<List<InstagramUser>> = _users.asStateFlow()

    // It is suspended, since it is assumed to be a network call
    suspend fun muteUser(id: Long) {
        _users.value = _users.value.map { user ->
            user.copy(
                isMuted = if (user.id == id) {
                    !user.isMuted
                } else {
                    user.isMuted
                }
            )
        }
    }

    companion object {
        val INITIAL_USERS_STATE = listOf(

            InstagramUser(
                id = 1L,
                username = "john950808",
                isCurrentUser = true,
                avatarPreviewUrl = "https://picsum.photos/500",
                isMuted = false,
            ),
            InstagramUser(
                id = 2L,
                username = "anna2522",
                isCurrentUser = false,
                avatarPreviewUrl = "https://picsum.photos/500",
                isMuted = false,
            ),
            InstagramUser(
                id = 3L,
                username = "alex_stylist",
                isCurrentUser = false,
                avatarPreviewUrl = "https://picsum.photos/500",
                isMuted = true,
            ),
            InstagramUser(
                id = 4L,
                username = "Stephan123",
                isCurrentUser = false,
                avatarPreviewUrl = "https://picsum.photos/500",
                isMuted = false,
            ),
            InstagramUser(
                id = 5L,
                username = "Dev17",
                isCurrentUser = false,
                avatarPreviewUrl = "https://picsum.photos/500",
                isMuted = false,
            ),
            InstagramUser(
                id = 6L,
                username = "shar_228",
                isCurrentUser = false,
                avatarPreviewUrl = "https://picsum.photos/500",
                isMuted = true,
            ),
            InstagramUser(
                id = 7L,
                username = "peppi",
                isCurrentUser = false,
                avatarPreviewUrl = "https://picsum.photos/500",
                isMuted = false,
            ),
            InstagramUser(
                id = 8L,
                username = "vesna",
                isCurrentUser = false,
                avatarPreviewUrl = "https://picsum.photos/500",
                isMuted = false,
            ),
            InstagramUser(
                id = 9L,
                username = "longtimenosee",
                isCurrentUser = false,
                avatarPreviewUrl = "https://picsum.photos/500",
                isMuted = false,
            ),
            InstagramUser(
                id = 10L,
                username = "Stanislau_ll",
                isCurrentUser = false,
                avatarPreviewUrl = "https://picsum.photos/500",
                isMuted = false,
            ),
        )
    }
}
