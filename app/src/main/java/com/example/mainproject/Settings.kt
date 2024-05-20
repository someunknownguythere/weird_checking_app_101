package com.example.mainproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import org.jetbrains.anko.toast
import kotlinx.android.synthetic.main.activity_settings.*

class Settings : AppCompatActivity() {
    var gioihanfb : Long? = null
    var kiemsoatfb : Boolean? = null
    var gioihanmess : Long? = null
    var kiemsoatmess : Boolean? = null
    var gioihanzalo : Long? = null
    var kiemsoatzalo : Boolean? = null
    var gioihantiktok : Long? = null
    var kiemsoattiktok : Boolean? = null
    var gioihaninsta : Long? = null
    var kiemsoatinsta : Boolean? = null
    var gioihantong : Long? = null
    var kiemsoattong : Boolean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        var thongbao : Toast = Toast.makeText(this, "",Toast.LENGTH_SHORT)
        setUI(gioihanfb, kiemsoatfb, check_facebook, tg_facebook, time_facebook)
        setUI(gioihanmess, kiemsoatmess, check_mess, tg_mess, time_mess)
        setUI(gioihanzalo, kiemsoatzalo, check_zalo, tg_zalo, time_zalo)
        setUI(gioihantiktok, kiemsoattiktok, check_tiktok, tg_tiktok, time_tiktok)
        setUI(gioihaninsta, kiemsoatinsta, check_instagram, tg_instagram, time_instagram)
        setUI(gioihantong, kiemsoattong, check_fulltime, tg_fulltime, time_fulltime)
        fun settle(){
            gioihanfb = Loadthoigian("facebook")
            gioihanmess = Loadthoigian("messenger")
            gioihanzalo = Loadthoigian("zalo")
            gioihantiktok = Loadthoigian("tiktok")
            gioihaninsta = Loadthoigian("insta")
            gioihantong = Loadthoigian("tong")
            kiemsoatfb = Loadtrangthai("facebook")
            kiemsoatinsta = Loadtrangthai("insta")
            kiemsoatmess = Loadtrangthai("messenger")
            kiemsoattiktok = Loadtrangthai("tiktok")
            kiemsoatzalo = Loadtrangthai("zalo")
            kiemsoattong = Loadtrangthai("tong")
            checking(check_facebook, tg_facebook)
            checking(check_mess, tg_mess)
            checking(check_zalo, tg_zalo)
            checking(check_tiktok, tg_tiktok)
            checking(check_instagram, tg_instagram)
            checking(check_fulltime, tg_fulltime)
            setUI(gioihanfb, kiemsoatfb, check_facebook, tg_facebook, time_facebook)
            setUI(gioihanmess, kiemsoatmess, check_mess, tg_mess, time_mess)
            setUI(gioihanzalo, kiemsoatzalo, check_zalo, tg_zalo, time_zalo)
            setUI(gioihantiktok, kiemsoattiktok, check_tiktok, tg_tiktok, time_tiktok)
            setUI(gioihaninsta, kiemsoatinsta, check_instagram, tg_instagram, time_instagram)
            setUI(gioihantong, kiemsoattong, check_fulltime, tg_fulltime, time_fulltime)
            UIupdate(tg_facebook, time_facebook)
            UIupdate(tg_mess, time_mess)
            UIupdate(tg_instagram, time_instagram)
            UIupdate(tg_tiktok, time_tiktok)
            UIupdate(tg_zalo, time_zalo)
            UIupdate(tg_fulltime, time_fulltime)
        }
        settle()

