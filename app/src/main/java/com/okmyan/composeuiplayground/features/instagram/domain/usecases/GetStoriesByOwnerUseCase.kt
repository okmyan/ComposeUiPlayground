package com.okmyan.composeuiplayground.features.instagram.domain.usecases

import com.okmyan.composeuiplayground.features.instagram.data.StoriesRepository
import com.okmyan.composeuiplayground.features.instagram.data.UsersRepository
import com.okmyan.composeuiplayground.features.instagram.domain.model.UserWithStories
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import timber.log.Timber

class GetStoriesByOwnerUseCase(
    private val usersRepository: UsersRepository,
    private val storiesRepository: StoriesRepository,
) {

    suspend operator fun invoke(storyOwnerId: Long): UserWithStories = coroutineScope {
        Timber.d("storyOwnerId: $storyOwnerId")

        val user = async { usersRepository.getUserById(storyOwnerId) }
        val storiesDeferred = async { storiesRepository.getStoriesByOwnerIds(storyOwnerId) }

        val stories = storiesDeferred.await()
        val allStoriesSeen = stories.all { it.isSeen }

        return@coroutineScope UserWithStories(
            user = user.await(),
            allStoriesSeen = allStoriesSeen,
            stories = stories.toImmutableList()
        )
    }

}
