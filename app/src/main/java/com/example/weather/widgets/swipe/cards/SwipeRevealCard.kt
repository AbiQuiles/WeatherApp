package com.example.weather.widgets.swipe.cards

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Image
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.SwipeableState
import androidx.wear.compose.material.swipeable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalWearMaterialApi::class)
@Stable
class SwipeRevealCardManager {
    private val states = mutableMapOf<Int, SwipeableState<Int>>()
    private var revealedCardKey: Int? = null
    private var keyCounter = 0

    @Composable
    fun rememberSwipeableState(): Pair<Int, SwipeableState<Int>> {
        val key = remember { keyCounter++ }
        val state = states.getOrPut(key) {
            SwipeableState(0)
        }
        return key to state
    }

    fun notifyRevealed(key: Int, coroutineScope: CoroutineScope) {
        if (revealedCardKey != null && revealedCardKey != key) {
            states[revealedCardKey]?.let { oldState ->
                coroutineScope.launch {
                    oldState.animateTo(
                        targetValue = 0,
                        anim = tween(durationMillis = 150)
                    )
                }
            }
        }
        revealedCardKey = key
    }
}

@Composable
fun rememberSwipeRevealCardManager(): SwipeRevealCardManager {
    return remember { SwipeRevealCardManager() }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun SwipeRevealCard(
    manager: SwipeRevealCardManager,
    frontContent: @Composable () -> Unit,
    hiddenContent: @Composable (onHideContent: () -> Unit) -> Unit
) {
    var hiddenContentWidth by remember { mutableFloatStateOf(50f) }
    val coroutineScope = rememberCoroutineScope()

    // Get the unique key and state for this card from the manager
    val (key, swipeableState) = manager.rememberSwipeableState()

    // Notify the manager when this card is revealed
    LaunchedEffect(swipeableState.currentValue) {
        if (swipeableState.currentValue == 1) { // 1 means revealed
            manager.notifyRevealed(key, coroutineScope)
        }
    }

    val sizePx = with(LocalDensity.current) { hiddenContentWidth }
    val anchors = mapOf(0f to 0, -sizePx to 1) // State 0 = un-swiped, State 1 = swiped

    val hideAction = {
        coroutineScope.launch {
            swipeableState.animateTo(
                targetValue = 0,
                anim = tween(durationMillis = 300)
            )
        }
    }

    println("Rez ${swipeableState.currentValue}")
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal,
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .onSizeChanged {
                    hiddenContentWidth = it.width.toFloat()
                }
                .padding(horizontal = 8.dp)
        ) {
            hiddenContent {
                hideAction()
            }
        }
        Card(
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .offset {
                    IntOffset(swipeableState.offset.value.roundToInt(), 0)
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            if (swipeableState.currentValue == 1) {
                                hideAction()
                            }
                        }
                    )
                }
        ) {
            frontContent()
        }
    }
}

@Composable
fun SwipeRevealCardButton(
    iconImage: ImageVector = Icons.TwoTone.Image,
    iconDescription: String = "Icon Description",
    iconTint: Color = Color.White,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    onClick: () -> Unit
) {
    IconButton(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .size(55.dp)
            .background(
                color = backgroundColor,
                shape = MaterialTheme.shapes.medium
            )
    ) {
        Icon(
            imageVector = iconImage,
            contentDescription = iconDescription,
            modifier = Modifier.size(30.dp),
            tint = iconTint
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SwipeRevealCardPreview() {
    val manager = rememberSwipeRevealCardManager()

    LazyColumn {
        items(2) {
            SwipeRevealCard(
                manager = manager,
                frontContent = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(70.dp)
                    ) {
                        Text("wohwodhwohwo")
                    }
                },
                hiddenContent = { hideContent ->
                    SwipeRevealCardButton(
                        backgroundColor = Color.Red
                    ) {
                        hideContent()
                    }
                    SwipeRevealCardButton(
                        backgroundColor = Color.Green
                    ) {
                        hideContent()
                    }
                    SwipeRevealCardButton(
                        backgroundColor = Color.Blue
                    ) {
                        hideContent()
                    }
                }
            )
        }
    }
}