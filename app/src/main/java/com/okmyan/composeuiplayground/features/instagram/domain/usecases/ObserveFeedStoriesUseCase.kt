package com.okmyan.composeuiplayground.features.instagram.domain.usecases

import com.okmyan.composeuiplayground.features.instagram.data.StoriesRepository
import com.okmyan.composeuiplayground.features.instagram.data.UsersRepository
import com.okmyan.composeuiplayground.features.instagram.domain.model.FeedStory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn

class ObserveFeedStoriesUseCase(
    private val usersRepository: UsersRepository,
    private val storiesRepository: StoriesRepository,
) {

    operator fun invoke(): Flow<List<FeedStory>> {
        return usersRepository.observeUsers()
            .combine(storiesRepository.observeStories()) { users, stories ->
                val storiesByUser = stories.groupBy { it.userId }

                users.mapNotNull { user ->
                    val userStories = storiesByUser[user.id] ?: return@mapNotNull null

                    val hasNonSeenStories = userStories.any { !it.isSeen }

                    FeedStory(
                        storyOwner = user,
                        hasNonSeenStories = hasNonSeenStories,
                    )
                }
            }
            .flowOn(Dispatchers.Default)
    }

}
