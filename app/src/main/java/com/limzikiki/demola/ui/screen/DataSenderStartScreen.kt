package com.limzikiki.demola.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DataSenderStartScreen(){
    val permissions = rememberMultiplePermissionsState(permissions = Permissions.DJI)
    if(!permissions.allPermissionsGranted){
        RequestPermissionsScreen(permissions)
        return
    }

    val coroutine = rememberCoroutineScope()
    var isLoading by remember{ mutableStateOf(true) }
    var isConnected by remember { mutableStateOf(false) }
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (isLoading){
            LoadingScreen()
        }else{
            if(!isConnected){
                ErrorScreen(text = "Place holder") {
                    coroutine.launch {  }
                }
            }else{
                TODO()
            }
        }
    }
}