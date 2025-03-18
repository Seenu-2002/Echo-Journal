package com.seenu.dev.android.echojournal.presentation.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
private fun PulsatingButtonPreview() {
    Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
        PulsatingButton(
            modifier = Modifier.size(48.dp),
            5,
            2000,
            Color.Blue,
            true,
        ) {
            IconButton(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.Red, shape = CircleShape),
                onClick = { /* Handle Click */ }) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun PulsatingButton(
    modifier: Modifier = Modifier,
    noOfCircles: Int = 3,
    duration: Int,
    rippleColor: Color,
    animate: Boolean,
    content: @Composable () -> Unit
) {
    Box(modifier, contentAlignment = Alignment.Center){
        if (animate) {
            val infiniteTransition = rememberInfiniteTransition("Pulsating animation")

            repeat(noOfCircles) {
                val progress by infiniteTransition.animateFloat(
                    initialValue = 0F,
                    targetValue = 1F,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = duration,
                            easing = LinearOutSlowInEasing
                        ),
                        initialStartOffset = StartOffset(
                            offsetMillis = (duration / noOfCircles) * it,
                            offsetType = StartOffsetType.Delay
                        ),
                        repeatMode = RepeatMode.Restart
                    )
                )

                Box(
                    modifier = modifier.then(
                        Modifier
                            .graphicsLayer {
                                val scale = 1 + progress
                                val alpha = 1 - progress
                                scaleX = scale
                                scaleY = scale
                                this.alpha = alpha
                            }
                            .background(color = rippleColor, shape = CircleShape)
                    ))
            }
        }

        content()
    }
}