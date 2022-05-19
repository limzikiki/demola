package com.limzikiki.demola.state

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import dji.common.error.DJIError
import dji.common.error.DJISDKError
import dji.sdk.base.BaseComponent
import dji.sdk.base.BaseProduct
import dji.sdk.sdkmanager.DJISDKInitEvent
import dji.sdk.sdkmanager.DJISDKManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@Stable
class DJIState(val coroutine: CoroutineScope) {
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

    private var _product: BaseProduct? = null
    private val product: BaseProduct?
        get() {
            synchronized(this) {
                if (_product == null) {
                    _product = DJISDKManager.getInstance().product
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
        _loading.value = true
        _connected.value = false
        _registered.value = false
        return true
    }


    @Suppress("PrivatePropertyName")
    private val DJISDKManagerCallback = object : DJISDKManager.SDKManagerCallback {
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

        override fun onProductConnect(product: BaseProduct?) {
            Timber.i("Product got disconnected")
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