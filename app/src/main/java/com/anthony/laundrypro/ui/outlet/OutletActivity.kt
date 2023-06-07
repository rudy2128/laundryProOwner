package com.anthony.laundrypro.ui.outlet

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anthony.laundrypro.R
import com.anthony.laundrypro.entity.Outlet
import com.anthony.laundrypro.helper.CodePref
import com.anthony.laundrypro.ui.MainActivity
import com.anthony.laundrypro.ui.employee.EmployeeActivity
import com.anthony.laundrypro.ui.employee.InputUpdateEmpActivity
import com.anthony.laundrypro.ui.help.HelpActivity
import com.anthony.laundrypro.ui.setting.SettingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*

class OutletActivity : AppCompatActivity() {
    private lateinit var mData:DatabaseReference
    private lateinit var rvOutlet:RecyclerView
    private lateinit var btnNav: BottomNavigationView
    private lateinit var btnAdd: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outlet)
        btnAdd = findViewById(R.id.btn_add)
        rvOutlet = findViewById(R.id.rvOutlet)
        btnNav = findViewById(R.id.bottom_navigation)

        val codePref = CodePref(applicationContext)
        val code = codePref.owner

        btnAdd.setOnClickListener {
            val i = Intent(applicationContext,InputOutletActivity::class.java)
            startActivity(i)
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
                    return@setOnItemSelectedListener true

                }
                R.id.page_setting ->{
                    val i = Intent(applicationContext, SettingActivity::class.java)
                    startActivity(i)
                    finish()
                    return@setOnItemSelectedListener true

                }
            }

            false


        }

        mData = FirebaseDatabase.getInstance("https://laundry-87068-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("laundryPro")

        rvOutlet.setHasFixedSize(false)
        rvOutlet.layoutManager = LinearLayoutManager(applicationContext)
        val outlets = arrayListOf<Outlet>()
        val adapter = OutletAdapter(this@OutletActivity,outlets)

        mData.child(code).child("outlet").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    outlets.clear()
                    for (i in snapshot.children){
                        val out = i.getValue(Outlet::class.java)
                        outlets.add(out!!)
                    }

                    rvOutlet.adapter = adapter

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "loadData:onCancelled", error.toException())
            }

        })
    }
}