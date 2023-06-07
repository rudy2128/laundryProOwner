package com.anthony.laundrypro.ui.outlet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.anthony.laundrypro.R
import com.anthony.laundrypro.entity.Outlet
import com.anthony.laundrypro.helper.CodePref
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InputOutletActivity : AppCompatActivity() {
    private lateinit var edtName: TextInputEditText
    private lateinit var edtCode: TextInputEditText
    private lateinit var edtAddress: TextInputEditText
    private lateinit var btnSave: Button
    private lateinit var btnDel: ImageButton
    private lateinit var mData: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_outlet)
        edtName = findViewById(R.id.edt_outlet_name)
        edtCode = findViewById(R.id.edt_code_outlet)
        edtAddress = findViewById(R.id.edt_outlet_address)
        btnSave = findViewById(R.id.btn_save)
        btnDel = findViewById(R.id.btn_del)

        val codeOutlet = intent.getStringExtra("CODE")
        val name = intent.getStringExtra("NAME")
        val address = intent.getStringExtra("ADDRESS")

        if (codeOutlet!=null){
            edtCode.setText(codeOutlet)
            edtName.setText(name)
            edtAddress.setText(address)
            edtCode.isEnabled = false

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
            saveOutlet()
        }
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

    private fun saveOutlet() {
        val name = edtName.text.toString()
        val code = edtCode.text.toString()
        val address = edtAddress.text.toString()

        if (name.isEmpty()){
            edtName.error = getString(R.string.empty)
        }else if (code.isEmpty()){
            edtCode.error = getString(R.string.empty)
        }else if (address.isEmpty()){
            edtAddress.error = getString(R.string.empty)
        }else{

            val cdPref = CodePref(applicationContext)
            val codeOwner = cdPref.owner

            mData = FirebaseDatabase.getInstance("https://laundry-87068-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("laundryPro")
            val out = Outlet()
            out.outlet_name = name
            out.outlet_code = code
            out.outlet_address =address
            cdPref.outlet = code
            mData.child(codeOwner).child("outlet").child(code).setValue(out)
            Toast.makeText(applicationContext,"Outlet berhasil ditambahkan!!", Toast.LENGTH_SHORT).show()
            val i = Intent(applicationContext,OutletActivity::class.java)
            startActivity(i)
            finish()

        }
    }
}