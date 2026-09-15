package com.okmyan.composeuiplayground.features.instagram.domain.usecases

import com.okmyan.composeuiplayground.features.instagram.data.StoriesRepository
import com.okmyan.composeuiplayground.features.instagram.data.UsersRepository
import com.okmyan.composeuiplayground.features.instagram.domain.model.UserWithStories
import kotlinx.collections.immutable.toImmutableList
import timber.log.Timber

class GetStoriesByOwnerUseCase(
    private val usersRepository: UsersRepository,
    private val storiesRepository: StoriesRepository,
) {

    suspend operator fun invoke(storyOwnerId: Long): UserWithStories {
        Timber.d("storyOwnerId: $storyOwnerId")

        val user = usersRepository.getUserById(storyOwnerId)
        val stories = storiesRepository.getStoriesByOwnerIds(storyOwnerId)

        val allStoriesSeen = stories.all { it.isSeen }

        return UserWithStories(
            user = user,
            allStoriesSeen = allStoriesSeen,
            stories = stories.toImmutableList()
        )
    }

}
