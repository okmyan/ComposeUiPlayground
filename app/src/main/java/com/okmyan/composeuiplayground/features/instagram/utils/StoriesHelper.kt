package com.okmyan.composeuiplayground.features.instagram.utils

import com.okmyan.composeuiplayground.features.instagram.domain.model.InstagramStory

internal fun getActiveStoryIndex(stories: List<InstagramStory>): Int {
    stories.forEachIndexed { index, story ->
        if (!story.isSeen) {
            return index
        }
    }

    return 0
}
