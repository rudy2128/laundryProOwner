package com.anthony.laundrypro.ui.otp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.anthony.laundrypro.R
import com.anthony.laundrypro.ui.reg.OwnerRegActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {
    private lateinit var btnOTP: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var phoneNumb: String
    private lateinit var verificationCodeBySystem:String
    private lateinit var edtOtp: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        btnOTP = findViewById(R.id.btn_otp)
        edtOtp = findViewById(R.id.edt_otp)



        phoneNumb = intent.getStringExtra("PHONE").toString()
        mAuth = FirebaseAuth.getInstance()

        sendVerificationCodeToUser(phoneNumb)

    }

    private fun sendVerificationCodeToUser(phoneNumb: String) {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber("+62$phoneNumb")
            .setTimeout(30L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    verificationCodeBySystem = p0

                }
                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    // Sign in with the credential
                    val code = phoneAuthCredential.smsCode
                    if (code != null) {
                        edtOtp.setText(code)
                        verifyCode(codeByUser = code)
                    }
                }
                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(applicationContext,e.message, Toast.LENGTH_SHORT).show()
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyCode(codeByUser: String) {
        mAuth = FirebaseAuth.getInstance()
        val credential =
            PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser)
        signInTheUserByCredential(credential)

    }

    private fun signInTheUserByCredential(credential: PhoneAuthCredential) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this@OtpActivity
            ) { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    val intent = Intent(applicationContext, OwnerRegActivity::class.java)
                    intent.putExtra("PHONE",phoneNumb)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, "No OTP salah", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
}