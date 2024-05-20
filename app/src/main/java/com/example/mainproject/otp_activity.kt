 package com.example.mainproject

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.mainproject.databinding.ActivityMainBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_otp.*
import java.util.concurrent.TimeUnit

 class otp_activity : AppCompatActivity() {

     private lateinit var binding: ActivityMainBinding


     // sesend if st failed
     private var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null

     private var mcallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks?= null
     private var mvertifationid: String? = null
     private lateinit var firebaseAuth: FirebaseAuth

     private val Tag = "MAIN_TAG"
     // PROGRESS DIALOG
     private lateinit var progressDialog: ProgressDialog
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_otp)
         var phonenumber : String = "+84344509688"
         phoneip.setText(phonenumber)
         phoneLl.visibility = View.VISIBLE
         code.visibility = View.GONE

         firebaseAuth = FirebaseAuth.getInstance()

         progressDialog = ProgressDialog(this)
         progressDialog.setTitle("Pls wait")
         progressDialog.setCanceledOnTouchOutside(false)

         mcallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
             override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                 Log.d(Tag, "on vertyfication done")
                 signInWithPhoneAuthCredentical(phoneAuthCredential)
             }

             override fun onVerificationFailed(e: FirebaseException) {
                 progressDialog.dismiss()
                 Log.d(Tag, "on vertyfication failed ${e.message}")
                 Toast.makeText(this@otp_activity, "failed because ${e.message} ", Toast.LENGTH_SHORT).show()
             }

             override fun onCodeSent(vertificationID: String, token: PhoneAuthProvider.ForceResendingToken) {
                 super.onCodeSent(vertificationID, token)
                 Log.d(Tag, "oncodesent: $vertificationID")
                 mvertifationid=vertificationID
                 forceResendingToken = token
                 progressDialog.dismiss()
                 Log.d(Tag, "code sent")

                 phoneLl.visibility=View.GONE
                 code.visibility=View.VISIBLE
                 Toast.makeText(this@otp_activity, "code sent ...", Toast.LENGTH_SHORT).show()
                 vertifycation_text.text = "Hãy nhập code đã được gửi đến sđt ${phoneip.text.toString().trim()}"
             }
         }

         button.setOnClickListener {
             val phone = phoneip.text.toString().trim()

             if(TextUtils.isEmpty(phone)){
                 Toast.makeText(this@otp_activity, "please enter ur number",Toast.LENGTH_SHORT).show()

             }else{
                 startphonenumbervertification(phone)
             }
         }
         respondcode.setOnClickListener {
             val phone = phoneip.text.toString().trim()

             if(TextUtils.isEmpty(phone)){
                 Toast.makeText(this@otp_activity, "please enter ur number",Toast.LENGTH_SHORT).show()

             }else{
                 resendvertificationcode(phone, forceResendingToken)
             }
         }
         sendbutton.setOnClickListener {
             val code = codeip.text.toString().trim()

             if (TextUtils.isEmpty(code)){
                 Toast.makeText(this@otp_activity, "please enter ur code",Toast.LENGTH_SHORT).show()
             }else{
                 verifyphonenumberwithcode(mvertifationid, code)
             }

         }
     }

     private fun startphonenumbervertification(phone:String){
         progressDialog.setMessage("Verifying...")
         progressDialog.show()

         val options = PhoneAuthOptions.newBuilder(firebaseAuth)
             .setPhoneNumber(phone)
             .setTimeout(60L, TimeUnit.SECONDS)
             .setActivity(this)
             .setCallbacks(mcallback!!)
             .build()
         PhoneAuthProvider.verifyPhoneNumber(options)
     }

     private fun resendvertificationcode(phone: String, token: PhoneAuthProvider.ForceResendingToken?){
         progressDialog.setMessage("Resending code")
         progressDialog.show()

         val options = PhoneAuthOptions.newBuilder(firebaseAuth)
             .setPhoneNumber(phone)
             .setTimeout(60L, TimeUnit.SECONDS)
             .setActivity(this)
             .setCallbacks(mcallback!!)
             .setForceResendingToken(token!!)
             .build()
         PhoneAuthProvider.verifyPhoneNumber(options)
     }
     private fun verifyphonenumberwithcode(verificationID: String?, code:String){
         progressDialog.setMessage("Verifying code...")
         progressDialog.show()

         val credential = PhoneAuthProvider.getCredential(verificationID!!, code)
         signInWithPhoneAuthCredentical(credential)

     }

     private fun signInWithPhoneAuthCredentical(credential: PhoneAuthCredential) {
         progressDialog.setMessage("Logging in...")
         firebaseAuth.signInWithCredential(credential)
             .addOnSuccessListener {
                 //lolgin done
                 startService(Intent(this, service_unlock::class.java))
                 progressDialog.dismiss()
                 val phone = firebaseAuth.currentUser?.phoneNumber
                 Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                 startService(Intent(this, MyService::class.java))
                 finish()
             }
             .addOnFailureListener{e->
                 //login fail
                 progressDialog.dismiss()
                 Toast.makeText(this, "failed to sign in because ${e.message}", Toast.LENGTH_SHORT).show()
                 startService(Intent(this, MyService::class.java))
             }

     }
     }