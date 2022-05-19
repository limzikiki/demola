package com.limzikiki.demola.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.limzikiki.demola.state.rememberDJIState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DataSenderStartScreen(){
    val permissions = rememberMultiplePermissionsState(permissions = Permissions.DJI)
    if(!permissions.allPermissionsGranted){
        RequestPermissionsScreen(permissions)
        return
    }

    val djiState = rememberDJIState()
    val loading by djiState.loading
    val registered by djiState.registered
    val ctx = LocalContext.current

    LaunchedEffect(true) {
        if (!loading && !registered){
            djiState.registerSDKListener(ctx)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (djiState.loading.value){
            LoadingScreen()
        }else{
            if(!registered){
                ErrorScreen(text = "Place holder") {
                    djiState.registerSDKListener(ctx)
                }
            }else{
                Text("Registered")
            }
        }
    }
}