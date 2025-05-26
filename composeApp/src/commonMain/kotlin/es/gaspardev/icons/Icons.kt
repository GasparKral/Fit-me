package es.gaspardev.icons

import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object FitMeIcons {

    val Gender: ImageVector
        get() {
            return ImageVector.Builder(
                name = "bigender",
                defaultWidth = 24.0.dp,
                defaultHeight = 24.0.dp,
                viewportWidth = 24.0f,
                viewportHeight = 24.0f
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                    fillAlpha = 1.0f,
                    stroke = null,
                    strokeAlpha = 1.0f,
                    strokeLineWidth = 1.0f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    strokeLineMiter = 1.0f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(0.0f, 0.0f)
                    horizontalLineToRelative(24.0f)
                    verticalLineToRelative(24.0f)
                    horizontalLineToRelative(-24.0f)
                    close()
                }
                path(
                    fill = SolidColor(Color.Transparent),
                    fillAlpha = 1.0f,
                    stroke = SolidColor(Color.Black),
                    strokeAlpha = 1.0f,
                    strokeLineWidth = 2.0f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    strokeLineMiter = 1.0f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(11.0f, 11.0f)
                    moveToRelative(-4.0f, 0.0f)
                    arcToRelative(4.0f, 4.0f, 0.0f, true, true, 8.0f, 0.0f)
                    arcToRelative(4.0f, 4.0f, 0.0f, true, true, -8.0f, 0.0f)
                }
                path(
                    fill = SolidColor(Color.Transparent),
                    fillAlpha = 1.0f,
                    stroke = SolidColor(Color.Black),
                    strokeAlpha = 1.0f,
                    strokeLineWidth = 2.0f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    strokeLineMiter = 1.0f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(19.0f, 3.0f)
                    lineToRelative(-5.0f, 5.0f)
                }
                path(
                    fill = SolidColor(Color.Transparent),
                    fillAlpha = 1.0f,
                    stroke = SolidColor(Color.Black),
                    strokeAlpha = 1.0f,
                    strokeLineWidth = 2.0f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    strokeLineMiter = 1.0f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(15.0f, 3.0f)
                    horizontalLineToRelative(4.0f)
                    verticalLineToRelative(4.0f)
                }
                path(
                    fill = SolidColor(Color.Transparent),
                    fillAlpha = 1.0f,
                    stroke = SolidColor(Color.Black),
                    strokeAlpha = 1.0f,
                    strokeLineWidth = 2.0f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    strokeLineMiter = 1.0f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(11.0f, 16.0f)
                    verticalLineToRelative(6.0f)
                }
                path(
                    fill = SolidColor(Color.Transparent),
                    fillAlpha = 1.0f,
                    stroke = SolidColor(Color.Black),
                    strokeAlpha = 1.0f,
                    strokeLineWidth = 2.0f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    strokeLineMiter = 1.0f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(8.0f, 19.0f)
                    horizontalLineToRelative(6.0f)
                }
            }.build()
        }

    val Athlets: ImageVector
        get() {
            return Builder(
                name = "Athlets", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
                viewportWidth = 24f, viewportHeight = 24f
            ).apply {
                // Path d="M0 0h24v24H0z" fill="none"
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = null,
                    strokeLineWidth = 0.0f
                ) {
                    moveTo(0f, 0f)
                    lineTo(24f, 0f)
                    lineTo(24f, 24f)
                    lineTo(0f, 24f)
                    close()
                }

                // Path d="M9 7m-4 0a4 4 0 1 0 8 0a4 4 0 1 0 -8 0"
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 2.0f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(9f, 7f)
                    arcToRelative(4f, 4f, 0f, true, true, 8f, 0f)
                    arcToRelative(4f, 4f, 0f, true, true, -8f, 0f)
                }

                // Path d="M3 21v-2a4 4 0 0 1 4 -4h4a4 4 0 0 1 4 4v2"
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 2.0f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(3f, 21f)
                    verticalLineToRelative(-2f)
                    arcToRelative(4f, 4f, 0f, false, true, 4f, -4f)
                    horizontalLineToRelative(4f)
                    arcToRelative(4f, 4f, 0f, false, true, 4f, 4f)
                    verticalLineToRelative(2f)
                }

                // Path d="M16 3.13a4 4 0 0 1 0 7.75"
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 2.0f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(16f, 3.13f)
                    arcToRelative(4f, 4f, 0f, false, true, 0f, 7.75f)
                }

                // Path d="M21 21v-2a4 4 0 0 0 -3 -3.85"
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 2.0f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(21f, 21f)
                    verticalLineToRelative(-2f)
                    arcToRelative(4f, 4f, 0f, false, false, -3f, -3.85f)
                }
            }.build()
        }

    val Calendar: ImageVector
        get() {
            return Builder(
                name = "Calendar", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
                viewportWidth = 24f, viewportHeight = 24f
            ).apply {
                // Path: background transparent
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = null,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(0f, 0f)
                    lineTo(24f, 0f)
                    lineTo(24f, 24f)
                    lineTo(0f, 24f)
                    close()
                }

                // Main calendar filled shape
                path(
                    fill = SolidColor(Color.Black), // You can change to currentColor if handled in Compose theme
                    stroke = null,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(16f, 2f)
                    arcToRelative(1f, 1f, 0f, false, true, 1f, 1f)
                    verticalLineToRelative(1f)
                    horizontalLineToRelative(1f)
                    arcToRelative(3f, 3f, 0f, false, true, 3f, 3f)
                    verticalLineToRelative(12f)
                    arcToRelative(3f, 3f, 0f, false, true, -3f, 3f)
                    horizontalLineToRelative(-12f)
                    arcToRelative(3f, 3f, 0f, false, true, -3f, -3f)
                    verticalLineToRelative(-12f)
                    arcToRelative(3f, 3f, 0f, false, true, 3f, -3f)
                    horizontalLineToRelative(1f)
                    verticalLineToRelative(-1f)
                    arcToRelative(1f, 1f, 0f, false, true, 2f, 0f)
                    verticalLineToRelative(1f)
                    horizontalLineToRelative(6f)
                    verticalLineToRelative(-1f)
                    arcToRelative(1f, 1f, 0f, false, true, 1f, -1f)
                    close()
                    moveTo(19f, 9f)
                    horizontalLineToRelative(-14f)
                    verticalLineToRelative(9.625f)
                    curveToRelative(0f, 0.705f, 0.386f, 1.286f, 0.883f, 1.366f)
                    lineToRelative(0.117f, 0.009f)
                    horizontalLineToRelative(12f)
                    curveToRelative(0.513f, 0f, 0.936f, -0.53f, 0.993f, -1.215f)
                    lineToRelative(0.007f, -0.16f)
                    verticalLineTo(9f)
                    close()
                }

                // Inner calendar dot/indicator
                path(
                    fill = SolidColor(Color.Black),
                    stroke = null,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(12f, 12f)
                    arcToRelative(1f, 1f, 0f, false, true, 1f, 1f)
                    verticalLineToRelative(3f)
                    arcToRelative(1f, 1f, 0f, false, true, -2f, 0f)
                    verticalLineToRelative(-2f)
                    arcToRelative(1f, 1f, 0f, false, true, 1f, -2f)
                    close()
                }
            }.build()
        }


    val Messages: ImageVector
        get() {
            return Builder(
                name = "Messages", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
                viewportWidth = 24f, viewportHeight = 24f
            ).apply {
                // Background path (invisible)
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = null,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(0f, 0f)
                    lineTo(24f, 0f)
                    lineTo(24f, 24f)
                    lineTo(0f, 24f)
                    close()
                }

                // First message bubble (top right)
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = SolidColor(Color.Black), // Replace with desired stroke color
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(21f, 14f)
                    lineToRelative(-3f, -3f)
                    horizontalLineToRelative(-7f)
                    arcToRelative(1f, 1f, 0f, false, true, -1f, -1f)
                    verticalLineTo(4f)
                    arcToRelative(1f, 1f, 0f, false, true, 1f, -1f)
                    horizontalLineToRelative(9f)
                    arcToRelative(1f, 1f, 0f, false, true, 1f, 1f)
                    verticalLineToRelative(10f)
                }

                // Second message bubble (bottom left)
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(14f, 15f)
                    verticalLineToRelative(2f)
                    arcToRelative(1f, 1f, 0f, false, true, -1f, 1f)
                    horizontalLineTo(6f)
                    lineToRelative(-3f, 3f)
                    verticalLineTo(11f)
                    arcToRelative(1f, 1f, 0f, false, true, 1f, -1f)
                    horizontalLineToRelative(2f)
                }
            }.build()
        }

    val Nutrition: ImageVector
        get() {
            return Builder(
                name = "ToolsKitchen2", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
                viewportWidth = 24f, viewportHeight = 24f
            ).apply {
                // Invisible bounding box
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = null,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(0f, 0f)
                    lineTo(24f, 0f)
                    lineTo(24f, 24f)
                    lineTo(0f, 24f)
                    close()
                }

                // First path: right side utensil
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = SolidColor(Color.Black), // Use currentColor or theme-based color
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(19f, 3f)
                    verticalLineToRelative(12f)
                    horizontalLineToRelative(-5f)
                    curveToRelative(-0.023f, -3.681f, 0.184f, -7.406f, 5f, -12f)
                }

                // Second path: bottom right handle, left vertical handle, and left spoon/fork shape
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(19f, 15f)
                    verticalLineToRelative(6f)
                    horizontalLineToRelative(-1f)
                    verticalLineToRelative(-3f)

                    moveTo(8f, 1f)
                    verticalLineToRelative(17f)

                    moveTo(5f, 1f)
                    verticalLineToRelative(3f)
                    arcToRelative(3f, 3f, 0f, true, false, 6f, 0f)
                    verticalLineToRelative(-3f)
                }
            }.build()
        }

    val Weight: ImageVector
        get() {
            return Builder(
                name = "BarbellFilled", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
                viewportWidth = 24f, viewportHeight = 24f
            ).apply {
                // Bounding transparent path
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = null,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(0f, 0f)
                    lineTo(24f, 0f)
                    lineTo(24f, 24f)
                    lineTo(0f, 24f)
                    close()
                }

                // Left side vertical handle
                path(
                    fill = SolidColor(Color.Black),
                    stroke = null,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(4f, 7f)
                    arcToRelative(1f, 1f, 0f, false, true, 1f, 1f)
                    verticalLineToRelative(8f)
                    arcToRelative(1f, 1f, 0f, false, true, -2f, 0f)
                    verticalLineToRelative(-3f)
                    horizontalLineToRelative(-1f)
                    arcToRelative(1f, 1f, 0f, false, true, 0f, -2f)
                    horizontalLineToRelative(1f)
                    verticalLineToRelative(-3f)
                    arcToRelative(1f, 1f, 0f, false, true, 1f, -1f)
                    close()
                }

                // Right side vertical handle
                path(
                    fill = SolidColor(Color.Black),
                    stroke = null,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(20f, 7f)
                    arcToRelative(1f, 1f, 0f, false, true, 1f, 1f)
                    verticalLineToRelative(3f)
                    horizontalLineToRelative(1f)
                    arcToRelative(1f, 1f, 0f, false, true, 0f, 2f)
                    horizontalLineToRelative(-1f)
                    verticalLineToRelative(3f)
                    arcToRelative(1f, 1f, 0f, false, true, -2f, 0f)
                    verticalLineToRelative(-8f)
                    arcToRelative(1f, 1f, 0f, false, true, 1f, -1f)
                    close()
                }

                // Central bar and plates
                path(
                    fill = SolidColor(Color.Black),
                    stroke = null,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(16f, 5f)
                    arcToRelative(2f, 2f, 0f, false, true, 2f, 2f)
                    verticalLineToRelative(10f)
                    arcToRelative(2f, 2f, 0f, true, true, -4f, 0f)
                    verticalLineToRelative(-4f)
                    horizontalLineToRelative(-4f)
                    verticalLineToRelative(4f)
                    arcToRelative(2f, 2f, 0f, true, true, -4f, 0f)
                    verticalLineTo(7f)
                    arcToRelative(2f, 2f, 0f, true, true, 4f, 0f)
                    verticalLineToRelative(4f)
                    horizontalLineToRelative(4f)
                    verticalLineTo(7f)
                    arcToRelative(2f, 2f, 0f, false, true, 2f, -2f)
                    close()
                }
            }.build()
        }

    val ChartBar: ImageVector
        get() {
            return Builder(
                name = "ChartBar", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
                viewportWidth = 24f, viewportHeight = 24f
            ).apply {
                // Invisible bounding box
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = null,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(0f, 0f)
                    lineTo(24f, 0f)
                    lineTo(24f, 24f)
                    lineTo(0f, 24f)
                    close()
                }

                // Bar 1 (left)
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(3f, 13f)
                    arcToRelative(1f, 1f, 0f, false, true, 1f, -1f)
                    horizontalLineToRelative(4f)
                    arcToRelative(1f, 1f, 0f, false, true, 1f, 1f)
                    verticalLineToRelative(6f)
                    arcToRelative(1f, 1f, 0f, false, true, -1f, 1f)
                    horizontalLineTo(4f)
                    arcToRelative(1f, 1f, 0f, false, true, -1f, -1f)
                    close()
                }

                // Bar 2 (middle)
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(9f, 5f)
                    arcToRelative(1f, 1f, 0f, false, true, 1f, -1f)
                    horizontalLineToRelative(4f)
                    arcToRelative(1f, 1f, 0f, false, true, 1f, 1f)
                    verticalLineToRelative(14f)
                    arcToRelative(1f, 1f, 0f, false, true, -1f, 1f)
                    horizontalLineTo(10f)
                    arcToRelative(1f, 1f, 0f, false, true, -1f, -1f)
                    close()
                }

                // Bar 3 (right)
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(15f, 9f)
                    arcToRelative(1f, 1f, 0f, false, true, 1f, -1f)
                    horizontalLineToRelative(4f)
                    arcToRelative(1f, 1f, 0f, false, true, 1f, 1f)
                    verticalLineToRelative(10f)
                    arcToRelative(1f, 1f, 0f, false, true, -1f, 1f)
                    horizontalLineTo(16f)
                    arcToRelative(1f, 1f, 0f, false, true, -1f, -1f)
                    close()
                }

                // Bottom axis line
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(4f, 20f)
                    horizontalLineToRelative(14f)
                }
            }.build()
        }
}