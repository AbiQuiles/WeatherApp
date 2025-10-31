package com.example.weather.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Image
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun SwipeRevealCard(
    frontContent: @Composable () -> Unit,
    hiddenContent: @Composable (onHideContent: () -> Unit) -> Unit
) {
    var frontContentSize by remember { mutableFloatStateOf(50f) }
    val swipeableState = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { frontContentSize }
    val anchors = mapOf(0f to 0, -sizePx to 1)
    val coroutineScope = rememberCoroutineScope()
    val hideAction = {
        coroutineScope.launch {
            swipeableState.animateTo(0)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(swipeableState.currentValue) {
                if (swipeableState.currentValue == 1) {
                    awaitPointerEventScope {
                        hideAction()
                    }
                }
            }
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
                    frontContentSize = it.width.toFloat()
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
    Column {
        SwipeRevealCard(
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

        SwipeRevealCard(
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