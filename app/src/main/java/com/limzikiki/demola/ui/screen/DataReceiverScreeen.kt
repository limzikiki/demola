package com.limzikiki.demola.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.limzikiki.demola.state.*
import com.limzikiki.demola.ui.theme.DemolaTheme


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DataReceiverScreen() {
    val permissions = rememberMultiplePermissionsState(permissions = Permissions.receiver)
    if(!permissions.allPermissionsGranted){
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
    val textColor = Color.White
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Battery level: " + state.batteryLevel.value, color = textColor)
            Text("Altitude: " + state.altitude.value, color = textColor)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Flight Time: " + state.flightTime.value / 60 + " min", color = textColor)
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