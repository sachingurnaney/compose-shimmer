package io.github.sachingurnaney.shimmers

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

/**
 * Interface to provide a single animated brush to child composables.
 */
interface ShimmerScope {
    val shimmerBrush: Brush
}

/**
 * Creates and provides a responsive, animated brush to a composable scope.
 */
@Composable
fun rememberShimmerScope(
    modifier: Modifier,
    shimmerEffect: ShimmerEffect = ShimmerEffect.Shimmer,
    shimmerDirection: ShimmerDirection = ShimmerDirection.Diagonal,
    durationMillis: Int = 1200,
    shimmerColors: List<Color>? = null
): @Composable (content: @Composable ShimmerScope.() -> Unit) -> Unit {
    val sizeState = remember { mutableStateOf(Size.Zero) }
    val brush = rememberShimmerBrush(
        effect = shimmerEffect,
        shimmerDirection = shimmerDirection,
        durationMillis = durationMillis,
        shimmerColors = shimmerColors,
        size = sizeState.value
    )

    val scope = remember(brush) {
        object : ShimmerScope {
            override val shimmerBrush: Brush = brush
        }
    }

    return { content ->
        Box(
            modifier = modifier.onSizeChanged {
                sizeState.value = it.toSize()
            }
        ) {
            scope.content()
        }
    }
}

/**
 * A reusable placeholder Box with a shimmer background.
 */
@Composable
fun ShimmerPlaceholder(
    modifier: Modifier = Modifier,
    brush: Brush,
    shape: Shape = RoundedCornerShape(8.dp)
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(brush)
    )
}

/* --------------------------------------------------------------------------
   🔹 Example Shimmer Layouts
-------------------------------------------------------------------------- */

/** ✅ Article Shimmer (image + 2 text lines) */
@Composable
fun ShimmerArticle(
    shimmerScope: ShimmerScope,
    imageHeight: Dp = 180.dp,
    titleWidthFraction: Float = 0.7f,
    subtitleWidthFraction: Float = 0.5f
) = with(shimmerScope) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ShimmerPlaceholder(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight),
            brush = shimmerBrush,
            shape = RoundedCornerShape(12.dp)
        )
        ShimmerPlaceholder(
            modifier = Modifier
                .fillMaxWidth(titleWidthFraction)
                .height(20.dp),
            brush = shimmerBrush
        )
        ShimmerPlaceholder(
            modifier = Modifier
                .fillMaxWidth(subtitleWidthFraction)
                .height(16.dp),
            brush = shimmerBrush
        )
    }
}

/** ✅ List Item Shimmer  */
@Composable
fun ShimmerListItem(
    shimmerScope: ShimmerScope,
    height: Dp = 80.dp
) = with(shimmerScope) {
    ShimmerPlaceholder(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        brush = shimmerBrush,
        shape = RoundedCornerShape(12.dp)
    )
}

/** ✅ List Shimmer (static)  */
@Composable
fun ShimmerList(
    shimmerScope: ShimmerScope,
    itemCount: Int = 5,
    itemHeight: Dp = 100.dp
) = with(shimmerScope) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        repeat(itemCount) {
            ShimmerPlaceholder(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeight),
                brush = shimmerBrush,
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

/** ✅ Profile Shimmer (avatar + 2 text lines) */
@Composable
fun ShimmerProfile(
    shimmerScope: ShimmerScope,
    avatarSize: Dp = 64.dp
) = with(shimmerScope) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ShimmerPlaceholder(
            modifier = Modifier.size(avatarSize),
            brush = shimmerBrush,
            shape = CircleShape
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            ShimmerPlaceholder(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(20.dp),
                brush = shimmerBrush
            )
            ShimmerPlaceholder(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(16.dp),
                brush = shimmerBrush
            )
        }
    }
}

/** ✅ Grid Shimmer */
@Composable
fun ShimmerGrid(
    shimmerScope: ShimmerScope,
    itemCount: Int = 6,
    columns: Int = 2,
    itemHeight: Dp = 120.dp
) = with(shimmerScope) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        repeat((itemCount + columns - 1) / columns) { rowIndex ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                repeat(columns) { columnIndex ->
                    val index = rowIndex * columns + columnIndex
                    if (index < itemCount) {
                        ShimmerPlaceholder(
                            modifier = Modifier
                                .weight(1f)
                                .height(itemHeight),
                            brush = shimmerBrush,
                            shape = RoundedCornerShape(12.dp)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

/* --------------------------------------------------------------------------
   🔹 Lazy Variants
-------------------------------------------------------------------------- */

@Composable
fun ShimmerLazyList(
    shimmerScope: ShimmerScope,
    itemCount: Int,
    listState: LazyListState = rememberLazyListState(),
    itemHeight: Dp = 50.dp
) = with(shimmerScope) {
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(itemCount, key = { it }) {
            ShimmerPlaceholder(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeight),
                brush = shimmerBrush,
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@Composable
fun ShimmerLazyGrid(
    shimmerScope: ShimmerScope,
    itemCount: Int,
    columns: Int,
    gridState: LazyGridState = rememberLazyGridState(),
    itemHeight: Dp = 120.dp
) = with(shimmerScope) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        state = gridState,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(itemCount, key = { it }) {
            ShimmerPlaceholder(
                modifier = Modifier
                    .height(itemHeight)
                    .fillMaxWidth(),
                brush = shimmerBrush,
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

/* --------------------------------------------------------------------------
   🔹 ShimmerContainer
-------------------------------------------------------------------------- */

/**
 * Wraps shimmer + real content and handles switching automatically.
 */
@Composable
fun ShimmerContainer(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    shimmerEffect: ShimmerEffect = ShimmerEffect.Shimmer,
    shimmerDirection: ShimmerDirection = ShimmerDirection.Diagonal,
    durationMillis: Int = 1200,
    shimmerColors: List<Color>? = null,
    shimmerContent: @Composable ShimmerScope.() -> Unit,
    content: @Composable () -> Unit
) {
    val createShimmerScope = rememberShimmerScope(
        modifier = modifier,
        shimmerEffect = shimmerEffect,
        shimmerDirection = shimmerDirection,
        durationMillis = durationMillis,
        shimmerColors = shimmerColors
    )

    Crossfade(targetState = isLoading, label = "shimmerCrossfade") { loading ->
        if (loading) {
            createShimmerScope {
                shimmerContent()
            }
        } else {
            content()
        }
    }
}