package com.okmyan.composeuiplayground.features.instagram.screens.story.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.outlined.Report
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.okmyan.composeuiplayground.R
import com.okmyan.composeuiplayground.features.instagram.screens.components.StoryBaseBottomSheet
import com.okmyan.composeuiplayground.features.instagram.screens.components.StoryBottomSheetOption

@Composable
fun StoryBottomSheet(
    isMuted: Boolean,
    onDismiss: () -> Unit,
    onReport: () -> Unit,
    onMute: () -> Unit,
) {
    StoryBaseBottomSheet(
        onDismiss = onDismiss,
    ) {
        StoryBottomSheetOption(
            icon = Icons.Outlined.Report,
            text = stringResource(R.string.instagram_story_options_report),
            onClick = onReport,
        )

        val muteIcon =
            if (isMuted) Icons.Outlined.Notifications else Icons.Outlined.NotificationsOff
        val muteTextId =
            if (isMuted) R.string.instagram_story_options_unmute else R.string.instagram_story_options_mute

        StoryBottomSheetOption(
            icon = muteIcon,
            text = stringResource(muteTextId),
            onClick = onMute,
        )
    }
}

@Preview
@Composable
private fun StoryBottomSheetPreview() {
    StoryBottomSheet(
        isMuted = false,
        onDismiss = {},
        onReport = {},
        onMute = {},
    )
}
