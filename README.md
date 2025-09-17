🎨 ComposeShimmer
A modern, highly customizable, and performant shimmer effect library for Jetpack Compose.

ComposeShimmer provides a set of beautiful, production-ready shimmer animations and composable layouts to enhance your loading states with minimal effort.

✨ Features
Multiple Shimmer Effects: Choose from a variety of built-in animations like Shimmer, PageSwipe, Ventilator, and SeaWave.

Fully Customizable: Easily adjust animation duration, colors, and direction.

Responsive Animations: Shimmer effects automatically adapt to the size of the composable they are applied to.

Seamless Integration: A simple ShimmerContainer manages loading state and content switching.

Light & Dark Theme Support: Built-in logic provides optimized colors for both light and dark themes.

⬇️ Installation
Add the following dependency to your module's build.gradle.kts file:
```Gradle
// build.gradle.kts (Module)
dependencies {
implementation("io.github.sachingurnaney:compose-shimmer:1.0.0")
}
```

🚀 Usage
The core of the library is the ShimmerContainer composable. It handles all the logic for displaying a shimmer placeholder while content is loading and then seamlessly transitioning to the real content.
```kotlin
@Composable
fun MyScreen() {
var isLoading by remember { mutableStateOf(true) }

    // Simulate loading data for 3 seconds
    LaunchedEffect(Unit) {
        delay(3000)
        isLoading = false
    }

    ShimmerContainer(
        isLoading = isLoading,
        shimmerEffect = ShimmerEffect.Shimmer,
        shimmerDirection = ShimmerDirection.Diagonal,
        modifier = Modifier.fillMaxSize(),
        shimmerContent = {
            // Your shimmer placeholder layout goes here
            // This composable has access to a `shimmerScope`
            MyShimmerLayout(shimmerScope = this)
        },
        content = {
            // Your real content goes here
            MyRealLayout()
        }
    )
}
```

🖼️ Example Layouts
You can use the provided layouts or create your own using ShimmerPlaceholder.

```kotlin
@Composable
fun MyShimmerLayout(shimmerScope: ShimmerScope) {
with(shimmerScope) {
Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
// A predefined shimmer layout
ShimmerArticle(shimmerScope = this)

            // A custom layout using the base placeholder
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ShimmerPlaceholder(
                    modifier = Modifier.size(64.dp),
                    brush = shimmerBrush,
                    shape = CircleShape
                )
                ShimmerPlaceholder(
                    modifier = Modifier.height(24.dp).weight(1f),
                    brush = shimmerBrush
                )
            }
        }
    }
}
```

🎨 Advanced Customization
For full control, you can provide your own Brush object to the ShimmerContainer. The container will then automatically wrap your brush in a ShimmerScope and provide it to your shimmer content.

```kotlin
@Composable
fun MyCustomShimmerScreen() {
val myCustomBrush = remember {
Brush.linearGradient(
colors = listOf(Color.Red, Color.Transparent, Color.Red)
)
}

    ShimmerContainer(
        isLoading = true,
        customBrush = myCustomBrush, // Pass your custom brush here (type is `Brush`)
        shimmerContent = {
            // All shimmer layouts within this block
            // will now use your custom brush
            MyCustomLayout(shimmerScope = this)
        },
        content = {
            // ...
        }
    )
}
```

🧑‍💻 Using ShimmerScope Directly (without Container)
For more advanced use cases, you can bypass the ShimmerContainer and use rememberShimmerScope directly within your own composable. This gives you full control over the animation and layout.

```kotlin
@Composable
fun MyShimmerScreenWithoutContainer() {
val createShimmerScope = rememberShimmerScope(
modifier = Modifier
.fillMaxSize()
.background(Color.White)
.padding(16.dp),
shimmerEffect = ShimmerEffect.Ventilator
)

    createShimmerScope {
        // Your shimmer layouts go inside this block
        MyShimmerLayout(shimmerScope = this)
    }
}
