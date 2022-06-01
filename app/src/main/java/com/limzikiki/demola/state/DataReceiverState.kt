package com.limzikiki.demola.state

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.limzikiki.demola.util.getActivity
import kotlinx.coroutines.CoroutineScope


@Stable
class DataReceiverState(private val coroutine: CoroutineScope) {
    private var _receiving = mutableStateOf(false)
    val receiving: State<Boolean>
        get() = _receiving

    private var _message = mutableStateOf("No error")
    val message: State<String>
        get() = _message

    private var _connected = mutableStateOf(false)
    val connected: State<Boolean>
        get() = _receiving

    private var _batteryLevel = mutableStateOf(57)
    val batteryLevel: State<Int>
        get() = _batteryLevel

    private var _altitude = mutableStateOf(10f)
    val altitude: State<Float>
        get() = _altitude

    private var _flightTime = mutableStateOf(300)
    val flightTime: State<Int>
        get() = _flightTime

    private var bluetoothManager: BluetoothManager? = null
    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        bluetoothManager?.adapter
    }

    fun startReceiving(ctx: Context) {

        // To mark this device as ready to receive
        _receiving.value = true
        //TODO("Arsenii implement")
    }


    private fun connect(ctx: Context) {
        bluetoothManager = ctx.getSystemService(BluetoothManager::class.java)

        val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
            putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
        }

        val activity = ctx.getActivity()
        if(activity == null){
            _message.value = "Can't connect to the activity"
            return
        }

        val resultLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    // TODO
                }

            }
        resultLauncher.launch(discoverableIntent)
    }

}



@Composable
fun rememberDataReceiverState(coroutine: CoroutineScope = rememberCoroutineScope()) =
    remember(coroutine) {
        DataReceiverState(coroutine)
    }