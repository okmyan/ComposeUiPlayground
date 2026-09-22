package com.okmyan.composeuiplayground.features.instagram.screens.story.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.okmyan.composeuiplayground.utils.extensions.noRippleClickable

@Composable
fun BlackoutStory(
    blackoutAlpha: Float,
    onClick: () -> Unit,
) {
    val clickableModifier = Modifier.noRippleClickable(
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
