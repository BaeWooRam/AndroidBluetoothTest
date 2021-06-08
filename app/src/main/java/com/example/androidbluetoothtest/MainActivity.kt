package com.example.androidbluetoothtest

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    var register:BluetoothReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(register == null)
            register = BluetoothReceiver()

        if(register != null)
            registerReceiver(register, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
    }

    override fun onDestroy() {
        super.onDestroy()

        if(register != null)
            unregisterReceiver(register)
    }

    class BluetoothReceiver:BroadcastReceiver(){
        private val debugTag: String = "BluetoothReceiver"

        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(debugTag, "onReceive")

            val action = intent!!.action

            if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state = intent!!.getIntExtra(
                    BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.ERROR
                )
                when (state) {
                    BluetoothAdapter.STATE_OFF -> Log.d(debugTag, "BluetoothAdapter.STATE_OFF")
                    BluetoothAdapter.STATE_TURNING_OFF -> Log.d(debugTag, "BluetoothAdapter.STATE_TURNING_OFF")
                    BluetoothAdapter.STATE_ON -> Log.d(debugTag, "BluetoothAdapter.STATE_ON")
                    BluetoothAdapter.STATE_TURNING_ON -> Log.d(debugTag, "BluetoothAdapter.STATE_TURNING_ON")
                }
            }
        }
    }
}