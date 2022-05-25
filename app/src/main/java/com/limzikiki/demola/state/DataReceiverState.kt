package com.limzikiki.demola.state

import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope


@Stable
class DataReceiverState(private val coroutine: CoroutineScope) {
    private var _receiving = mutableStateOf(false)
    val receiving: State<Boolean>
        get() = _receiving

    private var _batteryLevel = mutableStateOf(0)
    val batteryLevel: State<Int>
        get() = _batteryLevel

    private var _altitude = mutableStateOf(0f)
    val altitude: State<Float>
        get() = _altitude

    private var _flightTime = mutableStateOf(0)
    val flightTime: State<Int>
        get() = _flightTime

    fun startReceiving(){
        // To mark this device as ready to receive
        _receiving.value = true
        TODO("Arsenii implement")
    }
}

@Composable
fun rememberDataReceiverState(coroutine: CoroutineScope = rememberCoroutineScope()) = remember(coroutine) {
    DataReceiverState(coroutine)
}