package com.okmyan.composeuiplayground.features.instagram

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun InstagramStoryScreen(
    id: Long,
    viewModel: InstagramViewModel,
    storyViewModel: InstagramStoryViewModel = koinViewModel { parametersOf(id) },
    hasPreviousStory: Boolean,
    onGoToPrevious: () -> Unit,
    onGoToNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "This is a screen Instagram Story #$id")

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            if (hasPreviousStory) {
                Button(onClick = onGoToPrevious) {
                    Text("Previous")
                }
            }

            Button(onClick = onGoToNext) {
                Text("Next")
            }
        }
    }
}
