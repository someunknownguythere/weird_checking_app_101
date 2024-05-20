package com.example.mainproject

import android.app.AppOpsManager
import android.app.AppOpsManager.MODE_ALLOWED
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_mainscreen2.*
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

class Mainscreen_activity_2 : AppCompatActivity() {
    var sv2: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        var maxfb : Long
        var maxzalo : Long
        var maxmess: Long
        var maxtiktok: Long
        var maxinsta: Long
        var gioihantong: Long
        var tonghientai : Int


//        function thay đổi giá trị và gọi hàm (start)
        fun changing(){
            maxfb = lay_gia_tri_thoi_gian("facebook")
            maxzalo = lay_gia_tri_thoi_gian("zalo")
            maxmess = lay_gia_tri_thoi_gian("messenger")
            maxtiktok = lay_gia_tri_thoi_gian("tiktok")
            maxinsta = lay_gia_tri_thoi_gian("insta")
            appchange(fb, fb_1, fb_2,fb_3,fb_progressBar,"com.facebook.katana", maxfb)
            appchange(mess, mess_1, mess_2, mess_3, mess_progressbar, "com.facebook.orca", maxmess)
            appchange(zalo, zalo_1,zalo_2,zalo_3,zalo_progressbar,"com.zing.zalo", maxzalo)
            appchange(tiktok, tiktok_1,tiktok_2, tiktok_3,tiktok_progressbar,"com.ss.android.ugc.trill", maxtiktok)
            appchange(insta, insta_1, insta_2, insta_3, insta_progressbar, "com.instagram.android", maxinsta)
        }
//        function thay đổi giá trị và gọi hàm (end)


//        mở service
        var intent:Intent= Intent(this,MyService::class.java)
        startService(intent)



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainscreen2)
        sv2=false
        gioihantong = lay_gia_tri_thoi_gian("tong")
        tonghientai = (totaltimevisible("com.facebook.katana") + totaltimevisible("com.facebook.orca") + totaltimevisible("com.zing.zalo") + totaltimevisible("com.ss.android.ugc.trill") + totaltimevisible("com.instagram.android")).toInt()
        tong_progressbar.max = lay_gia_tri_thoi_gian("tong").toInt()
        tong_progressbar.progress = (totaltimevisible("com.facebook.katana") + totaltimevisible("com.facebook.orca") + totaltimevisible("com.zing.zalo") + totaltimevisible("com.ss.android.ugc.trill") + totaltimevisible("com.instagram.android")).toInt()
        tong_1.text = tong_progressbar.progress.toString() + " / " + tong_progressbar.max.toString()
        changing()
        if (checkUsageStatsPermisson()){
            changing()
        }else {
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
        retry.setOnClickListener {
            toast("reload")
            if (checkUsageStatsPermisson()){
                changing()
            }else {
                startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
            }

        }
        setting.setOnClickListener {
            luuonui2("checksetting", false)
            var intent2 : Intent = Intent(this, com.example.mainproject.Settings::class.java)
            startActivity(intent2)
            finish()
        }

    }



//    function lấy lần cuối sử dụng
    fun lasttimeused (id:String) : String {
        var usageStatsManager: UsageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        var cal: Calendar = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, -1)
        var queryUsageStats : List<UsageStats> = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,cal.timeInMillis,System.currentTimeMillis())
        var lasttimeusedapp: String = ""
        for(i in 0 .. queryUsageStats.size-1){
            if (queryUsageStats.get(i).packageName == id){
                lasttimeusedapp = convertTime(queryUsageStats.get(i).lastTimeUsed)
            }
        }
        return lasttimeusedapp
    }



//    function lấy thời gian sử dụng
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



//    function thay đổi giao diện bên ngoài
    fun appchange(image:ImageView, textView: TextView, textView2: TextView, textview3: TextView, progress: ProgressBar,id: String, max: Long?) {

        if (isAppEnabled(id, applicationContext)){
            textView.setText("Đã cài đặt")
            textView2.setText("lần cuối sd: " + lasttimeused(id))
            progress.setProgress(totaltimevisible(id).toInt())
            if (max != null) {
                progress.max = max.toInt()
            }
            textview3.setText(totaltimevisible(id).toString() + " / " + max.toString()+" phút")
            image.setOnClickListener {
                val launchIntent = packageManager.getLaunchIntentForPackage(id)
                startActivity(launchIntent)
            }

        }else {
            textView.setText("chưa cài đặt")
            textView2.setText("")
            progress.setProgress(0)
            textview3.setText("0 / 0")
        }
    }


//    function lấy format time
    private fun convertTime(lastTimeUsed: Long): String {
        var date: Date = Date(lastTimeUsed)
        var format : SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH)
        return format.format(date)
    }


//    function check quyền hê thống
    private fun checkUsageStatsPermisson(): Boolean {
        var appOpsManager: AppOpsManager?= null
        var mode:Int = 0
        appOpsManager = getSystemService(APP_OPS_SERVICE)!! as AppOpsManager

        mode = appOpsManager.checkOpNoThrow(OPSTR_GET_USAGE_STATS,Process.myUid(), packageName)
        return mode == MODE_ALLOWED
    }


//    funtion check app đã cài chưa
    fun isAppEnabled(packageName: String, context: Context): Boolean {
        try {
            val packageManager = context.packageManager
            return packageManager.getApplicationInfo(packageName, 0).enabled
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }


//    function lấy giá trị đã lưu
    fun lay_gia_tri_thoi_gian(tenapp: String): Long {
        var sharedPreferences : SharedPreferences = getSharedPreferences("shared value", Context.MODE_PRIVATE)
        var data: Long = sharedPreferences.getLong(tenapp, 0)
        return data
    }


//    function lấy trạng thái on UI activity
    fun luuonui2( name: String, boolean: Boolean){
        var sharedPreferences : SharedPreferences = getSharedPreferences("shared value", Context.MODE_PRIVATE)
        var editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.apply(){
            putBoolean(name + "boo", boolean)
        }.apply()
    }


//    function khi ngưng
    override fun onPause() {
        super.onPause()
        var sharedPreferences : SharedPreferences = getSharedPreferences("shared value", Context.MODE_PRIVATE)
        var editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.apply(){
            putBoolean("key", true)
        }.apply()
        startService(Intent(this, MyService::class.java))
    }

}