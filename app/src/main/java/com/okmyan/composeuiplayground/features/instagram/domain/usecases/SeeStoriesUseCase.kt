package com.okmyan.composeuiplayground.features.instagram.domain.usecases

import com.okmyan.composeuiplayground.features.instagram.data.StoriesRepository

class SeeStoriesUseCase(
    private val storiesRepository: StoriesRepository,
) {

    suspend operator fun invoke(storyId: Long) {
        storiesRepository.seeStory(storyId)
    }

}
