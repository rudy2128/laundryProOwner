package com.anthony.laundrypro.ui.login

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.anthony.laundrypro.R
import com.anthony.laundrypro.helper.CodePref
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*

class LoginEmpActivity : AppCompatActivity() {
    private lateinit var edtPhone: TextInputEditText
    private lateinit var edtPass: TextInputEditText
    private lateinit var edtCode: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var btnReg: Button
    private lateinit var mDatabase: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_emp)
        edtPhone= findViewById(R.id.edt_phone)
        edtPass= findViewById(R.id.edt_password)
        edtCode= findViewById(R.id.edt_code)
        btnLogin = findViewById(R.id.btn_login)

        btnLogin.setOnClickListener {
            val phone = edtPhone.text.toString()
            val password = edtPass.text.toString()
            val code = edtCode.text.toString()

            if (phone.isEmpty()){
                edtPhone.error = getString(R.string.empty)
            }else if(password.isEmpty()){
                edtPass.error = getString(R.string.empty)
            }else if(code.isEmpty()){
                edtCode.error = getString(R.string.empty)
            }else{
                val cdPref = CodePref(applicationContext)
                val codeOwner = cdPref.owner
                mDatabase = FirebaseDatabase.getInstance("https://laundry-87068-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("laundryPro")
                val query = mDatabase.child(codeOwner).child("outlet").child(code).child("phone").equalTo(phone)

                query.addListenerForSingleValueEvent(object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            nextQuery(password,phone,code)
                        }else{
                            Toast.makeText(applicationContext,"No handphone belum terdaftar!!",
                                Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.w(ContentValues.TAG, "loadData:onCancelled", error.toException())
                    }

                })
            }
        }
    }

    private fun nextQuery(password: String, phone: String, code: String) {
        mDatabase = FirebaseDatabase.getInstance("https://laundry-87068-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("laundryPro")
        val query = mDatabase.child("rs123").child("outlet").child(code).child("password").equalTo(password)
        query.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}