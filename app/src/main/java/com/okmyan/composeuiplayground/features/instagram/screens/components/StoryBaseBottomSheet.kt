package com.okmyan.composeuiplayground.features.instagram.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.okmyan.composeuiplayground.R
import com.okmyan.composeuiplayground.ui.theme.BottomSheetContentColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryBaseBottomSheet(
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = stringResource(R.string.instagram_story_options_about),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(BottomSheetContentColor)
                    .padding(vertical = 10.dp),
                content = content
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF252424)
@Composable
private fun StoryBaseBottomSheetPreview() {
    StoryBaseBottomSheet(
        onDismiss = {}
    ) {
        StoryBottomSheetOption(
            icon = Icons.Outlined.Report,
            text = stringResource(R.string.instagram_story_options_report),
            onClick = {},
        )
    }
}
