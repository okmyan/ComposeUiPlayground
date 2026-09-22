package com.okmyan.composeuiplayground.features.instagram.data

import com.okmyan.composeuiplayground.features.instagram.domain.model.InstagramStory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StoriesRepository {

    private val _stories = MutableStateFlow(INITIAL_STORIES_STATE)

    fun observeStories(): Flow<List<InstagramStory>> = _stories.asStateFlow()

    // The functions are suspended, since they are assumed to be network calls
    suspend fun getStoriesByOwnerIds(userIds: List<Long>): List<InstagramStory> =
        _stories.value.filter { it.userId in userIds }

    suspend fun getStoriesByOwnerId(storyOwnerId: Long): List<InstagramStory> =
        _stories.value.filter { storyOwnerId == it.userId }

    suspend fun seeStory(id: Long) {
        _stories.value = _stories.value.map { story ->
            story.copy(
                isSeen = if (story.id == id) {
                    true
                } else {
                    story.isSeen
                }
            )
        }
    }

    suspend fun likeStory(id: Long) {
        _stories.value = _stories.value.map { story ->
            story.copy(
                isLiked = if (story.id == id) {
                    !story.isLiked
                } else {
                    story.isLiked
                }
            )
        }
    }

    companion object {
        val INITIAL_STORIES_STATE = listOf(
            // john950808
            InstagramStory(
                id = 1L,
                userId = 1L,
                pictureUrl = "https://picsum.photos/720/1280",
                publishedAt = "1h",
                isSeen = true,
                isLiked = false,
            ),
            InstagramStory(
                id = 1111L,
                userId = 1L,
                pictureUrl = "https://picsum.photos/720/1280",
                publishedAt = "1h",
                isSeen = false,
                isLiked = false,
            ),
            InstagramStory(
                id = 1112L,
                userId = 1L,
                pictureUrl = "https://picsum.photos/720/1280",
                publishedAt = "1h",
                isSeen = false,
                isLiked = false,
            ),

            // anna2522
            InstagramStory(
                id = 2L,
                userId = 2L,
                pictureUrl = "https://picsum.photos/720/1280",
                publishedAt = "1h",
                isSeen = true,
                isLiked = false,
            ),
            InstagramStory(
                id = 102L,
                userId = 2L,
                pictureUrl = "https://picsum.photos/720/1280",
                publishedAt = "1h",
                isSeen = false,
                isLiked = false,
            ),
            InstagramStory(
                id = 103L,
                userId = 2L,
                pictureUrl = "https://picsum.photos/720/1280",
                publishedAt = "1h",
                isSeen = false,
                isLiked = false,
            ),

            // alex_stylist
            InstagramStory(
                id = 3L,
                userId = 3L,
                pictureUrl = "https://picsum.photos/720/1280",
                publishedAt = "2h",
                isSeen = false,
                isLiked = false,
            ),
            InstagramStory(
                id = 4L,
                userId = 3L,
                pictureUrl = "https://picsum.photos/720/1280",
                publishedAt = "2,5h",
                isSeen = false,
                isLiked = false,
            ),

            // Stephan123
            InstagramStory(
                id = 5L,
                userId = 4L,
                pictureUrl = "https://picsum.photos/720/1280",
                publishedAt = "40m",
                isSeen = true,
                isLiked = true,
            ),
            InstagramStory(
                id = 6L,
                userId = 4L,
                pictureUrl = "https://picsum.photos/720/1280",
                publishedAt = "45m",
                isSeen = true,
                isLiked = false,
            ),
            InstagramStory(
                id = 7L,
                userId = 4L,
                pictureUrl = "https://picsum.photos/720/1280",
                publishedAt = "50m",
                isSeen = false,
                isLiked = false,
            ),

            // Dev17
            InstagramStory(
                id = 8L,
                userId = 5L,
                pictureUrl = "https://picsum.photos/720/1280",
                publishedAt = "20h",
                isSeen = true,
                isLiked = true,
            ),

            // shar_228
            InstagramStory(
                id = 9L,
                userId = 6L,
                pictureUrl = "https://picsum.photos/720/1280",
                publishedAt = "2h",
                isSeen = true,
                isLiked = false,
            ),

            // peppi
            InstagramStory(
                id = 10L,
                userId = 7L,
                pictureUrl = "https://picsum.photos/720/1280",
                publishedAt = "3h",
                isSeen = true,
                isLiked = true,
            ),
            InstagramStory(
                id = 11L,
                userId = 7L,
                pictureUrl = "https://picsum.photos/720/1280",
                publishedAt = "3h",
                isSeen = true,
                isLiked = false,
            ),

            // vesna
            InstagramStory(
                id = 12L,
                userId = 8L,
                pictureUrl = "https://picsum.photos/720/1280",
                publishedAt = "10m",
                isSeen = false,
                isLiked = false,
            ),

            // vesna
            InstagramStory(
                id = 104L,
                userId = 8L,
                pictureUrl = "https://picsum.photos/720/1280",
                publishedAt = "10m",
                isSeen = false,
                isLiked = false,
            ),

            // longtimenosee
            InstagramStory(
                id = 13L,
                userId = 9L,
                pictureUrl = "https://picsum.photos/720/1280",
                publishedAt = "1h",
                isSeen = false,
                isLiked = false,
            ),

            // Stanislau_ll
            InstagramStory(
                id = 14L,
                userId = 10L,
                pictureUrl = "https://picsum.photos/720/1280",
                publishedAt = "1h",
                isSeen = true,
                isLiked = false,
            ),
            InstagramStory(
                id = 15L,
                userId = 10L,
                pictureUrl = "https://picsum.photos/720/1280",
                publishedAt = "10h",
                isSeen = false,
                isLiked = false,
            ),
        )
    }

}
