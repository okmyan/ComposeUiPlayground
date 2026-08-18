package com.okmyan.composeuiplayground.features.instagram.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.okmyan.composeuiplayground.R
import com.okmyan.composeuiplayground.ui.theme.PurpleGrey40

@Composable
fun InstagramHomeScreen(
    viewModel: InstagramHomeViewModel,
    onGoToStories: (UserWithStories) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            items(state.usersWithStories) { userWithStories ->
                Story(
                    userWithStories = userWithStories,
                    onClick = {
                        onGoToStories(userWithStories)
                    },
                    onAddStory = {
                        Toast.makeText(context, "add", Toast.LENGTH_SHORT).show()
                    },
                )
            }
        }

        Spacer(modifier = Modifier.height(13.dp))

        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "This is an Instagram page")
        }
    }
}

@Composable
fun Story(
    userWithStories: UserWithStories,
    onClick: () -> Unit,
    onAddStory: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        AvatarCircle(
            user = userWithStories.user,
            hasStories = userWithStories.hasStories,
            onClick = onClick,
            onAddStory = onAddStory,
        )

        Text(text = userWithStories.user.username)
    }
}

@Composable
fun AvatarCircle(
    user: User,
    hasStories: Boolean,
    onClick: () -> Unit,
    onAddStory: () -> Unit,
) = user.run {
    Box {
        val brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFFCAF45), Color(0xFFF77737), Color(0xFFF56040), Color(0xFFFD1D1D),
                Color(0xFFE1306C), Color(0xFFC13584), Color(0xFF833AB4),
            )
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .then(
                    other = if (hasStories) {
                        Modifier.border(
                            width = 3.dp,
                            brush = brush,
                            shape = CircleShape
                        )
                    } else Modifier

                )
        ) {
            val contentDescription = if (isCurrentUser) {
                stringResource(R.string.instagram_your_avatar_description)
            } else {
                stringResource(R.string.instagram_avatar_description, username)
            }
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(avatarPreviewUrl)
                    .memoryCacheKey(id.toString())
                    .diskCacheKey(id.toString())
                    .build(),
                contentDescription = contentDescription,
                modifier = Modifier
                    .size(83.dp)
                    .border(0.2.dp, PurpleGrey40, CircleShape)
                    .clip(CircleShape)
                    .clickable(enabled = hasStories, onClick = onClick),
            )
        }

        if (isCurrentUser) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.instagram_add_story),
                tint = Color.Black,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(2.5.dp)
                    .size(30.dp)
                    .background(MaterialTheme.colorScheme.background, CircleShape)
                    .padding(2.5.dp)
                    .background(Color.White, CircleShape)
                    .padding(2.5.dp)
                    .clickable(onClick = onAddStory)
            )
        }
    }
}
