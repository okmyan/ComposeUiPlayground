package com.okmyan.composeuiplayground.features.instagram.screens.components

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.unit.IntSize
import com.okmyan.composeuiplayground.features.instagram.utils.STORY_PRESSING_DELAY
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import kotlin.math.PI
import kotlin.math.abs

@Composable
fun StoryGestures(
    onLongPressOrZoom: () -> Unit,
    onLongPressOrZoomRelease: () -> Unit,
    isMovingEnabled: Boolean,
    onMove: () -> Unit,
    onMoveRelease: () -> Unit,
    onDragUp: () -> Unit,
    onDragDown: (Float) -> Unit,
    onDragDownRelease: () -> Unit,
    onTransformation: (Float, Offset) -> Unit,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
) {
    Timber.tag("StoryGestures").d("isMovingEnabled: $isMovingEnabled")

    val currentOnLongPressOrZoom by rememberUpdatedState(onLongPressOrZoom)
    val currentOnLongPressOrZoomRelease by rememberUpdatedState(onLongPressOrZoomRelease)
    val currentOnMove by rememberUpdatedState(onMove)
    val currentOnMoveRelease by rememberUpdatedState(onMoveRelease)
    val currentOnDragUp by rememberUpdatedState(onDragUp)
    val currentOnDragDown by rememberUpdatedState(onDragDown)
    val currentOnDragDownRelease by rememberUpdatedState(onDragDownRelease)
    val currentOnLeftClick by rememberUpdatedState(onLeftClick)
    val currentOnRightClick by rememberUpdatedState(onRightClick)

    // Enable tapping only if didn't register the long press
    var isTapAllowed by remember { mutableStateOf(false) }

    // Prevents multiple triggers
    var hasTriggeredDragUp by remember { mutableStateOf(false) }
    var hasTriggeredDragDown by remember { mutableStateOf(false) }
    var accumulatedYDragUpAmount by remember { mutableFloatStateOf(0f) }
    var accumulatedYDragDownAmount by remember { mutableFloatStateOf(0f) }

    var offset by remember { mutableStateOf(Offset.Zero) }
    var zoom by remember { mutableFloatStateOf(1f) }

    LaunchedEffect(zoom, offset) {
        onTransformation(zoom, offset)
    }

    var isLongPressing by remember { mutableStateOf(false) }
    var isZooming by remember { mutableStateOf(false) }

    LaunchedEffect(isLongPressing, isZooming) {
        if (isLongPressing || isZooming) {
            currentOnLongPressOrZoom()
        } else {
            currentOnLongPressOrZoomRelease()
        }
    }

    val pointerModifier = Modifier
        // Handle long press and tap gestures
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    // Set timeout for long press event
                    withTimeoutOrNull(STORY_PRESSING_DELAY) {
                        Timber.tag("StoryGestures.tap").d("onPress - takes less than 150ms")
                        tryAwaitRelease()
                    } ?: run {
                        Timber.tag("StoryGestures.tap").d("onPress - long press")
                        isTapAllowed = true
                        isLongPressing = true

                        val releaseResult = tryAwaitRelease()
                        if (releaseResult) {
                            Timber.tag("StoryGestures.tap").d("onPress - long press released")
                        } else {
                            Timber.tag("StoryGestures.tap")
                                .d("onPress - long press canceled by another gesture")
                        }
                        isLongPressing = false
                    }
                },
                onTap = { clickOffset ->
                    Timber.tag("StoryGestures.tap").d("onTap, isTapAllowed: $isTapAllowed")
                    // Enable tapping only if didn't register the long press
                    if (!isTapAllowed) {
                        val screenWidth = size.width

                        if (clickOffset.x < screenWidth * 0.33f) {
                            Timber.tag("StoryGestures.tap").d("onTap - Left click")
                            currentOnLeftClick()
                        } else {
                            Timber.tag("StoryGestures.tap").d("onTap - Right click")
                            currentOnRightClick()
                        }
                    }
                    isTapAllowed = false
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
                    hasTriggeredDragDown = false
                    accumulatedYDragUpAmount = 0f
                    accumulatedYDragDownAmount = 0f
                },
                onVerticalDrag = { change, dragAmount ->
                    Timber.tag("StoryGestures.drag")
                        .d("onVerticalDrag - dragAmount: $dragAmount, accumulatedYDragUpAmount: $accumulatedYDragUpAmount, accumulatedYDragDownAmount: $accumulatedYDragDownAmount")

                    change.consume()
                    currentOnMove()

                    if (!hasTriggeredDragUp && !hasTriggeredDragDown) {
                        if (dragAmount < 0f) {
                            accumulatedYDragUpAmount += dragAmount
                            accumulatedYDragDownAmount = 0f
                        }

                        if (dragAmount > 0f) {
                            accumulatedYDragUpAmount = 0f
                            accumulatedYDragDownAmount += dragAmount
                        }

                        if (accumulatedYDragUpAmount < -50f) {
                            Timber.tag("StoryGestures.drag").d("onVerticalDrag - onDragUp")

                            currentOnDragUp()
                            hasTriggeredDragUp = true   // Prevent multiple triggers
                        }

                        if (accumulatedYDragDownAmount > 50f) {
                            Timber.tag("StoryGestures.drag").d("onVerticalDrag - onDragDown")

                            accumulatedYDragDownAmount = 0f
                            hasTriggeredDragDown = true
                        }
                    }

                    if (hasTriggeredDragDown) {
                        currentOnDragDown(dragAmount)
                    }
                },
                onDragEnd = {
                    Timber.tag("StoryGestures.drag").d("onDragEnd")
                    currentOnMoveRelease()

                    if (hasTriggeredDragDown) {
                        currentOnDragDownRelease()
                    }
                },
                onDragCancel = {
                    Timber.tag("StoryGestures.drag").d("onDragCancel")
                    currentOnMoveRelease()
                }
            )
        }
        // Handle multitouch gestures
        .pointerInput(Unit) {
            detectMultitouchTransformGestures(
                onGestureStart = {
                    Timber.tag("StoryGestures.mt").d("onGestureStart")
                    isZooming = true
                },
                onGesture = { centroid, pan, gestureZoom, _ ->
                    Timber.tag("StoryGestures.mt").d("onGesture")
                    val newZoom = (zoom * gestureZoom).coerceIn(MIN_ZOOM, MAX_ZOOM)
                    offset = offset.calculateNewOffset(
                        centroid = centroid,
                        pan = pan,
                        currentZoom = zoom,
                        newZoom = newZoom,
                        size = size
                    )
                    zoom = newZoom
                },
                onGestureEnd = {
                    Timber.tag("StoryGestures.mt").d("onGestureEnd")
                    isZooming = false
                    offset = Offset.Zero
                    zoom = 1f
                }
            )
        }
    Box(
        modifier = pointerModifier
            .fillMaxSize()
    )
}

