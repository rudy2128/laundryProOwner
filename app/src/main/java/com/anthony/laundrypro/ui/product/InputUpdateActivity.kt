package com.anthony.laundrypro.ui.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.anthony.laundrypro.R
import com.anthony.laundrypro.R.array.listSatuan
import com.anthony.laundrypro.entity.Product
import com.anthony.laundrypro.helper.CodePref
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InputUpdateActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var mData:DatabaseReference
    private lateinit var edtName:EditText
    private lateinit var edtTime:TextInputEditText
    private lateinit var edtDesc:TextInputEditText
    private lateinit var edtPrice:EditText
    private lateinit var edtCode:EditText
    private lateinit var spPcs: Spinner
    private lateinit var btnSave:Button
    private lateinit var btnDel:ImageButton
    private lateinit var btnBack:ImageButton
    private lateinit var satuan:String
    private var listPcs = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_update)
        edtName = findViewById(R.id.edt_name)
        edtTime = findViewById(R.id.edt_time)
        edtDesc = findViewById(R.id.edt_min_order)
        edtPrice = findViewById(R.id.edt_price)
        spPcs = findViewById(R.id.sp_pcs)
        edtCode = findViewById(R.id.edt_code)
        btnSave = findViewById(R.id.btn_save)
        btnDel = findViewById(R.id.btn_del)
        btnBack = findViewById(R.id.btn_back)

        btnBack.setOnClickListener {
            finish()
        }

        val cdPref = CodePref(applicationContext)
        val codeOwner = cdPref.owner
        val codeOutlet = cdPref.outlet

        val codeProduct = intent.getStringExtra("CODE_PRO")
        val name = intent.getStringExtra("NAME")
        val time = intent.getStringExtra("TIME")
        val qtyDesc = intent.getStringExtra("QTY_DESC")
        val desc = intent.getStringExtra("DESC")
        val price = intent.getStringExtra("PRICE")

        if (codeProduct!=null){
            edtName.setText(name)
            edtTime.setText(time)
            edtDesc.setText(desc)
            edtPrice.setText(price)
            edtCode.setText(codeProduct)
            edtCode.isEnabled = false

        }else{
            btnDel.visibility = View.INVISIBLE
        }

        btnDel.setOnClickListener {
            shoAlertDialog(codeOwner,codeOutlet,codeProduct!!)
        }
        btnSave.setOnClickListener {
            saveProduct()
        }

        spPcs.onItemSelectedListener = this@InputUpdateActivity
        val list =resources.getStringArray(listSatuan)
        listPcs.addAll(list)
        val adapter = ArrayAdapter(this@InputUpdateActivity,android.R.layout.simple_spinner_dropdown_item,listPcs)
        spPcs.adapter =adapter
    }

    private fun saveProduct() {
        mData = FirebaseDatabase.getInstance("https://laundry-87068-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("laundryPro")
        val name = edtName.text.toString()
        val time = edtTime.text.toString()
        val desc = edtDesc.text.toString()
        val price = edtPrice.text.toString()
        val codeProduct = edtCode.text.toString()

        if (name.isEmpty()){
            edtName.error = getString(R.string.empty)
        }else if (time.isEmpty()){
            edtTime.error = getString(R.string.empty)
        }else if(desc.isEmpty()){
            edtDesc.error = getString(R.string.empty)
        }else if(codeProduct.isEmpty()){
            edtCode.error = getString(R.string.empty)
        }else if (price.isEmpty()){
            edtPrice.error = getString(R.string.empty)
        }else{
            val cdPref = CodePref(applicationContext)
            val codeOutlet = cdPref.outlet
            val codeOwner = cdPref.owner
            val pro = Product()
            pro.name = name
            pro.codeProduct = codeProduct
            pro.price = price.toInt()
            pro.time = time.toInt()
            pro.satuan = satuan
            pro.minimalOrder = desc
            mData.child(codeOwner).child("outlet").child(codeOutlet.toString()).child("product").child(codeProduct).setValue(pro)

            Toast.makeText(applicationContext,"Data berhasil ditambahkan.",Toast.LENGTH_SHORT).show()
            finish()

        }


    }

    private fun shoAlertDialog(codeOwner:String,codeOutlet:String,codeProduct:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Hapus Outlet")
        builder.setMessage("Yakin akan dihapus")

        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            mData = FirebaseDatabase.getInstance("https://laundry-87068-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("laundryPro")
            mData.child(codeOwner).child("outlet").child(codeOutlet).child("product").child(codeProduct).removeValue()
            Toast.makeText(applicationContext,
                "Data $codeProduct berhasil dihapus!!", Toast.LENGTH_SHORT).show()
            finish()
        }

        builder.setNegativeButton(getString(R.string.no)) { _, _ ->

            Toast.makeText(applicationContext,
                "Data tidak dihapus!!", Toast.LENGTH_SHORT).show()

        }

        builder.setNeutralButton("Cancel") { _, _ ->

        }
        builder.show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        parent!!.getItemAtPosition(position)
        when(parent.selectedItem){
            spPcs.selectedItem ->{
                satuan = listPcs[position]

            }

        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}