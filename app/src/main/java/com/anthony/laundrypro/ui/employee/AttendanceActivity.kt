package com.anthony.laundrypro.ui.employee

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anthony.laundrypro.R
import com.anthony.laundrypro.entity.Attendance
import com.anthony.laundrypro.helper.CodePref
import com.google.firebase.database.*

class AttendanceActivity : AppCompatActivity() {
    private lateinit var tvName:TextView
    private lateinit var tvPhone:TextView
    private lateinit var tvCome:TextView
    private lateinit var tvCode:TextView
    private lateinit var rvCome:RecyclerView
    private lateinit var btnBack:ImageButton
    private lateinit var mData:DatabaseReference
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)
        tvName = findViewById(R.id.tv_name)
        tvPhone = findViewById(R.id.tv_phone)
        tvCode = findViewById(R.id.tv_code_outlet)
        tvCome = findViewById(R.id.tv_come)
        rvCome = findViewById(R.id.rvCome)
        btnBack = findViewById(R.id.btn_back)

        btnBack.setOnClickListener {
            finish()
        }
        mData = FirebaseDatabase.getInstance("https://laundry-87068-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("laundryPro")
        val phone = intent.getStringExtra("PHONE")
        val name = intent.getStringExtra("NAME")
        val codeOutlet = intent.getStringExtra("CODE")

        tvName.text = name
        tvPhone.text = "62$phone"


        val cPref = CodePref(applicationContext)
        val codeOwner = cPref.owner

        mData.child(codeOwner).child("outlet").child(codeOutlet!!).child("outlet_name").get()
            .addOnSuccessListener {
                val outletName = it.value
                tvCode.text = outletName.toString()
            }

        rvCome.setHasFixedSize(false)
        rvCome.layoutManager = LinearLayoutManager(applicationContext)
        val attendanceList = arrayListOf<Attendance>()
        val adapter = AttdAdapter(attendanceList)

        mData.child(codeOwner).child("employee").child(phone.toString()).child("absen")
            .addValueEventListener(object:ValueEventListener{
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        attendanceList.clear()
                        for (i in snapshot.children){
                            val count = snapshot.childrenCount
                            tvCome.text = "$count hari"
                            val atList = i.getValue(Attendance::class.java)
                            attendanceList.add(atList!!)
                        }
                    }
                    rvCome.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "loadData:onCancelled", error.toException())
                }

            })




    }
}