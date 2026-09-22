package com.okmyan.composeuiplayground.features.instagram.domain.usecases

import com.okmyan.composeuiplayground.features.instagram.data.StoriesRepository
import com.okmyan.composeuiplayground.features.instagram.data.UsersRepository
import com.okmyan.composeuiplayground.features.instagram.domain.model.UserWithStories
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GetUsersWithStoriesUseCase(
    private val usersRepository: UsersRepository,
    private val storiesRepository: StoriesRepository,
) {

    suspend operator fun invoke(ownerIds: List<Long>): List<UserWithStories> = coroutineScope {
        val usersDeferred = async { usersRepository.getUsersByIds(ownerIds) }
        val storiesDeferred = async { storiesRepository.getStoriesByOwnerIds(ownerIds) }

        val users = usersDeferred.await()
        val stories = storiesDeferred.await()

        val storiesByUser = stories.groupBy { it.userId }

        return@coroutineScope users.mapNotNull { user ->
            val userStories = storiesByUser[user.id] ?: return@mapNotNull null
            UserWithStories(
                user = user,
                allStoriesSeen = false, // The actual value doesn't matter here
                stories = userStories.toImmutableList()
            )
        }
    }

}
