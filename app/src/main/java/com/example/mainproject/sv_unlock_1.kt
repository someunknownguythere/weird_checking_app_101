package com.example.mainproject

import android.app.Service
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import java.util.*

public class service_unlock2: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var sharedPreferences : SharedPreferences = getSharedPreferences("shared value 2", Context.MODE_PRIVATE)
        var editor : SharedPreferences.Editor = sharedPreferences.edit()
        Log.d("servicekty", "locked")
        editor.apply(){
            putBoolean("key", false)
            putBoolean("unlock", false)
        }.apply()
        Log.d("servicekty", "locked")
        if (check()) {
            startActivity(Intent(this, activity_showup::class.java))
        }
        stopSelf()
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onDestroy() {
        super.onDestroy()
        startService(Intent(this, MyService::class.java))
    }
    fun checktong():Boolean{
        var check : Boolean
        check = isAppRunning(applicationContext).toString() =="com.facebook.katana" ||
                isAppRunning(applicationContext).toString() == "com.facebook.orca"||
                isAppRunning(applicationContext).toString() == "com.zing.zalo"||
                isAppRunning(applicationContext).toString() == "com.ss.android.ugc.trill"||
                isAppRunning(applicationContext).toString() == "com.instagram.android"
        return check
    }
    fun check() : Boolean {
        var check : Boolean
        var fb: Boolean = checkapp("com.facebook.katana", "facebook")
        var mess: Boolean = checkapp("com.facebook.orca", "messenger")
        var zalo: Boolean = checkapp("com.zing.zalo", "zalo")
        var insta: Boolean = checkapp("com.ss.android.ugc.trill", "tiktok")
        var tiktok: Boolean = checkapp("com.instagram.android", "insta")
        var tong: Boolean
        tong = tongthoigian() >= lay_gia_tri_thoi_gian("tong")&& Loadtrangthai("tong")==true && checktong()==true
        check = fb || mess || zalo || insta || tiktok || tong
        return check
    }
    fun checkapp(packageName: String, tenapp: String): Boolean{
        var test: Boolean
        if (isAppEnabled(packageName, applicationContext)&& Loadtrangthai(tenapp) == true && totaltimevisible(packageName) >= lay_gia_tri_thoi_gian(tenapp)&& isAppRunning(applicationContext).toString() == packageName){
            test = true
        }else {test = false}
        return test
    }
    //lấy trạng thái tải app
    fun isAppEnabled(packageName: String, context: Context): Boolean {
        try {
            val packageManager = context.packageManager
            return packageManager.getApplicationInfo(packageName, 0).enabled
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }
    //lấy thời gian quản lí
    fun lay_gia_tri_thoi_gian(tenapp: String): Long {
        var sharedPreferences : SharedPreferences = getSharedPreferences("shared value", Context.MODE_PRIVATE)
        var data: Long = sharedPreferences.getLong(tenapp, 0)
        return data
    }
    //lấy trạng thái quản lí
    fun Loadtrangthai(s: String): Boolean? {
        var sharedPreferences : SharedPreferences = getSharedPreferences("shared value", Context.MODE_PRIVATE)
        var laytrangthai : Boolean = sharedPreferences.getBoolean(s + "boo", false)
        return laytrangthai
    }
    //    lấy thời gian đã sử dụng
    fun totaltimevisible (id: String) : Long {
        var usageStatsManager: UsageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        var cal: Calendar = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, -1)
        var queryUsageStats : List<UsageStats> = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,cal.timeInMillis,System.currentTimeMillis())
        var totaltimeuse : Long = 0
        var timeused : Long = 0
        for(i in 0 .. queryUsageStats.size-1){
            if (queryUsageStats.get(i).packageName == id){
                totaltimeuse = queryUsageStats.get(i).totalTimeInForeground
            }
        }
        timeused = totaltimeuse/1000/60
        return timeused
    }
    fun tongthoigian() : Long{
        var tong : Long
        tong =(totaltimevisible("com.facebook.katana") + totaltimevisible("com.facebook.orca") + totaltimevisible("com.zing.zalo") + totaltimevisible("com.ss.android.ugc.trill") + totaltimevisible("com.instagram.android"))
        return tong
    }
}