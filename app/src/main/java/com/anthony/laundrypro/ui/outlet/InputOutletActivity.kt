package com.anthony.laundrypro.ui.outlet

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.anthony.laundrypro.R
import com.anthony.laundrypro.entity.Outlet
import com.anthony.laundrypro.helper.CodePref
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class InputOutletActivity : AppCompatActivity() {
    private lateinit var edtPhone: TextInputEditText
    private lateinit var edtName: TextInputEditText
    private lateinit var edtCode: TextInputEditText
    private lateinit var edtAddress: TextInputEditText
    private lateinit var btnSave: Button
    private lateinit var btnChoose: Button
    private lateinit var btnDel: ImageButton
    private var imageUrl:String?=null
    private lateinit var mData: DatabaseReference
    private lateinit var imgOutlet:ImageView
    private var mImageUri: Uri? = null
    private var person : String =""
    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            person = it.data?.data.toString()
            mImageUri = Uri.parse(person)
            if (mImageUri != null) {
                imgOutlet.visibility = View.VISIBLE
                imgOutlet.setImageURI(mImageUri)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_outlet)
        edtName = findViewById(R.id.edt_outlet_name)
        edtCode = findViewById(R.id.edt_code_outlet)
        edtPhone = findViewById(R.id.edt_phone)
        imgOutlet = findViewById(R.id.image_outlet)
        edtAddress = findViewById(R.id.edt_outlet_address)
        btnSave = findViewById(R.id.btn_save)
        btnDel = findViewById(R.id.btn_del)
        btnChoose = findViewById(R.id.btn_choose)

        btnChoose.setOnClickListener {
            selectImage()
        }

        val codeOutlet = intent.getStringExtra("CODE_OUTLET")
        val name = intent.getStringExtra("NAME")
        val address = intent.getStringExtra("ADDRESS")
        val phone = intent.getStringExtra("PHONE")
        imageUrl = intent.getStringExtra("IMAGE_OUTLET")

        if (codeOutlet!=null){
            edtCode.setText(codeOutlet)
            edtName.setText(name)
            edtAddress.setText(address)
            edtPhone.setText(phone)
            edtCode.isEnabled = false
            Glide.with(applicationContext)
                .load(imageUrl)
                .override(100,100)
                .into(imgOutlet)

        }else{
            btnDel.visibility = View.INVISIBLE
        }

        btnDel.setOnClickListener {
            if (codeOutlet!!.isEmpty()){
                Toast.makeText(applicationContext,"Maaf tidak ada data!!",Toast.LENGTH_SHORT).show()
            }else{
                showAlertDialog(codeOutlet)
            }
        }


        btnSave.setOnClickListener {
            saveOutlet(person)
        }
    }

    private fun selectImage() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        getResult.launch(galleryIntent)
    }

    private fun showAlertDialog(codeOutlet:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Hapus Outlet")
        builder.setMessage("Yakin akan dihapus")

        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            mData = FirebaseDatabase.getInstance().getReference("laundryPro")
            mData.child(codeOutlet).removeValue()
            Toast.makeText(applicationContext,
                "Data berhasil dihapus!!", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton(getString(R.string.no)) { _, _ ->

            Toast.makeText(applicationContext,
                "Data tidak dihapus!!", Toast.LENGTH_SHORT).show()

        }

        builder.setNeutralButton("Cancel") { _, _ ->

        }
        builder.show()
    }

    private fun saveOutlet(imageUri:String) {
        val name = edtName.text.toString()
        val code = edtCode.text.toString()
        val address = edtAddress.text.toString()
        val phone = edtPhone.text.toString()

        if (name.isEmpty()){
            edtName.error = getString(R.string.empty)
        }else if (code.isEmpty()){
            edtCode.error = getString(R.string.empty)
        }else if (address.isEmpty()){
            edtAddress.error = getString(R.string.empty)
        }else if (phone.isEmpty()){
            edtPhone.error = getString(R.string.empty)
        }else if (imageUri.isEmpty()) {
            val cdPref = CodePref(applicationContext)
            val codeOwner = cdPref.owner
            mData = FirebaseDatabase.getInstance("https://laundry-87068-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("laundryPro")
            val out = Outlet()
            out.outlet_name = name
            out.outlet_code = code
            out.outlet_address =address
            out.phone =phone
            out.imageUrl = imageUrl
            cdPref.outlet = code
            mData.child(codeOwner).child("outlet").child(code).child("outlet_name").setValue(name)
            mData.child(codeOwner).child("outlet").child(code).child("outlet_code").setValue(code)
            mData.child(codeOwner).child("outlet").child(code).child("outlet_address").setValue(address)
            mData.child(codeOwner).child("outlet").child(code).child("phone").setValue(phone)
            mData.child(codeOwner).child("outlet").child(code).child("imageUrl").setValue(imageUrl)
            Toast.makeText(applicationContext,"Outlet berhasil ditambahkan!!", Toast.LENGTH_SHORT).show()
            finish()

        }else{
            val uri = Uri.parse(imageUri)
            val fileName = UUID.randomUUID().toString() +".jpg"
            val cdPref = CodePref(applicationContext)
            val codeOwner = cdPref.owner
            mData = FirebaseDatabase.getInstance("https://laundry-87068-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("laundryPro")
            val refStorage = FirebaseStorage.getInstance().reference.child("banner/$fileName")
            refStorage.putFile(uri).addOnSuccessListener {data->
            data.storage.downloadUrl.addOnSuccessListener {
                val imageUrl = it.toString()
                val out = Outlet()
                out.outlet_name = name
                out.outlet_code = code
                out.outlet_address =address
                out.phone =phone
                out.imageUrl = imageUrl
                cdPref.outlet = code
                mData.child(codeOwner).child("outlet").child(code).child("outlet_name").setValue(name)
                mData.child(codeOwner).child("outlet").child(code).child("outlet_code").setValue(code)
                mData.child(codeOwner).child("outlet").child(code).child("outlet_address").setValue(address)
                mData.child(codeOwner).child("outlet").child(code).child("phone").setValue(phone)
                mData.child(codeOwner).child("outlet").child(code).child("imageUrl").setValue(imageUrl)
                Toast.makeText(applicationContext,"Outlet berhasil ditambahkan!!", Toast.LENGTH_SHORT).show()
                finish()
            }

            }
        }
    }
}