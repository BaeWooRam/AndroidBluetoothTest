package com.example.androidbluetoothtest

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
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
    var register: BluetoothReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(register == null)
            register = BluetoothReceiver(findViewById(R.id.tvContents))

        if(register != null)
            registerReceiver(register, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))

        findViewById<Button>(R.id.btnShowPairedDevices).setOnClickListener {
            PreferencesData.setIsShowPairedDevicesPreferences(this@MainActivity, true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if(register != null)
            unregisterReceiver(register)
    }

    class BluetoothReceiver(private val tvTextView:TextView?):BroadcastReceiver(){
        constructor() : this(null)
        private val debugTag: String = "BluetoothReceiver"
        private val bluetoothAdapter:BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        private var isShowPairedDevices = false

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