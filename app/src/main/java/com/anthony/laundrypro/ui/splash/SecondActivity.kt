package com.anthony.laundrypro.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.anthony.laundrypro.R
import com.anthony.laundrypro.ui.login.LoginActivity

class SecondActivity : AppCompatActivity() {
    private lateinit var btnLogin: Button
    private lateinit var btnNext: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        btnLogin = findViewById(R.id.btn_login)
        btnNext = findViewById(R.id.btn_next)
        btnLogin.setOnClickListener {
            val i = Intent(applicationContext, LoginActivity::class.java)
            startActivity(i)
            finish()
        }

        btnNext.setOnClickListener {
            val i = Intent(applicationContext,LoginActivity::class.java)
            startActivity(i)
            finish()

        }
    }
}