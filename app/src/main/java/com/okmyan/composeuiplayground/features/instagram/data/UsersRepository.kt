package com.okmyan.composeuiplayground.features.instagram.data

import com.okmyan.composeuiplayground.features.instagram.domain.model.InstagramUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UsersRepository {

    private val _users = MutableStateFlow(INITIAL_USERS_STATE)

    fun observeUsers(): Flow<List<InstagramUser>> = _users.asStateFlow()

    // The functions are suspended, since they are assumed to be network calls
    suspend fun getUsersByIds(ids: List<Long>): List<InstagramUser> =
        _users.value.filter { it.id in ids }

    suspend fun getUserById(id: Long): InstagramUser =
        _users.value.first { it.id == id }

    suspend fun getAccountOwnerUser(): InstagramUser =
        _users.value.first { it.isAccountOwner }

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
            // Account owner
            InstagramUser(
                id = 1L,
                username = "john950808",
                isAccountOwner = true,
                avatarPreviewUrl = "https://picsum.photos/500",
                isMuted = false,
            ),

            InstagramUser(
                id = 2L,
                username = "anna2522",
                isAccountOwner = false,
                avatarPreviewUrl = "https://picsum.photos/500",
                isMuted = false,
            ),
            InstagramUser(
                id = 3L,
                username = "alex_stylist",
                isAccountOwner = false,
                avatarPreviewUrl = "https://picsum.photos/500",
                isMuted = true,
            ),
            InstagramUser(
                id = 4L,
                username = "Stephan123",
                isAccountOwner = false,
                avatarPreviewUrl = "https://picsum.photos/500",
                isMuted = false,
            ),
            InstagramUser(
                id = 5L,
                username = "Dev17",
                isAccountOwner = false,
                avatarPreviewUrl = "https://picsum.photos/500",
                isMuted = false,
            ),
            InstagramUser(
                id = 6L,
                username = "shar_228",
                isAccountOwner = false,
                avatarPreviewUrl = "https://picsum.photos/500",
                isMuted = true,
            ),
            InstagramUser(
                id = 7L,
                username = "peppi",
                isAccountOwner = false,
                avatarPreviewUrl = "https://picsum.photos/500",
                isMuted = false,
            ),
            InstagramUser(
                id = 8L,
                username = "vesna",
                isAccountOwner = false,
                avatarPreviewUrl = "https://picsum.photos/500",
                isMuted = false,
            ),
            InstagramUser(
                id = 9L,
                username = "longtimenosee",
                isAccountOwner = false,
                avatarPreviewUrl = "https://picsum.photos/500",
                isMuted = false,
            ),
            InstagramUser(
                id = 10L,
                username = "Stanislau_ll",
                isAccountOwner = false,
                avatarPreviewUrl = "https://picsum.photos/500",
                isMuted = false,
            ),
        )
    }
}
