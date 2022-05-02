package com.limzikiki.demola.ui.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.limzikiki.demola.ui.theme.DemolaTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

fun navigateToReceiver(ctx: Context) {
    TODO()
}

fun startSender(ctx: Context) {
    // TODO:
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectMode() {
    val ctx = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Card(modifier = Modifier
            .fillMaxHeight()
            .width(180.dp)
            .clickable { navigateToReceiver(ctx) }
            .padding(4.dp)) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Data Receiver")
            }
        }
        Spacer(modifier = Modifier.width(4.dp))
        Card(modifier = Modifier
            .fillMaxHeight()
            .width(180.dp)
            .clickable { startSender(ctx) }
            .padding(4.dp)) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Start Sending Data")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectModePreview() {
    DemolaTheme {
        SelectMode()
    }
}