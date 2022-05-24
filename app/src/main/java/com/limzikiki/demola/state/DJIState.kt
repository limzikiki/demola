package com.limzikiki.demola.state

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import dji.common.error.DJIError
import dji.common.error.DJISDKError
import dji.common.util.CommonCallbacks
import dji.sdk.base.BaseComponent
import dji.sdk.base.BaseProduct
import dji.sdk.sdkmanager.DJISDKInitEvent
import dji.sdk.sdkmanager.DJISDKManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
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

    private var _product: BaseProduct? = null
    private val product: BaseProduct?
        get() {
            synchronized(this) {
                if (_product == null) {
                    _product = DJISDKManager.getInstance().product
                    _connected.value = _product != null
                }
                return _product
            }
        }


    init {
        _connected.value = product?.isConnected == true

        if (connected.value) {
            _registered.value = true
            _loading.value = false
        }
    }

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
                Toast.makeText(ctx,"Registering DJI", Toast.LENGTH_SHORT).show()
                try{
                    DJISDKManager.getInstance().registerApp(ctx, DJISDKManagerCallback)
                }catch (error: Exception){
                    Timber.e(error)
                }
            }
        else{
            _loading.value = false
            _registered.value = true
        }
        _loading.value = true
        _connected.value = false
        _registered.value = false
        return true
    }

    fun checkConnection(ctx: Context){
        if(product?.isConnected == true){
            Toast.makeText(ctx, "Product is connected", Toast.LENGTH_SHORT).show()
        }else{
            if(product == null){
                Toast.makeText(ctx, "Product is null", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(ctx, "Product is not connected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun startSendingData(){
        TODO()
    }

    /**
     * Prepares required data for sending
     */
    private fun initSending(){

    }

    /**
     * Отправляет дату в формате который ты сам выберешь, желательно текст или интерфейс с данными
     */
    private fun sendData(data: Any?){
        TODO("Arsenii need to implement")
    }

    /**
     * Проверяет подключен ли телефон и можно ли с ним обмениваться информацией.
     */
    private fun canConnect(){
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
            product
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

val cb = object: CommonCallbacks.CompletionCallbackWith<String>{
    override fun onSuccess(name: String?) {
        Timber.i("Product $name got connected")
    }

    override fun onFailure(p0: DJIError?) {
        if (p0 != null) {
            Timber.e(p0.description)
        }
    }

}


@Composable
fun rememberDJIState(coroutine: CoroutineScope = rememberCoroutineScope()) = remember(coroutine) {
    DJIState(coroutine)
}