package com.anthony.laundrypro.ui.product

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anthony.laundrypro.R
import com.anthony.laundrypro.entity.Product
import com.anthony.laundrypro.helper.RupiahConvert

class ProAdapter(private val context: Context,private var products:List<Product>):
RecyclerView.Adapter<ProAdapter.ProViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product,parent,false)
        return ProViewHolder(view)
    }

    override fun getItemCount(): Int {
       return products.size
    }

    fun searchDataList(searchList:List<Product>){
        products = searchList
        notifyDataSetChanged()
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProViewHolder, position: Int) {
       val pro = products[position]
        holder.tvName.text = pro.name
        holder.tvPrice.text = RupiahConvert.formatRupiah(pro.price!!.toDouble()).toString()
        holder.tvQtyDesc.text = "per "+pro.satuan
        holder.tvDesc.text = "Minimal order: "+pro.minimalOrder.toString()
        holder.tvTime.text = pro.time.toString()+" hari"

        holder.itemView.setOnClickListener {
            val i = Intent(context,InputUpdateActivity::class.java)
            i.putExtra("NAME",pro.name)
            i.putExtra("PRICE",pro.price.toString())
            i.putExtra("QTY_DESC",pro.satuan)
            i.putExtra("DESC",pro.minimalOrder.toString())
            i.putExtra("TIME", pro.time.toString())
            i.putExtra("CODE_PRO",pro.codeProduct)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)

        }

    }
    class ProViewHolder(view: android.view.View):RecyclerView.ViewHolder(view) {
        val tvName:TextView = view.findViewById(R.id.tv_name)
        val tvPrice:TextView = view.findViewById(R.id.tv_price)
        val tvQtyDesc:TextView = view.findViewById(R.id.tv_qty_desc)
        val tvDesc:TextView = view.findViewById(R.id.tv_desc)
        val tvTime:TextView = view.findViewById(R.id.tv_time)


    }



}