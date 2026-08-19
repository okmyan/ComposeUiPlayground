package com.okmyan.composeuiplayground.features.instagram.data

import com.okmyan.composeuiplayground.features.instagram.domain.model.InstagramStory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StoriesRepository {

    private val _stories = MutableStateFlow(INITIAL_STORIES_STATE)

    fun observeStories(): Flow<List<InstagramStory>> = _stories.asStateFlow()

    suspend fun seenStory(id: Long) {
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

    companion object {
        val INITIAL_STORIES_STATE = listOf(
            // john950808
            InstagramStory(
                id = 1L,
                userId = 1L,
                publishedAt = "1h",
                isSeen = true,
            ),

            // anna2020
            InstagramStory(
                id = 2L,
                userId = 2L,
                publishedAt = "1h",
                isSeen = false,
            ),

            // alex_stylist
            InstagramStory(
                id = 3L,
                userId = 3L,
                publishedAt = "2h",
                isSeen = false,
            ),
            InstagramStory(
                id = 4L,
                userId = 3L,
                publishedAt = "2,5h",
                isSeen = false,
            ),

            // Stephan123
            InstagramStory(
                id = 5L,
                userId = 4L,
                publishedAt = "40m",
                isSeen = true,
            ),
            InstagramStory(
                id = 6L,
                userId = 4L,
                publishedAt = "45m",
                isSeen = false,
            ),
            InstagramStory(
                id = 7L,
                userId = 4L,
                publishedAt = "50m",
                isSeen = false,
            ),

            // Dev17
            InstagramStory(
                id = 8L,
                userId = 5L,
                publishedAt = "20h",
                isSeen = true,
            ),

            // shar_228
            InstagramStory(
                id = 9L,
                userId = 6L,
                publishedAt = "2h",
                isSeen = true,
            ),

            // peppi
            InstagramStory(
                id = 10L,
                userId = 7L,
                publishedAt = "3h",
                isSeen = true,
            ),
            InstagramStory(
                id = 11L,
                userId = 7L,
                publishedAt = "3h",
                isSeen = true,
            ),

            // vesna
            InstagramStory(
                id = 12L,
                userId = 8L,
                publishedAt = "10m",
                isSeen = false,
            ),

            // longtimenosee
            InstagramStory(
                id = 13L,
                userId = 9L,
                publishedAt = "1h",
                isSeen = false,
            ),

            // Stanislau_ll
            InstagramStory(
                id = 14L,
                userId = 10L,
                publishedAt = "1h",
                isSeen = true,
            ),
            InstagramStory(
                id = 15L,
                userId = 10L,
                publishedAt = "10h",
                isSeen = false,
            ),
        )
    }

}
