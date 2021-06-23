package com.example.androidbluetoothtest.ble

import android.app.Service
import android.bluetooth.*
import android.content.Intent
import android.os.IBinder

class BleService(private var mBluetoothGatt: BluetoothGatt?) : Service() {
    constructor() :this(null)

    private var connectionState = BLE_STATE_DISCONNECTED

    /**
     * GATT 서버 연결
     */
    private fun connectGatt(device: BluetoothDevice) {
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback)
    }

    /**
     * GATT 서버 연결 후 콜백 인터페이스
     */
    private val mGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(
            gatt: BluetoothGatt,
            status: Int,
            newState: Int
        ) {
            val intentAction: String
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    connectionState = BLE_STATE_CONNECTED
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    connectionState = BLE_STATE_CONNECTED
                }
            }
        }

        // New services discovered
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> { }
                else -> { }
            }
        }

        // Result of a characteristic read operation
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> { }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    companion object {
        //Bluetooth 연결 상태
        private const val BLE_STATE_DISCONNECTED = 0
        private const val BLE_STATE_CONNECTING = 1
        private const val BLE_STATE_CONNECTED = 2

        //Bluetooth GATT 상태
        const val BLE_ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED"
        const val BLE_ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED"
        const val BLE_ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED"
        const val BLE_ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE"
        const val BLE_EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA"
    }
}