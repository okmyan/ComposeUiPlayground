package com.okmyan.composeuiplayground.features.instagram.domain.usecases

import com.okmyan.composeuiplayground.features.instagram.data.StoriesRepository
import com.okmyan.composeuiplayground.features.instagram.data.UsersRepository
import com.okmyan.composeuiplayground.features.instagram.domain.model.UserWithStories
import kotlinx.collections.immutable.toImmutableList
import timber.log.Timber

class GetSelfStoriesUseCase(
    private val usersRepository: UsersRepository,
    private val storiesRepository: StoriesRepository,
) {

    suspend operator fun invoke(): UserWithStories {
        Timber.d("GetSelfStoriesUseCase")

        val user = usersRepository.getAccountOwnerUser()
        val stories = storiesRepository.getStoriesByOwnerIds(user.id)

        val allStoriesSeen = stories.all { it.isSeen }

        return UserWithStories(
            user = user,
            allStoriesSeen = allStoriesSeen,
            stories = stories.toImmutableList()
        )
    }

}
