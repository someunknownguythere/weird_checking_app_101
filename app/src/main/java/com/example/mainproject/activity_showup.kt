package com.example.mainproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_showup.*
import android.content.pm.PackageManager
import android.util.Log


class activity_showup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showup)
        button3.setOnClickListener {
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(startMain)
//            val launchIntent = packageManager.getLaunchIntentForPackage("com.bluestacks.launcher")
//            startActivity(launchIntent)
        }
        button.setOnClickListener {
            startActivity(Intent(this@activity_showup, otp_activity::class.java))
        }
        button2.setOnClickListener {
            startService(Intent(this, service_unlock::class.java))
            finish()
        }
    }
    override fun onPause() {
        super.onPause()
        startService(Intent(this, MyService::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        startService(Intent(this, MyService::class.java))

    }
}
