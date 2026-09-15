package com.okmyan.composeuiplayground.features.instagram.screens.accountownerstory.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.History
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.okmyan.composeuiplayground.R
import com.okmyan.composeuiplayground.features.instagram.screens.components.StoryBaseBottomSheet
import com.okmyan.composeuiplayground.features.instagram.screens.components.StoryBottomSheetOption

@Composable
fun AccountOwnerStoryBottomSheet(
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onArchive: () -> Unit,
) {
    StoryBaseBottomSheet(
        onDismiss = onDismiss,
    ) {
        StoryBottomSheetOption(
            icon = Icons.Outlined.Delete,
            text = stringResource(R.string.instagram_account_owner_story_options_delete),
            color = Color.Red,
            onClick = onDelete,
        )

        StoryBottomSheetOption(
            icon = Icons.Outlined.History,
            text = stringResource(R.string.instagram_account_owner_story_options_archive),
            onClick = onArchive,
        )
    }
}

@Preview
@Composable
private fun AccountOwnerStoryBottomSheetPreview() {
    AccountOwnerStoryBottomSheet(
        onDismiss = {},
        onDelete = {},
        onArchive = {},
    )
}
