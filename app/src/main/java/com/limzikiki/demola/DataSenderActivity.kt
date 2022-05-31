package com.limzikiki.demola

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import com.limzikiki.demola.ui.screen.DataSenderScreen
import com.limzikiki.demola.ui.theme.DemolaTheme
import java.io.IOException
import java.util.*


/*
val uuid: UUID = UUID.fromString("8989063a-c9af-463a-b3f1-f21d9b2b827b")


    class BluetoothServerController(activity: MainActivity) : Thread() {
        private var cancelled: Boolean
        private val serverSocket: BluetoothServerSocket?
        private val activity = activity

        init {
            val btAdapter = BluetoothAdapter.getDefaultAdapter()
            if (btAdapter != null) {
                this.serverSocket = btAdapter.listenUsingRfcommWithServiceRecord("test", uuid) // 1
                this.cancelled = false
            } else {
                this.serverSocket = null
                this.cancelled = true
            }}


    override fun run() {
        var socket: BluetoothSocket

        while(true) {
            if (this.cancelled) {
                break
            }

            try {
                socket = serverSocket!!.accept()  // 2
            } catch(e: IOException) {
                break
            }

            if (!this.cancelled && socket != null) {
                Log.i("server", "Connecting")
                BluetoothServerController(this.activity).start() // 3
            }
        }
    }

    fun cancel() {
        this.cancelled = true
        this.serverSocket!!.close()
    }
}
*/

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
