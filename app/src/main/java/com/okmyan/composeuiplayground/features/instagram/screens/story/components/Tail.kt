package com.okmyan.composeuiplayground.features.instagram.screens.story.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.okmyan.composeuiplayground.R

@Composable
fun Tail(
    modifier: Modifier = Modifier,
    onMessageEditing: () -> Unit,
    message: String,
    isLiked: Boolean,
    onLike: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .height(44.dp)
                .clip(CircleShape)
                .border(width = 0.4.dp, color = Color.White, shape = CircleShape)
                .clickable(onClick = onMessageEditing)
                .padding(horizontal = 18.dp)
                .weight(1f),
            contentAlignment = Alignment.CenterStart,
        ) {
            val textToShow = message.ifEmpty {
                stringResource(R.string.instagram_story_send_message)
            }
            Text(
                text = textToShow,
                color = Color.White,
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }

        val haptics = LocalHapticFeedback.current

        val favoriteIcon = if (isLiked) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder
        val favoriteIconContentDescriptionId =
            if (isLiked) R.string.instagram_story_favorite_unlike else R.string.instagram_story_favorite_like
        val favoriteIconColor = if (isLiked) Color.Red else Color.White

        Icon(
            imageVector = favoriteIcon,
            contentDescription = stringResource(favoriteIconContentDescriptionId),
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .clickable(onClick = {
                    if (!isLiked) {
                        haptics.performHapticFeedback(HapticFeedbackType.Confirm)
                    }
                    onLike()
                }),
            tint = favoriteIconColor
        )

        Icon(
            imageVector = Icons.AutoMirrored.Rounded.Send,
            contentDescription = stringResource(R.string.instagram_story_send_story),
            modifier = Modifier
                .size(27.dp)
                .offset(y = (-3).dp)
                .rotate(-23f),
            tint = Color.White
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF252424)
@Composable
private fun TailPreview() {
    var isLiked by remember { mutableStateOf(false) }
    Tail(
        onMessageEditing = {},
        message = "Hi there!",
        isLiked = isLiked,
        onLike = { isLiked = !isLiked }
    )
}
