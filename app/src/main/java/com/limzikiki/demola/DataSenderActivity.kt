package com.limzikiki.demola

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.limzikiki.demola.ui.screen.DataSenderScreen
import com.limzikiki.demola.ui.theme.DemolaTheme


class DataSenderActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DemolaTheme {
                DataSenderScreen()
            }
        }
    }
}
