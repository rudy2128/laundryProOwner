package com.anthony.laundrypro.ui.reg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.anthony.laundrypro.R
import com.anthony.laundrypro.ui.otp.OtpActivity
import com.google.android.material.textfield.TextInputEditText

class RegFirstActivity : AppCompatActivity() {
    private lateinit var edtPhone:TextInputEditText
    private lateinit var btnSend:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_first)
        edtPhone = findViewById(R.id.edt_phone)
        btnSend = findViewById(R.id.btn_send)

        btnSend.setOnClickListener {
            sendPhone()
        }
    }

    private fun sendPhone() {
        val phone = edtPhone.text.toString()

        if (phone.isEmpty()){
            edtPhone.error = getString(R.string.empty)
        }else{
            val i = Intent(applicationContext,OtpActivity::class.java)
            i.putExtra("PHONE",phone)
            startActivity(i)
        }
    }
}