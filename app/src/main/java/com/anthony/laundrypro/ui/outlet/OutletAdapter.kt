package com.anthony.laundrypro.ui.outlet

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anthony.laundrypro.R
import com.anthony.laundrypro.entity.Outlet
import com.anthony.laundrypro.ui.MainActivity

class OutletAdapter(private val context: Context,private val outlets:ArrayList<Outlet>):
    RecyclerView.Adapter<OutletAdapter.OutViewHolder>() {

    class OutViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tv_name)
        val tvCode: TextView = view.findViewById(R.id.tv_code)
        val tvAddress: TextView = view.findViewById(R.id.tv_address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_outlet,parent,false)
        return OutViewHolder(view)
    }

    override fun getItemCount(): Int {
       return outlets.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OutViewHolder, position: Int) {
        val out = outlets[position]
        holder.tvCode.text = out.outlet_code
        holder.tvName.text = out.outlet_name
        holder.tvAddress.text = out.outlet_address


        holder.itemView.setOnClickListener {
            val i = Intent(context,DetailOutletActivity::class.java)
            i.putExtra("CODE",out.outlet_code)
            i.putExtra("NAME",out.outlet_name)
            i.putExtra("ADDRESS",out.outlet_address)
            context.startActivity(i)
        }
    }
}