package com.okmyan.composeuiplayground.features.instagram.screens.story

import com.okmyan.composeuiplayground.features.instagram.domain.model.InstagramStory
import com.okmyan.composeuiplayground.features.instagram.domain.model.InstagramUser
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.GetStoriesByOwnerUseCase
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.SeeStoriesUseCase
import com.okmyan.composeuiplayground.features.instagram.utils.getActiveStoryIndex
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList

class StoryNavigationDelegate(
    private val getStoriesByOwnerUseCase: GetStoriesByOwnerUseCase,
    private val seeStoriesUseCase: SeeStoriesUseCase,
) {

    suspend fun loadStories(ownerId: Long): Triple<InstagramUser, ImmutableList<InstagramStory>, Int> {
        val userWithStories = getStoriesByOwnerUseCase(ownerId)

        // If the user opens the stories that already watched, we show them again
        val stories = if (userWithStories.allStoriesSeen) {
            userWithStories.stories.map {
                it.copy(isSeen = false)
            }
        } else {
            userWithStories.stories
        }.toImmutableList()

        return Triple(
            userWithStories.user,
            stories,
            getActiveStoryIndex(stories)
        )
    }

    suspend fun onStorySeen(storyId: Long) = seeStoriesUseCase(storyId)

    fun handlePrevStory(
        stories: ImmutableList<InstagramStory>,
        activeStoryIndex: Int,
        isActiveStoryFirstOne: Boolean
    ): Pair<Int, ImmutableList<InstagramStory>> {
        val newActiveStoryIndex = if (isActiveStoryFirstOne) {
            0
        } else {
            activeStoryIndex - 1
        }

        val updatedStories = stories.mapIndexed { index, story ->
            if (index == activeStoryIndex || index == newActiveStoryIndex) {
                story.copy(isSeen = false)
            } else {
                story
            }
        }

        return Pair(newActiveStoryIndex, updatedStories.toPersistentList())
    }

    fun handleNextStory(
        stories: ImmutableList<InstagramStory>,
        activeStoryIndex: Int,
    ): Pair<Int, ImmutableList<InstagramStory>> {
        val updatedStories = stories.mapIndexed { index, story ->
            if (index == activeStoryIndex) {
                story.copy(isSeen = true)
            } else {
                story
            }
        }

        return Pair(activeStoryIndex + 1, updatedStories.toPersistentList())
    }
}
