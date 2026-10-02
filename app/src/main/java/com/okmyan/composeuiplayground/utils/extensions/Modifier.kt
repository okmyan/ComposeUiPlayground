package com.okmyan.composeuiplayground.utils.extensions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

@Stable
fun Modifier.mirror(): Modifier {
    return scale(scaleX = -1f, scaleY = 1f)
}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}

fun Modifier.hapticNoRippleClickable(
    isHapticFeedbackEnabled: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    val haptics = LocalHapticFeedback.current

    this.noRippleClickable {
        if (isHapticFeedbackEnabled) {
            haptics.performHapticFeedback(HapticFeedbackType.Confirm)
        }
        onClick()
    }
}
