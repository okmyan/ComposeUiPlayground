package com.okmyan.composeuiplayground.features.instagram.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class InstagramUser(
    val id: Long = 0L,
    val username: String = "",
    val isAccountOwner: Boolean = false,
    val avatarPreviewUrl: String = "",
    val isMuted: Boolean = false,
)
