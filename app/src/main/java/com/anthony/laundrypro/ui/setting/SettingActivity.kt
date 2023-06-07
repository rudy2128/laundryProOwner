package com.anthony.laundrypro.ui.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.anthony.laundrypro.R
import com.anthony.laundrypro.helper.MyPref
import com.anthony.laundrypro.ui.MainActivity
import com.anthony.laundrypro.ui.employee.EmployeeActivity
import com.anthony.laundrypro.ui.login.LoginActivity
import com.anthony.laundrypro.ui.outlet.OutletActivity
import com.anthony.laundrypro.ui.reg.UpdateOwnActivity
import com.anthony.laundrypro.ui.slider.InputBannerActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingActivity : AppCompatActivity() {
    private lateinit var cdProf:CardView
    private lateinit var cdLogout:CardView
    private lateinit var cdBanner:CardView
    private lateinit var btnNav: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        cdProf = findViewById(R.id.card_profile)
        cdBanner = findViewById(R.id.card_banner)
        cdLogout = findViewById(R.id.card_logout)
        btnNav = findViewById(R.id.bottom_navigation)

        cdProf.setOnClickListener {
            val i = Intent(applicationContext, UpdateOwnActivity::class.java)
            startActivity(i)
        }

        cdLogout.setOnClickListener {
            showAlertDialog()
        }

        cdBanner.setOnClickListener {
            val i = Intent(applicationContext, InputBannerActivity::class.java)
            startActivity(i)
            finish()
        }


        btnNav.setOnItemSelectedListener {item->
            when(item.itemId){
                R.id.page_home ->{
                    val i = Intent(applicationContext, MainActivity::class.java)
                    startActivity(i)
                    finish()
                    return@setOnItemSelectedListener true
                }
                R.id.page_employee ->{
                    val i = Intent(applicationContext, EmployeeActivity::class.java)
                    startActivity(i)
                    finish()
                    return@setOnItemSelectedListener true

                }
                R.id.page_outlet->{
                    val i = Intent(applicationContext, OutletActivity::class.java)
                    startActivity(i)
                    finish()
                    return@setOnItemSelectedListener true

                }
                R.id.page_setting ->{
                    return@setOnItemSelectedListener true

                }
            }

            false


        }
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Hapus")
        builder.setMessage("Yakin akan keluar?")

        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            val myPref = MyPref(applicationContext)
            myPref.deleteMyPref()
            val i = Intent(applicationContext, LoginActivity::class.java)
            startActivity(i)
            Toast.makeText(applicationContext,
                "Berhasil logout!!", Toast.LENGTH_SHORT).show()
            finish()
        }

        builder.setNegativeButton(getString(R.string.no)) { _, _ ->
            Toast.makeText(applicationContext,
                "Cancel logout", Toast.LENGTH_SHORT).show()

        }

        builder.setNeutralButton("Cancel") { _, _ ->
            Toast.makeText(applicationContext,
                "Cancel logout", Toast.LENGTH_SHORT).show()

        }
        builder.show()


    }

}