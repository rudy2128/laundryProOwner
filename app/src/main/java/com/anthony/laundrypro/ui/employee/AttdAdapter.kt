package com.anthony.laundrypro.ui.employee

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anthony.laundrypro.R
import com.anthony.laundrypro.entity.Attendance

class AttdAdapter(private val attendanceList: ArrayList<Attendance>):
RecyclerView.Adapter<AttdAdapter.AtViewHolder>(){
    class AtViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val tvCome:TextView = view.findViewById(R.id.tv_date)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AtViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_abs,parent,false)
        return AtViewHolder(view)
    }

    override fun getItemCount(): Int {
       return attendanceList.size
    }

    override fun onBindViewHolder(holder: AtViewHolder, position: Int) {
        val abs = attendanceList[position]
        holder.tvCome.text = abs.dateTime

    }
}