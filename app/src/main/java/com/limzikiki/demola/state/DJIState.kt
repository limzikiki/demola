package com.limzikiki.demola.state

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import com.limzikiki.demola.TransmittableData
import dji.common.error.DJIError
import dji.common.error.DJISDKError
import dji.common.util.CommonCallbacks
import dji.sdk.base.BaseComponent
import dji.sdk.base.BaseProduct
import dji.sdk.products.Aircraft
import dji.sdk.sdkmanager.BluetoothDevice
import dji.sdk.sdkmanager.DJISDKInitEvent
import dji.sdk.sdkmanager.DJISDKManager
import kotlinx.coroutines.*
import org.bouncycastle.jcajce.provider.symmetric.ARC4.Base
import timber.log.Timber
import java.sql.Types.NULL
import java.util.*
import kotlin.reflect.typeOf

@Stable
class DJIState(private val coroutine: CoroutineScope) {
    private var _loading = mutableStateOf(false)
    val loading: State<Boolean>
        get() = _loading

    private var _loadingState = mutableStateOf(0f)
    val loadingState: State<Float>
        get() = _loadingState

    private var _registered = mutableStateOf(false)
    val registered: State<Boolean>
        get() = _registered

    private var _connected = mutableStateOf(false)
    val connected: State<Boolean>
        get() = _connected

    private var _sending = mutableStateOf(false)
    val sending: State<Boolean>
        get() = _sending

    private var _lastSentData = mutableStateOf<String?>(null)
    val lastSentData: State<String?>
        get() = _lastSentData

    private var _message = mutableStateOf("")
    val message: State<String>
        get() = _message

    private var _product: Aircraft? = null
    private val product: Aircraft?
        get() {
            synchronized(this) {
                if (_product == null) {
                    val temp = DJISDKManager.getInstance().product
                    if(temp is Aircraft)
                        _product = temp
                    else
                        _message.value = "You have connected not a drone"
                    _connected.value = _product != null
                }
                return _product
            }
        }

    private var sendingJob: Job? = null
    private var dataToSend = TransmittableData()
    init {
        _connected.value = product?.isConnected == true

        if (connected.value) {
            _registered.value = true
            _loading.value = false
        }
    }

    private var bluetoothAdapter:BluetoothAdapter? = null

    /**
     * @return false if sdk wasn't attempted to start because of lack of the permissions.
     * */
    fun registerSDKListener(ctx: Context): Boolean {

        val permissionCheck = ContextCompat.checkSelfPermission(
            ctx,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val permissionCheck2 = ContextCompat.checkSelfPermission(
            ctx,
            Manifest.permission.READ_PHONE_STATE
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED ||
            permissionCheck2 != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        Timber.i("Attempt to register app")
        val reg = DJISDKManager.getInstance().hasSDKRegistered()
        if (!reg)
            coroutine.launch {
                Toast.makeText(ctx, "Registering DJI", Toast.LENGTH_SHORT).show()
                try {
                    DJISDKManager.getInstance().registerApp(ctx, DJISDKManagerCallback)
                } catch (error: Exception) {
                    Timber.e(error)
                }
            }
        else {
            _loading.value = false
            _registered.value = true
        }
        _loading.value = true
        _connected.value = false
        _registered.value = false
        return true
    }

    fun checkConnection(ctx: Context) {
        if (product?.isConnected == true) {
            Toast.makeText(ctx, "Product is connected", Toast.LENGTH_SHORT).show()
        } else {
            if (product == null) {
                Toast.makeText(ctx, "Product is null", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(ctx, "Product is not connected", Toast.LENGTH_SHORT).show()
            }
        }
    }



    /**
     * When sending fails error message will be in [DJIState.message]
     */
    fun startSendingData(ctx: Context) {
        if(!canConnect(ctx)){
            return
        }
        _sending.value = true
        sendingJob = coroutine.launch {
            initSending(ctx)
        }
    }

    fun stopSending(){
        _sending.value = false
        sendingJob?.cancel()
        sendingJob = null
    }

    /**
     * Prepares required data for sending.
     */
    private suspend fun initSending(ctx: Context) {
        while(true){
            delay(1000)
            updateData()
            _lastSentData.value = dataToSend.toString()
            sendData(ctx)
        }
    }

    private fun updateData(){
        val tempProd = product ?: return;
        tempProd.flightController.run {
            dataToSend.altitude = state.aircraftLocation.altitude
            dataToSend.flightTime = state.flightTimeInSeconds
        }
    }


    private fun discoverDevices(ctx: Context){
        val BTManager:BluetoothManager = ctx.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = BTManager.adapter

        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        pairedDevices?.forEach { device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address // MAC address
        }
    }

    /**
     * Отправляет дату в формате [TransmittableData] беря ее из [dataToSend] и отправляет ее на другой телефон
     */
    private fun sendData(ctx: Context) {
        TODO("Arsenii need to implement")
    }

    /**
     * Проверяет подключен ли телефон и можно ли с ним обмениваться информацией. Если нет, то информация появиться в [_message]
     */
    private fun canConnect(ctx:Context):Boolean {
        _message.value = "Reason why can't connect"

        TODO("Arsenii need to implement")
    }

    val DJISDKManagerCallback = object : DJISDKManager.SDKManagerCallback {
        override fun onRegister(error: DJIError?) {
            when (error) {
                DJISDKError.REGISTRATION_SUCCESS -> {
                    Timber.i("DJI SDK was registered successfully")

                    with(this@DJIState) {
                        _loading.value = false
                        _registered.value = true
                    }
                    // Start connection to the drone
                    DJISDKManager.getInstance().startConnectionToProduct()
                }
                else -> {
                    Timber.e("Unknown error ${error?.errorCode ?: "NULL"}: ${error?.description ?: "NULL"} ")
                    with(this@DJIState) {
                        _loading.value = false
                        _registered.value = false
                    }
                }
            }
        }

        override fun onProductDisconnect() {
            Timber.e("Product got disconnected")
        }

        override fun onProductConnect(product1: BaseProduct?) {
            Timber.i("Product got connected")

            product?.battery?.setStateCallback {
                dataToSend.batteryLevel = it.chargeRemainingInPercent
            }
        }

        override fun onProductChanged(product: BaseProduct?) {
            Timber.i("Product got changed")
        }

        override fun onComponentChange(
            componentKey: BaseProduct.ComponentKey?,
            oldComponent: BaseComponent?,
            newComponent: BaseComponent?
        ) {
            Timber.i("Product component ${componentKey?.name} got changed")
        }

        override fun onInitProcess(Event: DJISDKInitEvent?, totalResource: Int) {
            Timber.i("Initialization of the process")
            this@DJIState._loadingState.value = totalResource / 100f
        }

        override fun onDatabaseDownloadProgress(current: Long, total: Long) {
            if (((current / total) * 100L) % 10L == 0L)
                Timber.i("Flight Map database downloading ${(current / total) * 100}")
        }

    }

}


@Composable
fun rememberDJIState(coroutine: CoroutineScope = rememberCoroutineScope()) = remember(coroutine) {
    DJIState(coroutine)
}