package com.okmyan.composeuiplayground.features.instagram.screens.story.components

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val InstagramComment: ImageVector
    get() {
        if (_comment != null) {
            return _comment!!
        }
        _comment = materialIcon(name = "Outlined.MapsUgc") {
            materialPath {
                moveTo(12.0f, 4.0f)
                curveToRelative(-4.41f, 0.0f, -8.0f, 3.59f, -8.0f, 8.0f)
                reflectiveCurveToRelative(3.59f, 8.0f, 8.0f, 8.0f)
                curveToRelative(1.18f, 0.0f, 2.34f, -0.26f, 3.43f, -0.78f)
                curveToRelative(0.27f, -0.13f, 0.56f, -0.19f, 0.86f, -0.19f)
                curveToRelative(0.19f, 0.0f, 0.38f, 0.03f, 0.56f, 0.08f)
                lineToRelative(3.2f, 0.94f)
                lineToRelative(-0.94f, -3.2f)
                curveToRelative(-0.14f, -0.47f, -0.1f, -0.98f, 0.11f, -1.42f)
                curveTo(19.74f, 14.34f, 20.0f, 13.18f, 20.0f, 12.0f)
                curveTo(20.0f, 7.59f, 16.41f, 4.0f, 12.0f, 4.0f)
                moveTo(12.0f, 2.0f)
                curveTo(17.52f, 2.0f, 22.0f, 6.48f, 22.0f, 12.0f)
                curveToRelative(0.0f, 1.54f, -0.36f, 2.98f, -0.97f, 4.29f)
                lineTo(23.0f, 23.0f)
                lineToRelative(-6.71f, -1.97f)
                curveTo(14.98f, 21.64f, 13.54f, 22.0f, 12.0f, 22.0f)
                curveToRelative(-5.52f, 0.0f, -10.0f, -4.48f, -10.0f, -10.0f)
                curveTo(2.0f, 6.48f, 6.48f, 2.0f, 12.0f, 2.0f)
                lineTo(12.0f, 2.0f)
                close()
            }
        }
        return _comment!!
    }

private var _comment: ImageVector? = null
