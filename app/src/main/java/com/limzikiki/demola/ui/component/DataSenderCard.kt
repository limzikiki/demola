package com.limzikiki.demola.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.limzikiki.demola.state.DJIState
import com.limzikiki.demola.ui.theme.DemolaTheme

@Composable
fun DataSenderCard(state: DJIState?) {
    var isOk by remember { mutableStateOf(false) }
    isOk = state != null
    state?.let {
        isOk = it.registered.value
        if (!isOk) {
            isOk = it.connected.value
        }
    }
    Wrap(!isOk) {
        if (!isOk) {
            ErrorDataSenderCard()
        } else {
            RawDataSenderCard(state = state)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Wrap(errorStyle: Boolean = false, child: @Composable ColumnScope.() -> Unit) {
    val cardColors =
        CardDefaults.cardColors(containerColor = if (errorStyle) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.background)
    Card(
        modifier = Modifier
            .padding(4.dp),
        colors = cardColors
    ) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .defaultMinSize(minHeight = 100.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            content = child
        )
    }
}

@Composable
private fun ErrorDataSenderCard() {
    Text("Can't use sender without working connection to the drone")
}

@Composable
private fun RawDataSenderCard(state: DJIState?) {
    val isSending by state?.sending ?: remember {
        mutableStateOf(true)
    }
    if (!isSending) {
        Button(onClick = { state?.startSendingData() }) {
            Text("Start Sending data")
        }
    } else {
        Text(
            state?.lastSentData?.value ?: "Nothing was sent yet",
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
@Preview
private fun PreviewDataSenderCard() {
    DemolaTheme {
        Wrap {
            RawDataSenderCard(null)
        }
    }
}