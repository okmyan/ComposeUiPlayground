package com.okmyan.composeuiplayground.features.instagram.screens.story.components

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imeAnimationTarget
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.okmyan.composeuiplayground.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MessageTextField(
    isMessageEditing: Boolean,
    message: TextFieldValue,
    onMessageChange: (TextFieldValue) -> Unit,
    onKeyboardHide: () -> Unit,
    onMessageSent: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(isMessageEditing) {
        if (isMessageEditing) {
            focusRequester.requestFocus()
        }
    }

    val density = LocalDensity.current
    val imeBottom = WindowInsets.ime.getBottom(density)
    val targetImeBottom = WindowInsets.imeAnimationTarget.getBottom(density)
    val navigationBottom = WindowInsets.navigationBars.getBottom(density)

    val keyboardBottom = (imeBottom - navigationBottom).coerceAtLeast(0)
    val targetKeyboardBottom = (targetImeBottom - navigationBottom).coerceAtLeast(0)
    val isKeyboardHiding =
        keyboardBottom > 0 && targetKeyboardBottom == 0

    LaunchedEffect(isKeyboardHiding) {
        if (isKeyboardHiding) {
            onKeyboardHide()
        }
    }

    val targetKeyboardHeight = with(density) {
        targetKeyboardBottom.toDp()
    }
    // We can't use AnimatedVisibility, as we only want to show the text field after the keyboard
    // has been opened
    val alpha by animateFloatAsState(
        targetValue = if (isMessageEditing && targetKeyboardBottom > 0) 1f else 0f,
    )

    val textStyle = TextStyle(color = Color.White, fontSize = 14.sp)
    val textFieldHeight = 44.dp
    val textFieldShape = RoundedCornerShape(textFieldHeight / 2)

    if (isMessageEditing) {
        BasicTextField(
            value = message,
            onValueChange = onMessageChange,
            modifier = modifier
                .focusRequester(focusRequester)
                .fillMaxWidth()
                .padding(bottom = targetKeyboardHeight)
                .alpha(alpha),
            textStyle = textStyle,
            maxLines = 5,
            cursorBrush = SolidColor(Color.White),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .defaultMinSize(minHeight = textFieldHeight)
                        .clip(textFieldShape)
                        .border(width = 0.4.dp, color = Color.White, shape = textFieldShape)
                        .padding(start = 18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        // Placeholder
                        if (message.text.isEmpty()) {
                            Text(
                                text = stringResource(R.string.instagram_story_send_message),
                                style = textStyle,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                            )
                        }

                        innerTextField()
                    }

                    SendMessageButton(
                        isMessageNotEmpty = message.text.isNotEmpty(),
                        buttonHeight = textFieldHeight,
                        onMessageSent = onMessageSent,
                    )
                }
            }
        )
    }
}

@Composable
fun RowScope.SendMessageButton(
    isMessageNotEmpty: Boolean,
    buttonHeight: Dp,
    onMessageSent: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    var currentState by remember { mutableStateOf(isMessageNotEmpty) }
    val transition = updateTransition(currentState, label = "send button transition")

    LaunchedEffect(isMessageNotEmpty) {
        currentState = isMessageNotEmpty
    }

    val sendButtonRotation by transition.animateFloat(label = "send button rotation") { state ->
        when (state) {
            false -> 23f
            true -> -23f
        }
    }
    val sendButtonSize by transition.animateDp(label = "send button size") { state ->
        when (state) {
            false -> 10.dp
            true -> 20.dp
        }
    }

    if (isMessageNotEmpty) {
        Box(
            modifier = Modifier
                .height(buttonHeight)
                .width(60.dp)
                .padding(5.5.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable(onClick = {
                    onMessageSent()
                    focusManager.clearFocus()
                })
                .align(Alignment.Bottom),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.Send,
                contentDescription = stringResource(R.string.instagram_story_send_message),
                tint = Color.Black,
                modifier = Modifier
                    .size(sendButtonSize)
                    .offset(x = 2.dp, y = (-2).dp)
                    .rotate(sendButtonRotation)
            )
        }
    }
}
