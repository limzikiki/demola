package com.limzikiki.demola.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.limzikiki.demola.ui.theme.DemolaTheme

@Composable
fun ErrorScreen(text: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text, color = Color.Red)
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Preview
@Composable
fun ErrorScreenPreview() {
    DemolaTheme {
        ErrorScreen(text = "Error Message Example") {}
    }
}