suspend fun PointerInputScope.detectMultitouchTransformGestures(
    panZoomLock: Boolean = false,
    onGestureStart: (() -> Unit)? = null,
    onGesture: (centroid: Offset, pan: Offset, zoom: Float, rotation: Float) -> Unit,
    onGestureEnd: (() -> Unit)? = null
) {
    awaitEachGesture {
        var rotation = 0f
        var zoom = 1f
        var pan = Offset.Zero
        var pastTouchSlop = false
        // Reduced touchSlop for 2-finger multitouch to eliminate initial lag/jerk
        val touchSlop = viewConfiguration.touchSlop / 3f
        var lockedToPanZoom = false
        var isTransforming = false

        awaitFirstDown(requireUnconsumed = false)
        do {
            val event = awaitPointerEvent()
            val canceled = event.changes.any { it.isConsumed }
            if (!canceled) {
                val pointerCount = event.changes.count { it.pressed }
                // Gesture activates ONLY with 2+ fingers, but can continue with 1 finger if already activated
                if (pointerCount >= 2 || (pastTouchSlop && pointerCount >= 1)) {
                    val zoomChange = event.calculateZoom()
                    val rotationChange = event.calculateRotation()
                    val panChange = event.calculatePan()

                    // Sensitivity threshold check
                    if (!pastTouchSlop) {
                        zoom *= zoomChange
                        rotation += rotationChange
                        pan += panChange

                        val centroidSize = event.calculateCentroidSize(useCurrent = false)
                        val zoomMotion = abs(1 - zoom) * centroidSize
                        val rotationMotion = abs(rotation * PI.toFloat() / 180f)
                        val panMotion = pan.getDistance()

                        if (zoomMotion > touchSlop || rotationMotion > touchSlop || panMotion > touchSlop) {
                            pastTouchSlop = true
                            lockedToPanZoom = panZoomLock && rotationMotion < touchSlop
                        }
                    }

                    if (pastTouchSlop) {
                        val centroid = event.calculateCentroid(useCurrent = false)
                        val effectiveRotation = if (lockedToPanZoom) 0f else rotationChange

                        if (effectiveRotation != 0f || zoomChange != 1f || panChange != Offset.Zero) {
                            if (!isTransforming) {
                                isTransforming = true
                                onGestureStart?.invoke()
                            }
                            onGesture(centroid, panChange, zoomChange, effectiveRotation)
                        }
                        event.changes.forEach {
                            if (it.positionChanged()) {
                                it.consume()
                            }
                        }
                    }
                } else {
                    pastTouchSlop = false
                    zoom = 1f
                    rotation = 0f
                    pan = Offset.Zero
                }
            }
        } while (!canceled && event.changes.any { it.pressed })

        if (isTransforming) {
            onGestureEnd?.invoke()
        }
    }
}

private const val MIN_ZOOM = 1f
private const val MAX_ZOOM = 5f

fun Offset.calculateNewOffset(
    centroid: Offset,
    pan: Offset,
    currentZoom: Float,
    newZoom: Float,
    size: IntSize,
): Offset {
    val newOffset = (this + centroid / currentZoom) -
            (centroid / newZoom + pan / currentZoom)
    return Offset(
        newOffset.x.coerceIn(0f, (size.width / currentZoom) * (currentZoom - 1f)),
        newOffset.y.coerceIn(0f, (size.height / currentZoom) * (currentZoom - 1f))
    )
}
