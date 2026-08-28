package com.okmyan.composeuiplayground.utils.extensions

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.clearFocusOnTap(
    focusManager: FocusManager,
): Modifier = pointerInput(focusManager) {
    detectTapGestures {
        focusManager.clearFocus()
    }
}
