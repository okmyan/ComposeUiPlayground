package com.okmyan.composeuiplayground.features.instagram.screens.story

import com.okmyan.composeuiplayground.R

data class StoryNotification(
    val type: StoryNotificationType = StoryNotificationType.MESSAGE_SENT,
    val visible: Boolean = false,
)

enum class StoryNotificationType(val textId: Int) {
    MESSAGE_SENT(R.string.instagram_story_notification_message_sent),
    MUTED(R.string.instagram_story_notification_muted),
    UNMUTED(R.string.instagram_story_notification_unmuted),
    REPORTED(R.string.instagram_story_notification_reported),
}
