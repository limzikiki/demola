package com.limzikiki.demola.ui.screen

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissionsScreen(state: MultiplePermissionsState = rememberMultiplePermissionsState(
    permissions = listOf()
)){
    Column(
        Modifier
            .padding(4.dp)
            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
        Text("It's required to have some permissions that you didn't grant yet", color = MaterialTheme.colorScheme.primary)
        Column {
            for(permission in state.revokedPermissions){
                Text(permission.permission, style=MaterialTheme.typography.titleMedium)
            }
        }
        Button(onClick = {state.launchMultiplePermissionRequest() }) {
            Text(text = "Grant")
        }
    }
}

object Permissions{
    val DJI: List<String> = arrayListOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.VIBRATE,
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE,
    )
}