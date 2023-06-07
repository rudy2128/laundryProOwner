package com.anthony.laundrypro.ui.product

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.anthony.laundrypro.R
import com.anthony.laundrypro.entity.Product
import com.anthony.laundrypro.helper.CodePref
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*

class CopyActivity : AppCompatActivity() {
    private lateinit var mData:DatabaseReference
    private lateinit var edtCode:TextInputEditText
    private lateinit var btnSave:Button
    private lateinit var btnBack:ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_copy)
        edtCode = findViewById(R.id.edt_code_outlet)
        btnSave = findViewById(R.id.btn_save)
        btnBack = findViewById(R.id.btn_back)

        btnBack.setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {
            copyService()
        }
    }

    private fun copyService() {
        val cdPref = CodePref(applicationContext)
        val codeOwner = cdPref.owner
        val codeOutletThis = cdPref.outlet
         val codeOutlet = edtCode.text.toString()
        if (codeOutlet.isEmpty()){
            edtCode.error = getString(R.string.empty)
        }else{
            mData = FirebaseDatabase.getInstance("https://laundry-87068-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("laundryPro")
            mData.child(codeOwner).child("outlet").child(codeOutlet).child("product")
                .addValueEventListener(object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            for (i in snapshot.children){
                                val products = arrayListOf<Product>()
                                val pro = i.getValue(Product::class.java)
                                val codeProduct = pro?.codeProduct
                                val name = pro?.name
                                val  price= pro?.price
                                val  time= pro?.time
                                val  satuan= pro?.satuan
                                val  minimalOrder= pro?.minimalOrder
                                val product = Product(codeProduct,name,price,time,satuan,minimalOrder)
                                products.add(pro!!)
                                mData.child(codeOwner).child("outlet").child(codeOutletThis).child("product").child(codeProduct.toString()).setValue(product)
                                Toast.makeText(applicationContext,"Data berhasil dicopy",Toast.LENGTH_SHORT).show()
                                finish()

                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.w(ContentValues.TAG, "loadData:onCancelled", error.toException())
                    }

                })


        }
    }
}