package io.github.sachingurnaney.shimmers

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


/* --------------------------------------------------------------------------
   🔹 Shimmer Config
-------------------------------------------------------------------------- */
enum class ShimmerEffect {
    Shimmer, Pulse, HighlightSweep, PageSwipe, CardFlipRush, SeaWave, Ventilator
}

enum class ShimmerDirection {
    Horizontal, Vertical, Diagonal
}


/**
 * Returns default shimmer colors based on system theme.
 */
@Composable
@Stable
private fun defaultShimmerColors(): List<Color> {
    val isDark = isSystemInDarkTheme()
    val baseColor = if (isDark) Color(0xFF282828) else Color(0xFFE0E0E0)
    val highlightColor = if (isDark) Color(0xFF383838) else Color(0xFFC8C8C8)

    return listOf(
        baseColor,
        highlightColor,
        baseColor
    )
}

/**
 * Returns an animated Brush for shimmer placeholders.
 * Automatically adapts to light/dark mode by default.
 */
@Composable
fun rememberShimmerBrush(
    effect: ShimmerEffect = ShimmerEffect.Shimmer,
    shimmerDirection: ShimmerDirection = ShimmerDirection.Diagonal,
    durationMillis: Int = 1200,
    shimmerColors: List<Color>? = null,
    size: Size
): Brush {
    val transition = rememberInfiniteTransition(label = "shimmerTransition")
    val colors = shimmerColors ?: defaultShimmerColors()

    val anim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            // Use a non-linear easing for a more fluid, professional feel
            animation = tween(durationMillis = durationMillis, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerProgress"
    )

    return when (effect) {
        ShimmerEffect.Shimmer -> {
            // A professional shimmer is a narrow highlight band that sweeps across
            val width = size.width
            val height = size.height
            val gradientWidth = width * 0.4f // Highlight is 40% of the width
            val animatedPosition = (width + gradientWidth) * anim.value - gradientWidth

            val start = when (shimmerDirection) {
                ShimmerDirection.Horizontal -> Offset(animatedPosition, 0f)
                ShimmerDirection.Vertical -> Offset(0f, animatedPosition)
                ShimmerDirection.Diagonal -> Offset(animatedPosition, animatedPosition)
            }
            val end = when (shimmerDirection) {
                ShimmerDirection.Horizontal -> Offset(animatedPosition + gradientWidth, height)
                ShimmerDirection.Vertical -> Offset(width, animatedPosition + gradientWidth)
                ShimmerDirection.Diagonal -> Offset(animatedPosition + gradientWidth, animatedPosition + gradientWidth)
            }
            Brush.linearGradient(colors = colors, start = start, end = end)
        }

        ShimmerEffect.Pulse -> {
            val alpha = transition.animateFloat(
                initialValue = 0.2f,
                targetValue = 0.8f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = durationMillis, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulseAlpha"
            )
            // Use a solid color brush with a pulsing alpha for a cleaner pulse effect
            Brush.linearGradient(listOf(colors.first().copy(alpha = alpha.value), colors.last().copy(alpha = alpha.value)))
        }

        ShimmerEffect.HighlightSweep -> {
            // A narrower, more focused highlight sweep
            val startX = -size.width
            val endX = size.width * 2
            val animatedX = startX + (endX - startX) * anim.value

            Brush.linearGradient(
                colors = colors,
                start = Offset(x = animatedX, y = 0f),
                end = Offset(x = animatedX + size.width * 0.5f, y = size.height)
            )
        }

        ShimmerEffect.PageSwipe -> {
            // The original logic is good, just applying the new colors and easing
            val startX = -size.width * 1.5f
            val endX = size.width * 1.5f
            val animatedX = startX + (endX - startX) * anim.value

            val start = when (shimmerDirection) {
                ShimmerDirection.Horizontal -> Offset(animatedX, 0f)
                ShimmerDirection.Vertical -> Offset(0f, animatedX)
                ShimmerDirection.Diagonal -> Offset(animatedX, animatedX)
            }
            val end = when (shimmerDirection) {
                ShimmerDirection.Horizontal -> Offset(animatedX + size.width, 0f)
                ShimmerDirection.Vertical -> Offset(0f, animatedX + size.height)
                ShimmerDirection.Diagonal -> Offset(animatedX + size.width, animatedX + size.height)
            }
            Brush.linearGradient(colors, start = start, end = end)
        }

        ShimmerEffect.CardFlipRush -> {
            // The original logic is fine, but it can be more intentional. Let's make it a sharp, horizontal highlight.
            val startX = -size.width
            val endX = size.width * 2
            val animatedX = startX + (endX - startX) * anim.value

            val start = if (shimmerDirection == ShimmerDirection.Horizontal) {
                Offset(animatedX, 0f)
            } else {
                Offset(0f, animatedX)
            }
            val end = if (shimmerDirection == ShimmerDirection.Horizontal) {
                Offset(animatedX + size.width * 0.5f, size.height)
            } else {
                Offset(size.width, animatedX + size.height * 0.5f)
            }
            Brush.linearGradient(colors, start = start, end = end)
        }

        ShimmerEffect.SeaWave -> {
            val waveCount = 5
            val totalWidth = size.width * (waveCount + 2) // Extra width for smooth looping
            val horizontalAnim = (anim.value * totalWidth) % (size.width * 2) // Repeatable horizontal scroll

            val brushList = mutableListOf<Brush>()
            for (i in 0 until waveCount) {
                val waveHorizontalOffset = horizontalAnim + (i * size.width / (waveCount - 1)) - (size.width)

                val waveVerticalOffset = sin(waveHorizontalOffset / size.width * 2 * Math.PI.toFloat()) * size.height * 0.1f

                brushList.add(
                    Brush.linearGradient(
                        colors = colors,
                        start = Offset(x = waveHorizontalOffset, y = size.height / 2 + waveVerticalOffset),
                        end = Offset(x = waveHorizontalOffset + size.width * 0.2f, y = size.height / 2 + waveVerticalOffset)
                    )
                )
            }


            val startX = -size.width * 1.5f
            val endX = size.width * 1.5f
            val animatedX = startX + (endX - startX) * anim.value

            Brush.linearGradient(
                colors,
                start = Offset(x = animatedX, y = 0f),
                end = Offset(x = animatedX + size.width, y = size.height)
            )
        }

        ShimmerEffect.Ventilator -> {
            // The original logic is solid; we are just applying the new colors and easing.
            val center = Offset(size.width / 2, size.height / 2)
            val radius = min(size.width, size.height) / 2
            val angle = anim.value * 2 * Math.PI.toFloat()

            val start = Offset(
                x = center.x + radius * 0.1f * cos(angle),
                y = center.y + radius * 0.1f * sin(angle)
            )
            val end = Offset(
                x = center.x + radius * cos(angle),
                y = center.y + radius * sin(angle)
            )
            Brush.linearGradient(
                colors = colors,
                start = start,
                end = end
            )
        }
    }
}