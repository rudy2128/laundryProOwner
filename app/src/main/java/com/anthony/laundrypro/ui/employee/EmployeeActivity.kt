package com.anthony.laundrypro.ui.employee

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
import com.anthony.laundrypro.entity.Employee
import com.anthony.laundrypro.helper.CodePref
import com.anthony.laundrypro.ui.MainActivity
import com.anthony.laundrypro.ui.help.HelpActivity
import com.anthony.laundrypro.ui.outlet.OutletActivity
import com.anthony.laundrypro.ui.setting.SettingActivity
import com.firebase.ui.auth.data.model.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class EmployeeActivity : AppCompatActivity() {
    private lateinit var mData: DatabaseReference
    private lateinit var rvWorker: RecyclerView
    private lateinit var btnAdd: View
    private lateinit var btnNav: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee)
        rvWorker = findViewById(R.id.rvWorker)
        btnAdd = findViewById(R.id.btn_add)
        btnNav = findViewById(R.id.bottom_navigation)

        rvWorker.setHasFixedSize(false)
        rvWorker.layoutManager = LinearLayoutManager(applicationContext)

        val employees = arrayListOf<Employee>()
        val adapter = EmpAdapter(this@EmployeeActivity,employees)

        val codePref = CodePref(applicationContext)
        val codeOwner = codePref.owner
        val codeOutlet = codePref.outlet

        mData = FirebaseDatabase.getInstance("https://laundry-87068-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("laundryPro")
        mData.child(codeOwner).child("employee")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        employees.clear()
                        for (i in snapshot.children){
                            val emp = i.getValue(Employee::class.java)
                            employees.add(emp!!)
                        }
                    }
                    rvWorker.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "loadData:onCancelled", error.toException())
                }

            })
        btnAdd.setOnClickListener {
            val i = Intent(applicationContext,InputUpdateEmpActivity::class.java)
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
                    return@setOnItemSelectedListener true

                }
                R.id.page_outlet->{
                    val i = Intent(applicationContext, OutletActivity::class.java)
                    startActivity(i)
                    finish()
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
    }

}