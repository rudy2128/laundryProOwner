package com.anthony.laundrypro.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anthony.laundrypro.R
import com.anthony.laundrypro.ui.MainActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit


class PhoneActivity : AppCompatActivity() {
    private lateinit var edtOtp:EditText
    private lateinit var tvTime:TextView
    private lateinit var tvSend:TextView
    private lateinit var btnNext:Button
    private lateinit var mAuth:FirebaseAuth
    private lateinit var verificationId:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone)
        edtOtp = findViewById(R.id.edt_otp)
        tvTime = findViewById(R.id.tv_time)
        tvSend = findViewById(R.id.tv_send)
        btnNext = findViewById(R.id.btn_next)

        val phone = intent.getStringExtra("PHONE")

        if(phone!=null){
            verificationPhone(phone)
        }

        mAuth = FirebaseAuth.getInstance()

        tvSend.setOnClickListener {

        }

        btnNext.setOnClickListener {

            verifyOtp()

        }
    }

    private fun verifyOtp() {

        if (TextUtils.isEmpty(edtOtp.text.toString())){
            Toast.makeText(applicationContext,"Code OTP salah!!",Toast.LENGTH_SHORT).show()
        }else{
            verifyCode(edtOtp.text.toString())
        }


    }


    private fun verificationPhone(phone: String) {
        mAuth = FirebaseAuth.getInstance()
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber("+62$phone") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this@PhoneActivity) // Activity (for callback binding)
            .setCallbacks(object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                   val code = credential.smsCode
                    if (code!=null) {
                        verifyCode(code)
                    }

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(applicationContext,"Code Filed",Toast.LENGTH_SHORT).show()
                }
                override fun onCodeSent(s: String, token: PhoneAuthProvider.ForceResendingToken,
                ) {
                    super.onCodeSent(s,token)
                    verificationId = s

                }

            }) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId,code)

        signInByCredential(credential)
    }

    private fun signInByCredential(credential: PhoneAuthCredential) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(applicationContext,"Login Sukses",Toast.LENGTH_SHORT).show()
                val i = Intent(applicationContext,MainActivity::class.java)
                startActivity(i)
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser!=null){
            val i = Intent(applicationContext,MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }


}