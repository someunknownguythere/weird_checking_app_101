package com.example.mainproject

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import android.util.Log

public class service_unlock: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("servicekty", "unlocked")
        stopService(Intent(this, MyService::class.java))
        var sharedPreferences : SharedPreferences = getSharedPreferences("shared value 2", Context.MODE_PRIVATE)
        var editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.apply(){
            putBoolean("key", true)
            putBoolean("unlock", true)

        }.apply()
        Thread.sleep(10000)
        startService(Intent(this, service_unlock2::class.java))
        stopSelf()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        startService(Intent(this, MyService::class.java))
    }
}