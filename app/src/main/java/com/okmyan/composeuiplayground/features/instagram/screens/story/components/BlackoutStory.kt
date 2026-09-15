package com.okmyan.composeuiplayground.features.instagram.screens.story.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun BlackoutStory(
    blackoutAlpha: Float,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val clickableModifier = Modifier.clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = blackoutAlpha))
            .then(if (blackoutAlpha > 0f) clickableModifier else Modifier),
        contentAlignment = Alignment.Center,
    ) {}
}
