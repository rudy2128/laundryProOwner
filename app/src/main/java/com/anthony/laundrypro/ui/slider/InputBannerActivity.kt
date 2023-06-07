package com.anthony.laundrypro.ui.slider

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.anthony.laundrypro.R
import com.anthony.laundrypro.helper.CodePref
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class InputBannerActivity : AppCompatActivity() {
    private lateinit var imgBanner:ImageView
    private lateinit var btnChoose:Button
    private lateinit var btnSave:Button
    private lateinit var btnBack:ImageButton
    private lateinit var mData:DatabaseReference
    private var mImageUri: Uri? = null
    private var person : String =""
    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            person = it.data?.data.toString()
            mImageUri = Uri.parse(person)
            if (mImageUri != null) {
                imgBanner.visibility = View.VISIBLE
                imgBanner.setImageURI(mImageUri)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_banner)
        imgBanner = findViewById(R.id.image_banner)
        btnChoose = findViewById(R.id.btn_choose)
        btnSave = findViewById(R.id.btn_save)
        btnBack = findViewById(R.id.btn_back)

        btnBack.setOnClickListener {
            finish()
        }

        btnChoose.setOnClickListener {
            selectImage()
        }


        btnSave.setOnClickListener {
            saveBanner(person)
        }

    }

    private fun selectImage() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        getResult.launch(galleryIntent)
    }

    private fun saveBanner(imageUri:String) {
        if (imageUri.isEmpty()){
            Toast.makeText(applicationContext,"Pilih gambar dulu!!",Toast.LENGTH_SHORT).show()
        }else{
            val uri = Uri.parse(imageUri)
            val fileName = UUID.randomUUID().toString() +".jpg"
            val codePref = CodePref(applicationContext)
            val codeOwner = codePref.owner
            mData = FirebaseDatabase.getInstance("https://laundry-87068-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("laundryPro")
            val refStorage = FirebaseStorage.getInstance().reference.child("banner/$fileName")
            refStorage.putFile(uri).addOnSuccessListener {data->
                data.storage.downloadUrl.addOnSuccessListener {
                    val imageUrl = it.toString()
                    val key = mData.push().key.toString()
                    mData.child(codeOwner).child("banner").child(key).setValue(imageUrl)
                    Toast.makeText(applicationContext,"Data berhasil disimpan!!", Toast.LENGTH_SHORT).show()
                    finish()
                }

            }


        }

    }
}