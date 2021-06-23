package com.example.androidbluetoothtest.ble

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * GATT 서비스로부터 다양한 이벤트 핸들링
 */
class GattUpdateReceiver(private var bleService: BleService?) : BroadcastReceiver() {
    constructor():this(null)

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action){
            BleService.BLE_ACTION_GATT_CONNECTED -> {

            }
            BleService.BLE_ACTION_GATT_DISCONNECTED -> {

            }
            BleService.BLE_ACTION_GATT_SERVICES_DISCOVERED -> {

            }
            BleService.BLE_ACTION_DATA_AVAILABLE -> {

            }
        }
    }
}