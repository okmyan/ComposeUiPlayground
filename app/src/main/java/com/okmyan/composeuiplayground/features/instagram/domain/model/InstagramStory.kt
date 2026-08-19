package com.okmyan.composeuiplayground.features.instagram.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class InstagramStory(
    val id: Long,
    val userId: Long,
    val publishedAt: String,
    val isSeen: Boolean,
)
