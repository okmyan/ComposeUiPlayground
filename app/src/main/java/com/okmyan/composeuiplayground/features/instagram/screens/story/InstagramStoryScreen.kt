package com.okmyan.composeuiplayground.features.instagram.screens.story

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.okmyan.composeuiplayground.R
import com.okmyan.composeuiplayground.features.instagram.domain.model.UserWithStories
import com.okmyan.composeuiplayground.features.instagram.screens.story.components.Header
import com.okmyan.composeuiplayground.features.instagram.screens.story.components.MessageTextField
import com.okmyan.composeuiplayground.features.instagram.screens.story.components.Stories
import com.okmyan.composeuiplayground.features.instagram.screens.story.components.Tail
import com.okmyan.composeuiplayground.utils.extensions.clearFocusOnTap
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

@Composable
fun InstagramStoryScreen(
    userWithStories: UserWithStories,
    viewModel: InstagramStoryViewModel = koinViewModel {
        parametersOf(userWithStories)
    },
    hasPreviousStory: Boolean,
    onGoToPrevious: () -> Unit,
    onGoToNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val enteredMessage = state.enteredMessage

    var isMessageEditing by remember { mutableStateOf(false) }
    val contentAlpha by animateFloatAsState(
        targetValue = if (isMessageEditing) 0.3f else 0f,
        animationSpec = tween(300),
    )

    Timber.d("alpha: $contentAlpha")
    val messageFieldModifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)

    val focusManager = LocalFocusManager.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .clearFocusOnTap(focusManager),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f)) {
                Stories(
                    stories = state.stories,
                    activeStoryIndex = state.activeStoryIndex,
                    onStorySeen = viewModel::onStorySeen,
                    isContinuous = !isMessageEditing,
                    onStoryEnded = {
                        if (state.isActiveStoryLastOne) {
                            onGoToNext()
                        } else {
                            viewModel.onStoryEnded(it)
                        }
                    },
                )
                Header(
                    modifier = Modifier.padding(
                        horizontal = 10.dp,
                        vertical = 20.dp
                    ),
                    user = state.user, activeStory = state.activeStory,
                )
            }

            if (!state.user.isCurrentUser) {
                Tail(
                    modifier = messageFieldModifier,
                    onMessageEditing = { isMessageEditing = true },
                    message = enteredMessage.text,
                    isLiked = state.activeStory.isLiked,
                    onLike = { viewModel.onStoryLiked(state.activeStory.id) },
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = contentAlpha)),
            contentAlignment = Alignment.Center,
        ) {}

        var showMessageSentNotification by remember { mutableStateOf(false) }
        MessageSentNotification(showMessageSentNotification)

        val scope = rememberCoroutineScope()
        var notificationJob by remember { mutableStateOf<Job?>(null) }

        MessageTextField(
            isMessageEditing = isMessageEditing,
            message = enteredMessage,
            onMessageChange = viewModel::onMessageChange,
            onKeyboardHide = { isMessageEditing = false },
            onMessageSent = {
                viewModel.onMessageSend()

                notificationJob?.cancel()
                notificationJob =
                    scope.launch(CoroutineName("InstagramStoryScreen - show Message sent notification")) {
                        delay(1000)
                        showMessageSentNotification = true
                        delay(4000)
                        showMessageSentNotification = false
                    }
            },
            modifier = messageFieldModifier,
        )
    }
}

@Composable
fun BoxScope.MessageSentNotification(
    show: Boolean,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = show,
        modifier = modifier.align(Alignment.Center),
        enter = fadeIn(animationSpec = tween(500)),
        exit = fadeOut(animationSpec = tween(500)),
    ) {
        Text(
            text = stringResource(R.string.instagram_story_message_sent),
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
