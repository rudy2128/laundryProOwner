package com.anthony.laundrypro.ui.outlet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.anthony.laundrypro.R
import com.anthony.laundrypro.helper.CodePref
import com.anthony.laundrypro.ui.product.ProductActivity

class DetailOutletActivity : AppCompatActivity() {
    private lateinit var tvOutletName:TextView
    private lateinit var tvPhone:TextView
    private lateinit var tvAddress:TextView
    private lateinit var tvCode:TextView
    private lateinit var cardService:CardView
    private lateinit var imgOutlet:ImageView
    public lateinit var btnBack:ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_outlet)
        tvOutletName = findViewById(R.id.tv_outlet_name)
        tvCode = findViewById(R.id.tv_outlet_code)
        tvPhone = findViewById(R.id.tv_outlet_phone)
        tvAddress = findViewById(R.id.tv_address)
        cardService = findViewById(R.id.card_service)
        imgOutlet = findViewById(R.id.img_outlet)
        btnBack = findViewById(R.id.btn_back)

        btnBack.setOnClickListener {
            finish()
        }

        val code = intent.getStringExtra("CODE")
        val name = intent.getStringExtra("NAME")
        val address = intent.getStringExtra("ADDRESS")

        tvOutletName.text = name
        tvCode.text = code
        tvAddress.text = address
        tvPhone.text = ""

        cardService.setOnClickListener {
            val cdPref = CodePref(applicationContext)
            cdPref.outlet = code
            val i = Intent(applicationContext,ProductActivity::class.java)
            i.putExtra("CODE_OUTLET",code)
            startActivity(i)
        }
    }
}