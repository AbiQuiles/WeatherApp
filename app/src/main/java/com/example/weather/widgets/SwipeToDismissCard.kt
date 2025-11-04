package com.example.weather.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Image
import androidx.compose.material.icons.twotone.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SwipeToDismissCard(
    frontContent: @Composable () -> Unit,
    leadingHiddenContent: @Composable (state: SwipeToDismissBoxState) -> Unit,
    onLeadingSwipe: () -> Unit,
    trailingHiddenContent: @Composable (state: SwipeToDismissBoxState) -> Unit,
    onTrailingSwipe: () -> Unit,
) {

    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = { direction ->
            when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    onLeadingSwipe()
                    false
                }

                SwipeToDismissBoxValue.EndToStart -> {
                    onTrailingSwipe()
                    false
                }

                else -> false
            }
        }
    )
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        SwipeToDismissBox(
            modifier = Modifier.fillMaxWidth(),
            state = swipeToDismissBoxState,
            enableDismissFromStartToEnd = true,
            enableDismissFromEndToStart = true,
            backgroundContent = {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    when (swipeToDismissBoxState.dismissDirection) {

                        SwipeToDismissBoxValue.StartToEnd -> {
                            leadingHiddenContent(swipeToDismissBoxState)
                        }

                        SwipeToDismissBoxValue.EndToStart -> {
                            trailingHiddenContent(swipeToDismissBoxState)
                        }

                        else -> { /* Do nothing when settled */
                        }
                    }
                }
            },
            content = {
                Row(modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)) {
                    frontContent()
                }
            }
        )
    }
}

@Composable
fun SwipeToDismissCardIcon(
    swipeToDismissBoxState: SwipeToDismissBoxState,
    iconImage: ImageVector = Icons.TwoTone.Image,
    iconDescription: String = "Icon Description",
    iconTint: Color = Color.White,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
) {
    val alignment = when (swipeToDismissBoxState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
        SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
        else -> Alignment.Center
    }

    Box(
        contentAlignment = alignment,
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
    ) {
        Icon(
            imageVector = iconImage,
            contentDescription = iconDescription,
            modifier = Modifier
                .padding(12.dp)
                .size(33.dp),
            tint = iconTint,
        )
    }
}

@Preview
@Composable
fun SwipeToDismissCardPreview() {
    SwipeToDismissCard(
        frontContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(70.dp)
            ) {
                Text("This is the body of the card. For testing purposes.")
            }
        },
        leadingHiddenContent = { swipeState ->
            SwipeToDismissCardIcon(
                swipeToDismissBoxState = swipeState,
                iconImage = Icons.TwoTone.Refresh,
                iconDescription = "Refresh item",
                backgroundColor = Color(0xF7FFB300),
            )
        },
        onTrailingSwipe = {},
        trailingHiddenContent = { swipeState ->
            SwipeToDismissCardIcon(
                swipeToDismissBoxState = swipeState,
                iconImage = Icons.TwoTone.Delete,
                iconDescription = "Remove item",
                backgroundColor = MaterialTheme.colorScheme.error,
            )
        },
        onLeadingSwipe = {}
    )
}