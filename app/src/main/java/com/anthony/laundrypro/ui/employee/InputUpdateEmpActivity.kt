package com.anthony.laundrypro.ui.employee

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.anthony.laundrypro.R
import com.anthony.laundrypro.entity.Employee
import com.anthony.laundrypro.entity.Outlet
import com.anthony.laundrypro.helper.CodePref
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*

class InputUpdateEmpActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var edtName:TextInputEditText
    private lateinit var edtPhone:TextInputEditText
    private lateinit var edtAddress:TextInputEditText
    private lateinit var edtPass:TextInputEditText
    private lateinit var edtPass2:TextInputEditText
    private lateinit var edtJob:TextInputEditText
    private lateinit var btnSave:Button
    private lateinit var btnCome:Button
    private lateinit var btnDel:ImageButton
    private lateinit var mData:DatabaseReference
    private lateinit var spOutlet:Spinner
    private var listOutlet = ArrayList<String>()
    private var listCodeOutlet = ArrayList<String>()
    private var listNameOutlet = ArrayList<String>()
    private lateinit var outletCode:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_update_emp)
        edtName = findViewById(R.id.edt_name)
        edtPhone = findViewById(R.id.edt_phone)
        edtAddress = findViewById(R.id.edt_address)
        edtPass = findViewById(R.id.edt_password)
        edtPass2 = findViewById(R.id.edt_password2)
        edtJob = findViewById(R.id.edt_job)
        btnSave = findViewById(R.id.btn_save)
        btnDel = findViewById(R.id.btn_del)
        btnCome = findViewById(R.id.btn_come)
        spOutlet = findViewById(R.id.sp_outlet)




        val phone = intent.getStringExtra("PHONE")
        val name = intent.getStringExtra("NAME")
        val address = intent.getStringExtra("ADDRESS")
        val password = intent.getStringExtra("PASSWORD")
        val status = intent.getStringExtra("JOB")
        val codeOutlet = intent.getStringExtra("CODE")

        btnCome.setOnClickListener {
            val i = Intent(applicationContext,AttendanceActivity::class.java)
            i.putExtra("PHONE",phone)
            i.putExtra("NAME",name)
            i.putExtra("JOB",status)
            i.putExtra("CODE",codeOutlet)
            startActivity(i)
        }

        if (phone!=null){
            edtName.setText(name)
            edtPhone.setText(phone)
            edtAddress.setText(address)
            edtPass.setText(password)
            edtPass2.setText(password)
            edtJob.setText(status)
            edtPhone.isEnabled = false
            edtPass.isEnabled = false
            edtPass2.isEnabled = false

        }else{
            btnDel.visibility = View.INVISIBLE
        }

        val codePref = CodePref(applicationContext)
        val codeOwner = codePref.owner
        showOutlet(codeOwner)





        btnDel.setOnClickListener {
            showAlertDialog(codeOwner, phone.toString())
        }


        btnSave.setOnClickListener {
            saveOutlet()
        }
    }


    private fun showOutlet(codeOwner:String) {
        mData = FirebaseDatabase.getInstance("https://laundry-87068-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("laundryPro")
        mData.child(codeOwner).child("outlet").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (i in snapshot.children){
                        val out = i.getValue(Outlet::class.java)
                        val name = out!!.outlet_name.toString()
                        val outletCode = out.outlet_code.toString()

                        listNameOutlet.add(name)
                        listCodeOutlet.add(outletCode)

                        spOutlet.onItemSelectedListener = this@InputUpdateEmpActivity
                        val adapter = ArrayAdapter(this@InputUpdateEmpActivity,android.R.layout.simple_spinner_dropdown_item,listNameOutlet)
                        spOutlet.adapter =adapter
                        listOutlet.add(0,"Pilih outlet")

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "loadData:onCancelled", error.toException())
            }

        })

    }

    private fun showAlertDialog(codeOwner:String,phone:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Hapus Outlet")
        builder.setMessage("Yakin akan dihapus")

        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            mData = FirebaseDatabase.getInstance("https://laundry-87068-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("laundryPro")
            mData.child(codeOwner).child("employee").child(phone).removeValue()
            Toast.makeText(applicationContext,
                "Data $phone berhasil dihapus!!", Toast.LENGTH_SHORT).show()
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
            spOutlet.selectedItem ->{
                outletCode = listCodeOutlet[position]

            }

        }


    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
    private fun saveOutlet() {
        val name = edtName.text.toString()
        val phone = edtPhone.text.toString()
        val address = edtAddress.text.toString()
        val password = edtPass.text.toString()
        val password2 = edtPass2.text.toString()
        val status = edtJob.text.toString()

        if (name.isEmpty()){
            edtName.error = getString(R.string.empty)
        }else if (phone.isEmpty()){
            edtPhone.error = getString(R.string.empty)
        }else if (status.isEmpty()){
            edtJob.error = getString(R.string.empty)
        }else if (password.isEmpty()){
            edtPass.error = getString(R.string.empty)
        }else if (password2.isEmpty()){
            edtPass2.error = getString(R.string.empty)
        }else if (address.isEmpty()){
            edtAddress.error = getString(R.string.empty)
        }else{

            val cdPref = CodePref(applicationContext)
            val codeOwner = cdPref.owner

            mData = FirebaseDatabase.getInstance("https://laundry-87068-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("laundryPro")
            val emp = Employee()
            emp.phone = phone
            emp.name = name
            emp.outlet_code = outletCode
            emp.address =address
            emp.status = status
            emp.password =password
            mData.child(codeOwner).child("employee").child(phone).setValue(emp)
            Toast.makeText(applicationContext,"Data berhasil ditambahkan!!",Toast.LENGTH_SHORT).show()
            val i = Intent(applicationContext,EmployeeActivity::class.java)
            startActivity(i)
            finish()

        }
    }
}