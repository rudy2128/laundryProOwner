package com.anthony.laundrypro.ui.cash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.anthony.laundrypro.R

class CashBankActivity : AppCompatActivity() {
    private lateinit var btnBack: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_bank)
        btnBack = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }
    }
}