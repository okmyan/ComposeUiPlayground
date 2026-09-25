package com.okmyan.composeuiplayground.features.instagram.utils

import com.okmyan.composeuiplayground.features.instagram.domain.model.FeedStory
import com.okmyan.composeuiplayground.features.instagram.domain.model.InstagramStory

internal fun getActiveStoryIndex(stories: List<InstagramStory>): Int {
    stories.forEachIndexed { index, story ->
        if (!story.isSeen) {
            return index
        }
    }

    return 0
}

val STORY_COMPARATOR =
    compareByDescending<FeedStory> { it.storyOwner.isAccountOwner }
        .thenByDescending { it.hasNonSeenStories }
        .thenBy { it.isMuted }
