package com.example.mainproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var sharedPreferences : SharedPreferences = getSharedPreferences("shared value", Context.MODE_PRIVATE)
        var editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.apply(){
            putBoolean("key", false)
        }.apply()
        var intent2 = Intent(this, Mainscreen_activity_2::class.java)
        startActivity(intent2)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }
}