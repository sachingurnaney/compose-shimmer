package io.github.sachingurnaney.composeshimmer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.sachingurnaney.shimmers.ShimmerArticle
import io.github.sachingurnaney.shimmers.ShimmerContainer
import io.github.sachingurnaney.shimmers.ShimmerDirection
import io.github.sachingurnaney.shimmers.ShimmerEffect
import io.github.sachingurnaney.shimmers.ShimmerGrid
import io.github.sachingurnaney.shimmers.ShimmerLazyGrid
import io.github.sachingurnaney.shimmers.ShimmerLazyList
import io.github.sachingurnaney.shimmers.ShimmerList
import io.github.sachingurnaney.shimmers.ShimmerProfile
import io.github.sachingurnaney.shimmers.ShimmerScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShimmerDemoScreen() {
    var isLoading by remember { mutableStateOf(true) }
    val mainListState = rememberLazyListState()

    var shimmerEffect by remember { mutableStateOf(ShimmerEffect.Shimmer) }
    var shimmerDirection by remember { mutableStateOf(ShimmerDirection.Diagonal) }
    val isShimmerActive = remember { derivedStateOf { isLoading } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shimmer UI Demo") },
                actions = {
                    Button(
                        onClick = { isLoading = !isLoading },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(if (isShimmerActive.value) "Stop Loading" else "Start Loading")
                    }

                    var expandedEffect by remember { mutableStateOf(false) }
                    Box {
                        TextButton(onClick = { expandedEffect = true }) {
                            Text(shimmerEffect.name)
                        }
                        DropdownMenu(
                            expanded = expandedEffect,
                            onDismissRequest = { expandedEffect = false }
                        ) {
                            ShimmerEffect.values().forEach { effect ->
                                DropdownMenuItem(
                                    text = { Text(effect.name) },
                                    onClick = {
                                        shimmerEffect = effect
                                        expandedEffect = false
                                    }
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { padding ->
        ShimmerContainer (
            isLoading = isLoading,
            shimmerEffect = shimmerEffect,
            shimmerDirection = shimmerDirection,
            modifier = Modifier.padding(padding),
            shimmerContent = {
                ShimmerDemoPlayground(
                    shimmerScope = this,
                    listState = mainListState
                )
            },
            content = {
                RealContent(listState = mainListState)
            }
        )
    }
}

@Composable
fun RealContent(listState: LazyListState) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item { Text("📰 Real Articles", style = MaterialTheme.typography.titleLarge) }
        items(5) { index ->
            Card(
                modifier = Modifier.fillMaxWidth().height(180.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Article #$index", style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        item { Text("📄 Real List", style = MaterialTheme.typography.titleLarge) }
        items(5) { index ->
            Row(
                modifier = Modifier.fillMaxWidth().height(50.dp).padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("List Item #$index", style = MaterialTheme.typography.bodyMedium)
            }
        }

        item { Text("👤 Real Profiles", style = MaterialTheme.typography.titleLarge) }
        items(5) { index ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(64.dp).background(MaterialTheme.colorScheme.secondary, CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Profile #$index", style = MaterialTheme.typography.titleMedium)
                    Text("Subtitle #$index", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        item { Text("🖼 Real Grid", style = MaterialTheme.typography.titleLarge) }
        item {
            val realGridState = rememberLazyGridState()
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = realGridState,
                modifier = Modifier.height(300.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(6) { index ->
                    Box(
                        modifier = Modifier.height(120.dp).fillMaxWidth().background(MaterialTheme.colorScheme.tertiaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Grid Item #$index")
                    }
                }
            }
        }
    }
}

@Composable
fun ShimmerDemoPlayground(
    shimmerScope: ShimmerScope,
    listState: LazyListState
) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Shimmer Article
        item { Text("📰 Shimmer Article", style = MaterialTheme.typography.titleLarge) }
        item {
            ShimmerArticle(shimmerScope = shimmerScope, imageHeight = 180.dp)
        }

        // Shimmer List
        item { Text("📄 Shimmer List", style = MaterialTheme.typography.titleLarge) }
        item {
            ShimmerList(shimmerScope = shimmerScope, itemCount = 5, itemHeight = 50.dp)
        }

        // Shimmer Profile
        item { Text("👤 Shimmer Profile", style = MaterialTheme.typography.titleLarge) }
        items(5) {
            ShimmerProfile(shimmerScope = shimmerScope, avatarSize = 64.dp)
        }

        // Shimmer Grid
        item { Text("🖼 Shimmer Grid", style = MaterialTheme.typography.titleLarge) }
        item {
            Box(modifier = Modifier.height(300.dp)) {
                ShimmerGrid(
                    shimmerScope = shimmerScope,
                    itemCount = 6,
                    columns = 2,
                    itemHeight = 100.dp
                )
            }
        }
        item { Text("🖼 Shimmer Lazy List", style = MaterialTheme.typography.titleLarge) }
        item {
            Box(modifier = Modifier.height(250.dp)) {
                ShimmerLazyList(
                    shimmerScope = shimmerScope,
                    itemCount = 10,
                    itemHeight = 50.dp
                )
            }
        }

        item { Text("🖼 Shimmer Lazy Grid", style = MaterialTheme.typography.titleLarge) }
        item {
            Box(modifier = Modifier.height(300.dp)) {
                ShimmerLazyGrid(
                    shimmerScope = shimmerScope,
                    itemCount = 6,
                    columns = 2,
                    itemHeight = 50.dp
                )
            }
        }
    }
}

/* --------------------------------------------------------------------------
   🔹 Light/Dark Preview
-------------------------------------------------------------------------- */

@Preview(showBackground = true)
@Composable
fun ShimmerDemoScreenPreviewLight() {
    MaterialTheme {
        ShimmerDemoScreen()
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ShimmerDemoScreenPreviewDark() {
    MaterialTheme {
        ShimmerDemoScreen()
    }
}