        gioi_thieu.setOnClickListener {
            var intent : Intent = Intent(this, AboutUs::class.java)
            startActivity(intent)
        }
        save.setOnClickListener {
            if (checkfaulty(check_facebook, tg_facebook, tg_fulltime) && checkfaulty(check_mess, tg_mess, tg_fulltime) && checkfaulty(check_zalo, tg_zalo, tg_fulltime) && checkfaulty(check_tiktok, tg_tiktok, tg_fulltime) && checkfaulty(check_instagram, tg_instagram, tg_fulltime)){

                saveData(check_facebook, tg_facebook, "facebook", thongbao)
                saveData(check_mess, tg_mess, "messenger", thongbao)
                saveData(check_zalo, tg_zalo, "zalo", thongbao)
                saveData(check_tiktok, tg_tiktok, "tiktok", thongbao)
                saveData(check_instagram, tg_instagram, "insta", thongbao)
                saveData(check_fulltime, tg_fulltime, "tong", thongbao)
                settle()

                toast("Đã lưu dữ liệu")
                Log.d("stat", Loadtrangthai("check").toString())

            }else{
                toast("Vui lòng nhập lại giá trị. Giá trị giới hạn của một ứng dụng không được lớn hơn giá trị tổng")

            }

        }
        home_screen.setOnClickListener {
            var intent : Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
    //    override fun onResume() {
//        super.onResume()
//        luuonui("checksetting", false)
//    }
    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }
    fun checkfaulty(checkBox: CheckBox,seekBar: SeekBar, seekbarmax: SeekBar ) : Boolean {
        var a: Boolean
        if (seekbarmax.isEnabled == true) {
            if (checkBox.isChecked == true){
                a = seekBar.progress <= seekbarmax.progress
            }else{
                a = true
            }
        }else {
            a = true
        }

        return a
    }
    fun Loadtrangthai(s: String): Boolean? {
        var sharedPreferences : SharedPreferences = getSharedPreferences("shared value", Context.MODE_PRIVATE)
        var laytrangthai : Boolean = sharedPreferences.getBoolean(s + "boo", false)
        return laytrangthai
    }
    fun UIupdate(seekbar: SeekBar, text: TextView){
        seekbar.setOnSeekBarChangeListener(object  : SeekBar.OnSeekBarChangeListener{
            var start = 0
            var end = 0
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                text.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    start = seekBar.progress
                    text.text = seekBar.progress.toString()
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    end = seekBar.progress
                    text.text = seekBar.progress.toString()
                }
            }


        })
    }

    fun Loadthoigian(s: String): Long? {
        var sharedPreferences : SharedPreferences = getSharedPreferences("shared value", Context.MODE_PRIVATE)
        var laythoigian : Long = sharedPreferences.getLong(s, 0)
        return laythoigian
    }

    fun setUI(gioihan: Long?, kiemsoat: Boolean?, checkBox: CheckBox, seekBar: SeekBar,text: TextView){
        var isenbled : Boolean = false
        isenbled = checkBox.isChecked
        if (isenbled == true) {
            if (kiemsoat != null) {
                checkBox.isChecked = kiemsoat
            }
            if (gioihan != null) {
                seekBar.progress = gioihan.toInt()
                seekBar.isEnabled = true
            }
            text.text = gioihan.toString()
        }else {
            if (kiemsoat != null) {
                checkBox.isChecked = kiemsoat
            }
            if (gioihan != null) {
                seekBar.progress = gioihan.toInt()
                seekBar.isEnabled = false
            }
            text.text = gioihan.toString()
        }


    }
    fun checking (checkBox: CheckBox, seekBar: SeekBar){
        checkBox.setOnClickListener {
            seekBar.isEnabled = checkBox.isChecked == true
        }
    }
    fun saveData(c: CheckBox, d: SeekBar, tenapp: String, thongbao : Toast){
        var time = d.progress.toLong()
        var kiemsoat = c.isChecked
        var sharedPreferences : SharedPreferences = getSharedPreferences("shared value", Context.MODE_PRIVATE)
        var editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.apply {
            putLong(tenapp, time)
            putBoolean(tenapp + "boo", kiemsoat)
        }.apply()

    }
    fun luukiemsoat(switch: Switch?, name: String){
        var sharedPreferences : SharedPreferences = getSharedPreferences("shared value", Context.MODE_PRIVATE)
        var editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.apply {
            if (switch != null) {
                putBoolean(name + "boo", switch.isChecked)
            }
        }.apply()
    }
    fun luuonui( name: String, boolean: Boolean){
        var sharedPreferences : SharedPreferences = getSharedPreferences("shared value", Context.MODE_PRIVATE)
        var editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.apply {
            putBoolean(name + "boo", boolean)
        }.apply()
    }
    fun UIcheck(switch: Switch, boolean: Boolean?){
        if (boolean != null) {
            switch.isActivated = boolean
        }
    }




}