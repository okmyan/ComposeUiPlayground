package com.okmyan.composeuiplayground.features.instagram.domain.usecases

import com.okmyan.composeuiplayground.features.instagram.data.StoriesRepository
import com.okmyan.composeuiplayground.features.instagram.data.UsersRepository
import com.okmyan.composeuiplayground.features.instagram.domain.model.UserWithStories
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn

class GetUsersWithStoriesUseCase(
    private val usersRepository: UsersRepository,
    private val storiesRepository: StoriesRepository,
) {

    operator fun invoke(): Flow<List<UserWithStories>> {
        return usersRepository.observeUsers()
            .combine(storiesRepository.observeStories()) { users, stories ->
                val storiesByUser = stories.groupBy { it.userId }

                users.mapNotNull { user ->
                    val userStories = storiesByUser[user.id] ?: return@mapNotNull null
                    UserWithStories(
                        user = user,
                        stories = userStories.toImmutableList()
                    )
                }
                    .sortedWith(STORY_COMPARATOR)
            }
            .flowOn(Dispatchers.Default)
    }

    companion object {
        private val STORY_COMPARATOR =
            compareByDescending<UserWithStories> { it.user.isCurrentUser }
                .thenByDescending { it.hasNonSeenStories }
                .thenBy { it.isMuted }
    }

}
