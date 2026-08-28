package com.okmyan.composeuiplayground.utils.extensions

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.clearFocusOnTap(
    focusManager: FocusManager,
): Modifier = pointerInput(focusManager) {
    detectTapGestures {
        focusManager.clearFocus()
    }
}

@Stable
fun Modifier.mirror(): Modifier {
    return scale(scaleX = -1f, scaleY = 1f)
}
