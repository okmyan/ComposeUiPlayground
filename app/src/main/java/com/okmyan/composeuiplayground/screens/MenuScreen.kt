package com.okmyan.composeuiplayground.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MenuScreen(
    onGoToA: () -> Unit,
    onGoToB: () -> Unit,
    onGoToC: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = onGoToA) {
            Text(text = "Go to A")
        }

        Button(onClick = onGoToB) {
            Text(text = "Go to B")
        }

        Button(onClick = onGoToC) {
            Text(text = "Go to C")
        }

    }

}
