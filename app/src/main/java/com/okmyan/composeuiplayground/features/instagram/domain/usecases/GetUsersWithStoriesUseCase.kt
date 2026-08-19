package com.okmyan.composeuiplayground.features.instagram.domain.usecases

import com.okmyan.composeuiplayground.features.instagram.data.StoriesRepository
import com.okmyan.composeuiplayground.features.instagram.data.UsersRepository
import com.okmyan.composeuiplayground.features.instagram.domain.model.UserWithStories
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetUsersWithStoriesUseCase(
    private val usersRepository: UsersRepository,
    private val storiesRepository: StoriesRepository,
) {

    operator fun invoke(): Flow<List<UserWithStories>> {
        return storiesRepository.observeStories().map { stories ->
            val storiesByUser = stories.groupBy { it.userId }
            usersRepository.users.map { user ->
                UserWithStories(
                    user = user,
                    stories = storiesByUser[user.id].orEmpty().toImmutableList()
                )
            }
                .filter { it.hasStories }
                .sortedWith(
                    compareByDescending<UserWithStories> { it.user.isCurrentUser }
                        .thenByDescending { it.hasNonSeenStories }
                )
        }
    }

}
