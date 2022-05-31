package com.limzikiki.demola.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.limzikiki.demola.state.rememberDJIState
import com.limzikiki.demola.ui.component.DataSenderCard

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DataSenderScreen(){
    val permissions = rememberMultiplePermissionsState(permissions = Permissions.sender)
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
                Column{
                    Text("Registered")
                    if (djiState.message.value.isNotEmpty()){
                        Text(djiState.message.value)
                    }
                    if(! djiState.connected.value){
                        Text("Not connected")
                        Button(onClick = { djiState.checkConnection(ctx) }) {
                            Text("Check product")
                        }
                    }else{
                        Text("Connected")
                        DataSenderCard(state = djiState)
                    }

                }

            }
        }
    }
}

