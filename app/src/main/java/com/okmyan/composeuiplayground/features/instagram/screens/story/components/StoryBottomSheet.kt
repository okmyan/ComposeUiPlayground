package com.okmyan.composeuiplayground.features.instagram.screens.story.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.okmyan.composeuiplayground.R
import com.okmyan.composeuiplayground.ui.theme.BottomSheetContentColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryBottomSheet(
    isMuted: Boolean,
    onDismiss: () -> Unit,
    onReport: () -> Unit,
    onMute: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = stringResource(R.string.instagram_story_options_about),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(BottomSheetContentColor)
                    .padding(vertical = 10.dp),
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
    }
}

@Composable
fun StoryBottomSheetOption(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 15.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text
        )

        Text(text = text)
    }
}
