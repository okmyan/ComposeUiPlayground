package com.okmyan.composeuiplayground.features.instagram.screens.story.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.okmyan.composeuiplayground.features.instagram.utils.STORY_PRESSING_DELAY
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber

@Composable
fun StoryGestures(
    onPress: () -> Unit,
    onPressRelease: () -> Unit,
    isMovingEnabled: Boolean,
    onMove: () -> Unit,
    onMoveRelease: () -> Unit,
    onDragUp: () -> Unit,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
) {
    Timber.tag("StoryGestures").d("isMovingEnabled: $isMovingEnabled")

    val currentOnPress by rememberUpdatedState(onPress)
    val currentOnPressRelease by rememberUpdatedState(onPressRelease)
    val currentOnMove by rememberUpdatedState(onMove)
    val currentOnMoveRelease by rememberUpdatedState(onMoveRelease)
    val currentOnDragUp by rememberUpdatedState(onDragUp)
    val currentOnLeftClick by rememberUpdatedState(onLeftClick)
    val currentOnRightClick by rememberUpdatedState(onRightClick)

    var isLongPressed by remember { mutableStateOf(false) }
    var hasTriggeredDragUp by remember { mutableStateOf(false) }
    var accumulatedY by remember { mutableFloatStateOf(0f) }

    val pointerModifier = Modifier
        // Handle press gestures
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    // Set timeout for onPress event
                    withTimeoutOrNull(STORY_PRESSING_DELAY) {
                        Timber.tag("StoryGestures.tap").d("onPress - takes less than 150ms")
                        tryAwaitRelease()
                    } ?: run {
                        Timber.tag("StoryGestures.tap").d("onPress - takes more than 150ms")
                        isLongPressed = true
                        currentOnPress()

                        val releaseResult = tryAwaitRelease()
                        if (releaseResult) {
                            Timber.tag("StoryGestures.tap").d("onPress - releases")
                        } else {
                            Timber.tag("StoryGestures.tap").d("onPress - canceled by moving")
                        }
                        currentOnPressRelease()
                    }
                },
                onTap = { clickOffset ->
                    Timber.tag("StoryGestures.tap").d("onTap, isPressed: $isLongPressed")
                    // Enable tapping only if didn't register the long press
                    if (!isLongPressed) {
                        val screenWidth = size.width

                        if (clickOffset.x < screenWidth * 0.33f) {
                            Timber.tag("StoryGestures.tap").d("onTap - Left click")
                            currentOnLeftClick()
                        } else {
                            Timber.tag("StoryGestures.tap").d("onTap - Right click")
                            currentOnRightClick()
                        }
                    }
                    isLongPressed = false
                }
            )
        }
        // Hande vertical drag gestures
        .pointerInput(isMovingEnabled) {
            detectVerticalDragGestures(
                onDragStart = {
                    Timber.tag("StoryGestures.drag").d("onDragStart")
                    // Reset flags in the beginning of the gesture
                    hasTriggeredDragUp = false
                    accumulatedY = 0f
                },
                onVerticalDrag = { change, dragAmount ->
                    Timber.tag("StoryGestures.drag").d("onVerticalDrag - dragAmount: $dragAmount")

                    change.consume()
                    currentOnMove()

                    if (!hasTriggeredDragUp) {
                        if (dragAmount < 0f) {
                            accumulatedY += dragAmount
                        }

                        if (accumulatedY < -50f) {
                            Timber.tag("StoryGestures.drag").d("onVerticalDrag - onDragUp")

                            currentOnDragUp()
                            hasTriggeredDragUp = true   // Prevent multiple triggers
                        }
                    }
                },
                onDragEnd = {
                    Timber.tag("StoryGestures.drag").d("onDragEnd")
                    currentOnMoveRelease()
                },
                onDragCancel = {
                    Timber.tag("StoryGestures.drag").d("onDragCancel")
                    currentOnMoveRelease()
                }
            )
        }
    Box(
        modifier = pointerModifier
            .fillMaxSize()
    )
}
