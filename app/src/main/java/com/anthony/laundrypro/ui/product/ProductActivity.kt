package com.anthony.laundrypro.ui.product

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anthony.laundrypro.R
import com.anthony.laundrypro.entity.Product
import com.anthony.laundrypro.helper.CodePref
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class ProductActivity : AppCompatActivity() {
    private lateinit var mData: DatabaseReference
    private lateinit var rvPro:RecyclerView
    private lateinit var btnAdd:FloatingActionButton
    private lateinit var adapter: ProAdapter
    private lateinit var searchView: SearchView
    private lateinit var tvQty:TextView
    private var products = arrayListOf<Product>()
    private lateinit var btnBack:ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        rvPro = findViewById(R.id.rvPro)
        searchView = findViewById(R.id.search_product)
        tvQty = findViewById(R.id.tv_qty_service)
        btnAdd = findViewById(R.id.btn_add)
        btnBack = findViewById(R.id.btn_back)

        btnBack.setOnClickListener {
            finish()
        }

        val codeOutlet = intent.getStringExtra("CODE_OUTLET").toString()

        btnAdd.setOnClickListener {
            val i = Intent(applicationContext,InputUpdateActivity::class.java)
            i.putExtra("CODE_PRODUCT",codeOutlet)
            startActivity(i)

        }


        mData = FirebaseDatabase.getInstance("https://laundry-87068-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("laundryPro")

        rvPro.setHasFixedSize(false)
        rvPro.layoutManager = LinearLayoutManager(applicationContext)

        val cdPref = CodePref(applicationContext)
        val codeOwner = cdPref.owner


        adapter = ProAdapter(applicationContext,products)

        mData.child(codeOwner).child("outlet").child(codeOutlet).child("product")
            .addValueEventListener(object :ValueEventListener{
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        products.clear()
                        for (i in snapshot.children){
                            val count = snapshot.childrenCount
                            tvQty.text = "$count Layanan"
                            val pro = i.getValue(Product::class.java)
                            products.add(pro!!)
                        }
                    }
                    rvPro.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "loadData:onCancelled", error.toException())
                }

            })

        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchList(newText)
                return true
            }

        })



    }

    private fun searchList(text: String?) {
        val searchList = ArrayList<Product>()
        for (dataPro in products){
            if (dataPro.name?.lowercase()?.contains(text!!.lowercase()) == true ||
                dataPro.satuan?.lowercase()?.contains(text!!.lowercase()) == true ){
                searchList.add(dataPro)

            }
        }
        adapter.searchDataList(searchList)
    }
}