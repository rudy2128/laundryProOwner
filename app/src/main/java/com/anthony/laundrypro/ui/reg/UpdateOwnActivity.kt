package com.anthony.laundrypro.ui.reg

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.anthony.laundrypro.R
import com.anthony.laundrypro.helper.CodePref
import com.anthony.laundrypro.helper.MyPref
import com.anthony.laundrypro.ui.camera.CameraActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class UpdateOwnActivity : AppCompatActivity() {
    private lateinit var edtName:TextInputEditText
    private lateinit var edtAddress:TextInputEditText
    private lateinit var tvPhone:TextView
    private lateinit var tvEmail:TextView
    private lateinit var tvCode:TextView
    private lateinit var imgPerson: ImageView
    private lateinit var btnPhoto: Button
    private lateinit var btnSave: Button
    private lateinit var mData: DatabaseReference
    private var mImageUri: Uri? = null
    private var person : String =""
    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            person = it.data?.getStringExtra("ImgPerson").toString()
            mImageUri = Uri.parse(person)
            if (mImageUri != null) {
                imgPerson.visibility = View.VISIBLE
                imgPerson.setImageURI(mImageUri)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_own)
        imgPerson = findViewById(R.id.img_profile)
        btnPhoto = findViewById(R.id.btn_photo)
        btnSave = findViewById(R.id.btn_save)
        edtName = findViewById(R.id.edt_name)
        edtAddress = findViewById(R.id.edt_address)
        tvPhone = findViewById(R.id.tv_phone)
        tvEmail = findViewById(R.id.tv_email)
        tvCode = findViewById(R.id.tv_code_owner)


        btnPhoto.setOnClickListener {
            val i = Intent(applicationContext,CameraActivity::class.java)
            getResult.launch(i)
        }


        btnSave.setOnClickListener {
            saveProfile(person)
        }


        val cdPref = CodePref(applicationContext)
        val myPref = MyPref(applicationContext)
        val codeOwner = cdPref.owner
        val phone = myPref.phone

        mData = FirebaseDatabase.getInstance("https://laundry-87068-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("laundryPro")
        mData.child(codeOwner).child("name").get().addOnSuccessListener {
            val name = it.value.toString()
            edtName.setText(name)
        }
        mData.child(codeOwner).child("address").get().addOnSuccessListener {
            val address = it.value.toString()
            edtAddress.setText(address)
        }
        mData.child(codeOwner).child("email").get().addOnSuccessListener {
            val email = it.value.toString()
            tvEmail.text = email
        }
        tvPhone.text = phone
        tvCode.text = codeOwner




    }

    private fun saveProfile(imageUri: String) {
        val uri = Uri.parse(imageUri)
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val name = edtName.text.toString()
        val address = edtAddress.text.toString()

        if (name.isEmpty()){
            edtName.error = getString(R.string.empty)
        }else if (address.isEmpty()){
            edtAddress.error = getString(R.string.empty)
        }else{
            val cdPref = CodePref(applicationContext)
            val codeOwner = cdPref.owner
            mData = FirebaseDatabase.getInstance("https://laundry-87068-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("laundryPro")
            val refStorage = FirebaseStorage.getInstance().reference.child("laundry/$fileName")
            refStorage.putFile(uri).addOnSuccessListener {data->
                data.storage.downloadUrl.addOnSuccessListener {
                    val imageUrl = it.toString()
                    mData.child(codeOwner).child("imageOwner").setValue(imageUrl)
                    mData.child(codeOwner).child("name").setValue(name)
                    mData.child(codeOwner).child("address").setValue(address)
                    Toast.makeText(applicationContext,"Data berhasil disimpan!!", Toast.LENGTH_SHORT).show()
                    finish()

                }

                mData.child(codeOwner).child("name").setValue(name)
                mData.child(codeOwner).child("address").setValue(address)
                Toast.makeText(applicationContext,"Data berhasil disimpan!!", Toast.LENGTH_SHORT).show()
                finish()
        }

        }
    }
}