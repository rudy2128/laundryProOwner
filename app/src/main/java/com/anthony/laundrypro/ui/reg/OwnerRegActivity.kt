package com.anthony.laundrypro.ui.reg

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.anthony.laundrypro.R
import com.anthony.laundrypro.entity.Outlet
import com.anthony.laundrypro.entity.User
import com.anthony.laundrypro.helper.CodePref
import com.anthony.laundrypro.helper.MyPref
import com.anthony.laundrypro.ui.login.LoginActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

class OwnerRegActivity : AppCompatActivity() {
    private lateinit var edtOwnName:TextInputEditText
    private lateinit var edtOwnCode:TextInputEditText
    private lateinit var edtOwnPhone:TextInputEditText
    private lateinit var edtOwnEmail:TextInputEditText
    private lateinit var edtOwnAddress:TextInputEditText
    private lateinit var edtOwnPass1:TextInputEditText
    private lateinit var edtOwnPass2:TextInputEditText
    private lateinit var btnReg:Button
    private lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_reg)

        edtOwnName = findViewById(R.id.edt_name)
        edtOwnCode = findViewById(R.id.edt_code_owner)
        edtOwnPhone = findViewById(R.id.edt_phone)
        edtOwnEmail = findViewById(R.id.edt_email)
        edtOwnAddress = findViewById(R.id.edt_address)
        edtOwnPass1 = findViewById(R.id.edt_password)
        edtOwnPass2 = findViewById(R.id.edt_password2)
        btnReg = findViewById(R.id.btn_reg)

        val phone = intent.getStringExtra("PHONE")
        edtOwnPhone.setText(phone)


        btnReg.setOnClickListener {
            val phone = edtOwnPhone.text.toString()
            val name = edtOwnName.text.toString()
            val email = edtOwnEmail.text.toString()
            val address = edtOwnAddress.text.toString()
            val code = edtOwnCode.text.toString()
            val password = edtOwnPass1.text.toString()
            val password2 = edtOwnPass2.text.toString()
            val status = "owner"

            if(phone.isEmpty()){
                edtOwnPhone.error = getString(R.string.empty)
            }else if (name.isEmpty()){
                edtOwnName.error = getString(R.string.empty)
            }else if (email.isEmpty()){
                edtOwnEmail.error = getString(R.string.empty)
            }else if (address.isEmpty()){
                edtOwnAddress.error = getString(R.string.empty)
            }else if (code.isEmpty()){
                edtOwnCode.error = getString(R.string.empty)
            }else if (password.isEmpty()){
                edtOwnPass1.error = getString(R.string.empty)
            }else if (password2.isEmpty()){
                edtOwnPass2.error = getString(R.string.empty)
            }else if (password != password2){
                Toast.makeText(applicationContext,"Password tidak sama!!",Toast.LENGTH_SHORT).show()
            }else{
                mDatabase = FirebaseDatabase.getInstance("https://laundry-87068-default-rtdb.asia-southeast1.firebasedatabase.app").reference

                val user = User()
                user.phone =phone
                user.name = name
                user.email = email
                user.address = address
                user.password = password
                user.status = status
                user.owner_code = code
                mDatabase.child("laundryPro").child(code).setValue(user)
                Toast.makeText(applicationContext,"Pandaftaran berhasil.",Toast.LENGTH_SHORT).show()
                val i = Intent(applicationContext,LoginActivity::class.java)
                startActivity(i)
                finish()
            }





        }

    }


}


