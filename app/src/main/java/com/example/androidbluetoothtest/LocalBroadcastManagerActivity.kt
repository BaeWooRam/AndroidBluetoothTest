package com.example.androidbluetoothtest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.JobIntentService
import androidx.core.app.JobIntentService.enqueueWork
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class LocalBroadcastManagerActivity:AppCompatActivity(R.layout.activity_local_broadcast_manager) {
    var receiver:LocalBroadReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receiver = LocalBroadReceiver()
        LocalBroadcastManager.getInstance(this@LocalBroadcastManagerActivity).registerReceiver(receiver!!, IntentFilter(BROADCAST_ACTION))


        findViewById<Button>(R.id.btSendMessage1).run { setOnClickListener {
            notifyRSSPull("Success")
        }}

        findViewById<Button>(R.id.btSendMessage2).run { setOnClickListener {
            notifyRSSPull("Failure")
        }}
    }

    private fun notifyRSSPull(status:String){
        val localIntent = Intent(BROADCAST_ACTION).apply {
            // Puts the status into the Intent
            putExtra(EXTENDED_DATA_STATUS, status)
        }

        enqueueWork(this, RSSPullService::class.java, RSS_JOB_ID, localIntent)
    }

    /**
     *
     */
    class RSSPullService : JobIntentService() {
        override fun onHandleWork(intent: Intent) {
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
    }

    /**
     *
     */
    class LocalBroadReceiver:BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val status = intent?.getStringExtra(EXTENDED_DATA_STATUS) ?: "none"
            Log.d("LocalBroadReceiver", "status = $status")
        }
    }

    companion object{
        const val RSS_JOB_ID = 1000
        const val BROADCAST_ACTION = "com.example.androidbluetoothtest.BROADCAST"
        const val EXTENDED_DATA_STATUS = "com.example.androidbluetoothtest.STATUS"
    }
}