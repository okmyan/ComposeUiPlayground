package com.okmyan.composeuiplayground.features.instagram.domain.model

data class InstagramStory(
    val id: Long,
    val userId: Long,
    val pictureUrl: String,
    val publishedAt: String,
    val isSeen: Boolean,
    val isLiked: Boolean,
    val isForClosedFriendsOnly: Boolean,
)
