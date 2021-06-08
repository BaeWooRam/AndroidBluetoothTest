package com.example.androidbluetoothtest

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class CheckBluetoothActivity : AppCompatActivity() {
    private val debugTag = "CheckBluetoothActivity"
    var mBluetoothAdapter:BluetoothAdapter? = null

    private val result =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d(debugTag, "블루투스 켜짐")
        }else{
            Log.d(debugTag, "블루투스 꺼짐")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        //블루투스가 현재 활성화 되어있고 사용할 준비가 되어있으면 true를 반환한다.
        if(mBluetoothAdapter!!.isEnabled)
            Log.d(debugTag, "블루투스 isEnabled = true")
        else {
            Log.d(debugTag, "블루투스 isEnabled = false")
            result.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        }
    }
}