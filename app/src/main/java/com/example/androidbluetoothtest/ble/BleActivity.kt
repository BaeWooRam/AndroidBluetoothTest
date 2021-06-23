package com.example.androidbluetoothtest.ble

import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.androidbluetoothtest.R

class BleActivity : AppCompatActivity() {
    private val mBleHandler: Handler = Handler()
    private var mScanning: Boolean = false
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mBluetoothGatt: BluetoothGatt? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBluetoothAdapter = getBluetoothAdapter()
        checkBluetooth()
    }

    /**
     * BluetoothAdapter 가져오기
     */
    private fun getBluetoothAdapter(): BluetoothAdapter {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return bluetoothManager.adapter
    }

    private val BluetoothAdapter.isDisabled: Boolean
        get() = !isEnabled

    /**
     * 블루투스 활성화 시키기
     */
    private fun checkBluetooth() {
        mBluetoothAdapter?.takeIf { it.isDisabled }?.apply {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, BLE_REQUEST_ENABLE)
        }
    }

    /**
     * 저전력 블루투스 기기 찾기
     */
    private fun scanLeDevice(enable: Boolean) {
        when (enable) {
            true -> {
                // Stops scanning after a pre-defined scan period.
                mBleHandler.postDelayed({
                    mScanning = false
                    mBluetoothAdapter?.stopLeScan(leScanCallback)
                }, BLE_SCAN_PERIOD)
                mScanning = true
                mBluetoothAdapter?.startLeScan(leScanCallback)
            }
            else -> {
                mScanning = false
                mBluetoothAdapter?.stopLeScan(leScanCallback)
            }
        }
    }


    /**
     * 저전력 블루투스 스캔 결과를 제공하는 콜백 인터페이스
     */
    private val leScanCallback = BluetoothAdapter.LeScanCallback { device, rssi, scanRecord ->

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        const val BLE_REQUEST_ENABLE = 100
        const val BLE_SCAN_PERIOD: Long = 10000
    }
}