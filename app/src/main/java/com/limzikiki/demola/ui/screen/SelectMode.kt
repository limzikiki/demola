package com.limzikiki.demola.ui.screen

import android.content.Context
import android.content.Intent
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
import com.limzikiki.demola.DataReceiverActivity
import com.limzikiki.demola.DataSenderActivity

fun navigateToReceiver(ctx: Context) {
    TODO()
}

fun startSender(ctx: Context) {
    val intent = Intent(ctx, DataSenderActivity::class.java)
    ctx.startActivity(intent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectMode() {
    val ctx = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Card(modifier = Modifier
            .fillMaxHeight()
            .weight(1f)
            .fillMaxWidth(1f)
            .padding(4.dp)
            .clickable { navigateToReceiver(ctx) }
            ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Start Receiving Data")
            }
        }
        Spacer(modifier = Modifier.width(4.dp))
        Card(modifier = Modifier
            .fillMaxHeight()
            .weight(1f)
            .fillMaxWidth(1f)
            .padding(4.dp)
            .clickable { startSender(ctx) }
           ) {
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