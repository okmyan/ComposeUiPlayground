package com.okmyan.composeuiplayground.features.instagram.domain.usecases

import com.okmyan.composeuiplayground.features.instagram.domain.model.FeedStory
import com.okmyan.composeuiplayground.features.instagram.utils.STORY_COMPARATOR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveSortedFeedStoriesUseCase(
    private val observeFeedStoriesUseCase: ObserveFeedStoriesUseCase,
) {

    operator fun invoke(): Flow<List<FeedStory>> {
        return observeFeedStoriesUseCase().map { stories ->
            stories.sortedWith(STORY_COMPARATOR)
        }
    }

}
