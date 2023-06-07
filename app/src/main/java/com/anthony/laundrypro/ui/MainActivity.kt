package com.anthony.laundrypro.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.anthony.laundrypro.R
import com.anthony.laundrypro.helper.CodePref
import com.anthony.laundrypro.ui.cash.CashBankActivity
import com.anthony.laundrypro.ui.cash.CashEdcActivity
import com.anthony.laundrypro.ui.cash.CashHandActivity
import com.anthony.laundrypro.ui.cash.CashMerchantActivity
import com.anthony.laundrypro.ui.employee.EmployeeActivity
import com.anthony.laundrypro.ui.outlet.OutletActivity
import com.anthony.laundrypro.ui.setting.SettingActivity
import com.anthony.laundrypro.ui.slider.SliderAdapter
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import com.smarteist.autoimageslider.SliderView

class MainActivity : AppCompatActivity() {
    private lateinit var cardCashHand:CardView
    private lateinit var cardCashBank:CardView
    private lateinit var cardCashMerchant:CardView
    private lateinit var cardCashEdc:CardView
    private lateinit var imgProf:ImageView
    private lateinit var btnNav: BottomNavigationView
    private lateinit var mDatabase: DatabaseReference
    private lateinit var tvName:TextView
    private lateinit var tvStatus:TextView
    lateinit var sliderView: SliderView
    lateinit var sliderAdapter: SliderAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvName = findViewById(R.id.tv_name)
        tvStatus = findViewById(R.id.tv_status)
        imgProf = findViewById(R.id.img_profile)
        btnNav = findViewById(R.id.bottom_navigation)
        cardCashHand = findViewById(R.id.card_cash_hand)
        cardCashBank = findViewById(R.id.card_cash_bank)
        cardCashMerchant = findViewById(R.id.card_cash_merchant)
        cardCashEdc = findViewById(R.id.card_cash_edc)
        sliderView = findViewById(R.id.slider)


        cardCashHand.setOnClickListener {
            val i = Intent(applicationContext,CashHandActivity::class.java)
            startActivity(i)
        }
        cardCashBank.setOnClickListener {
            val i = Intent(applicationContext,CashBankActivity::class.java)
            startActivity(i)
        }
        cardCashMerchant.setOnClickListener {
            val i = Intent(applicationContext,CashMerchantActivity::class.java)
            startActivity(i)
        }
        cardCashEdc.setOnClickListener {
            val i = Intent(applicationContext, CashEdcActivity::class.java)
            startActivity(i)
        }

        mDatabase = FirebaseDatabase.getInstance("https://laundry-87068-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("laundryPro")
        val imageUrl = arrayListOf<String>()
        val codePref = CodePref(applicationContext)
        val code = codePref.owner
        sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        sliderAdapter = SliderAdapter(imageUrl)
        mDatabase.child(code).child("banner").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (i in snapshot.children){
                        val slide = i.value.toString()
                        repeat(slide.length) {
                            imageUrl.add(slide)

                        }


                    }
                }
                sliderView.setSliderAdapter(sliderAdapter)
                sliderView.scrollTimeInSec = 5
                sliderView.isAutoCycle = true
                sliderView.startAutoCycle()

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "loadData:onCancelled", error.toException())
            }

        })




      mDatabase.child(code).child("imageOwner").get().addOnSuccessListener {
          val imageOwner = it.value
          Glide.with(applicationContext)
              .load(imageOwner)
              .override(80,120)
              .error(R.drawable.baseline_account_circle_24)
              .into(imgProf)

      }
        mDatabase.child(code).child("status").get().addOnSuccessListener {
            val status = it.value
            tvStatus.text = status.toString()
        }

        mDatabase.child(code).child("name").get().addOnSuccessListener {
            val name = it.value
            tvName.text = name.toString()
        }


        btnNav.setOnItemSelectedListener {item->
            when(item.itemId){
                R.id.page_home ->{
                    return@setOnItemSelectedListener true
                }
                R.id.page_employee ->{
                    val i = Intent(applicationContext, EmployeeActivity::class.java)
                    startActivity(i)
                    return@setOnItemSelectedListener true

                }
                R.id.page_outlet->{
                    val i = Intent(applicationContext, OutletActivity::class.java)
                    startActivity(i)
                    return@setOnItemSelectedListener true

                }
                R.id.page_setting ->{
                    val i = Intent(applicationContext,SettingActivity::class.java)
                    startActivity(i)
                    return@setOnItemSelectedListener true

                }
            }

            false


        }
        }

}