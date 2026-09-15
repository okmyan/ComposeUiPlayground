package com.okmyan.composeuiplayground.features.instagram.screens.story.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.okmyan.composeuiplayground.features.instagram.screens.story.StoryNotification
import com.okmyan.composeuiplayground.features.instagram.screens.story.StoryNotificationType

@Composable
fun BoxScope.StoryNotificationPopup(
    notification: StoryNotification,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = notification.visible,
        modifier = modifier.align(Alignment.Center),
        enter = fadeIn(animationSpec = tween(500)),
        exit = fadeOut(animationSpec = tween(500)),
    ) {
        Text(
            text = stringResource(notification.type.textId),
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Black.copy(alpha = 0.25f))
                .padding(horizontal = 20.dp, vertical = 13.dp),
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF252424)
@Composable
private fun StoryNotificationPopupPreview() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        StoryNotificationPopup(
            StoryNotification(StoryNotificationType.MESSAGE_SENT, true)
        )
    }
}
