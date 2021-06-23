package com.example.androidbluetoothtest

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var register: BluetoothReceiver? = null
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mScanCallback = getDefaultScanCallback()
    private var isStart: Boolean = false
    private var mBtStartScan: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(register == null)
            register = BluetoothReceiver(findViewById(R.id.tvContents))

        if(register != null)
            registerReceiver(register, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))

        //패더링 관련 버튼 이벤트 초기화
        findViewById<Button>(R.id.btnShowPairedDevices).setOnClickListener {
            PreferencesData.setIsShowPairedDevicesPreferences(this@MainActivity, true)
        }

        //스캔 관련 버튼 이벤트 초기화
        mBtStartScan = findViewById<Button>(R.id.btStartScan).apply {
            setOnClickListener {
                isStart = !isStart

                if (isStart)
                    mBtStartScan?.text = "스캔 멈춤"
                else
                    mBtStartScan?.text = "스캔 시작"

                scanBluetooth(isStart)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if(register != null)
            unregisterReceiver(register)
    }

    /**
     * 블루투스 스캔
     */
    private fun scanBluetooth(isStart: Boolean) {
        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        val filters = ArrayList<ScanFilter>()

        if (mBluetoothAdapter != null) {
            if (isStart)
                mBluetoothAdapter!!.bluetoothLeScanner?.startScan(filters, settings, mScanCallback)
            else
                mBluetoothAdapter!!.bluetoothLeScanner?.stopScan(mScanCallback)
        } else
            Log.d(javaClass.simpleName, "BluetoothAdapter is Null")
    }

    /**
     * 블루투스 스캔을 위한 콜백
     */
    private fun getDefaultScanCallback(): ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            Log.d(
                javaClass.simpleName,
                "onScanResult() callbackType : $callbackType, result : device = ${result?.device ?: "None"}, name = ${result?.device?.name ?: "None"}, address = ${result?.device?.address ?: "None"}"
            )
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            if (results == null)
                return

            for (result in results) {
                Log.d(
                    javaClass.simpleName,
                    "onBatchScanResults() result : device = ${result?.device ?: "None"}, name = ${result?.device?.name ?: "None"}, address = ${result?.device?.address ?: "None"}"
                )
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.d(javaClass.simpleName, "onScanFailed() errorCode = $errorCode")
        }
    }

    /**
     * 블루투스 활성화 변화시 BroadcastReceiver
     */
    class BluetoothReceiver(private val tvTextView:TextView?):BroadcastReceiver(){
        constructor() : this(null)
        private val debugTag: String = "BluetoothReceiver"
        private val bluetoothAdapter:BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(debugTag, "onReceive")

            val action = intent!!.action

            if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state = intent!!.getIntExtra(
                    BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.ERROR
                )
                when (state) {
                    BluetoothAdapter.STATE_OFF -> tvTextView?.text = "BluetoothAdapter.STATE_OFF"
                    BluetoothAdapter.STATE_TURNING_OFF -> tvTextView?.text = "BluetoothAdapter.STATE_TURNING_OFF"
                    BluetoothAdapter.STATE_ON -> {
                        tvTextView?.text = "BluetoothAdapter.STATE_ON"

                        var isShowPairedDevices = PreferencesData.getIsShowPairedDevicesPreferences(context)

                        if(isShowPairedDevices != null && isShowPairedDevices == true) {
                            showPairedDevices()
                            PreferencesData.setIsShowPairedDevicesPreferences(context, false)
                        }
                    }
                    BluetoothAdapter.STATE_TURNING_ON -> tvTextView?.text = "BluetoothAdapter.STATE_TURNING_ON"
                }
            }
        }

        private fun showPairedDevices(){
            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
            pairedDevices?.forEach { device ->
                val deviceName = device.name
                val deviceHardwareAddress = device.address // MAC address
                Log.d(debugTag, "deviceName = $deviceName, deviceHardwareAddress = $deviceHardwareAddress")
            }
        }
    }
}