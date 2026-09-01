package com.okmyan.composeuiplayground.features.instagram.screens.story.components

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val InstagramMenu: ImageVector
    get() {
        if (_menu != null) {
            return _menu!!
        }
        _menu = materialIcon(name = "InstagramMenu") {
            materialPath {
                moveTo(3.0f, 15.0f)
                horizontalLineToRelative(13.0f)
                verticalLineToRelative(-1.5f)
                lineTo(3.0f, 13.5f)
                verticalLineToRelative(1.5f)
                close()

                moveTo(3.0f, 6.0f)
                verticalLineToRelative(1.5f)
                horizontalLineToRelative(18.0f)
                lineTo(21.0f, 6.0f)
                lineTo(3.0f, 6.0f)
                close()
            }
        }
        return _menu!!
    }

private var _menu: ImageVector? = null
