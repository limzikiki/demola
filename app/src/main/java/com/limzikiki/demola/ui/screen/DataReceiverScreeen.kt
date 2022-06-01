package com.limzikiki.demola.ui.screen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.limzikiki.demola.state.*
import com.limzikiki.demola.ui.theme.DemolaTheme


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DataReceiverScreen() {
    val permissions = rememberMultiplePermissionsState(permissions = Permissions.receiver)
    if (!permissions.allPermissionsGranted) {
        RequestPermissionsScreen(permissions)
        return
    }
    val state = rememberDataReceiverState()
    val ctx = LocalContext.current
    Surface {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!state.receiving.value) {
                Button({ state.startReceiving(ctx) }) {
                    Text("Start receiving data")
                }
            } else {
                DataReceiverDisplay(state)
            }
        }
    }
}

@Composable
private fun DataReceiverDisplay(state: DataReceiverState) {
    val systemUi = rememberSystemUiController()
    val textColor = Color.White
    var isReflected by remember { mutableStateOf(false) }
    var textSize by remember {
        mutableStateOf(14f)
    }
    val scale by animateFloatAsState(
        targetValue = if (isReflected) -1f else 1f,
        animationSpec = tween(600, 10, FastOutSlowInEasing)
    )

    SideEffect {
        systemUi.isSystemBarsVisible = false
    }


    Column(
        Modifier
            .fillMaxSize()
            .scale(scale, 1f)
            .background(Color.Black)
            .scrollable(
                orientation = Orientation.Vertical, state = rememberScrollableState{ delta->
                    textSize -= delta/4f
                    if(textSize < 14f) textSize = 14f
                    delta
                }
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        isReflected = !isReflected
                    }
                )
            },
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Battery: " + state.batteryLevel.value + "%", color = textColor, fontSize = textSize.sp)
            Text("Altitude: " + state.altitude.value + " m", color = textColor, fontSize = textSize.sp)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Time: " + state.flightTime.value / 60 + " min", color = textColor, fontSize = textSize.sp)
        }
    }
}

@Composable
@Preview
private fun PreviewDataReceiverDisplay() {
    DemolaTheme() {
        Surface {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DataReceiverDisplay(rememberDataReceiverState())
            }
        }
    }
}

@Composable
@Preview
private fun PreviewDataReceiverScreen() {
    DemolaTheme {
        DataReceiverScreen()
    }
}