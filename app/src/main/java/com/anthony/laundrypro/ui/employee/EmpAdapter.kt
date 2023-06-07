package com.anthony.laundrypro.ui.employee

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anthony.laundrypro.R
import com.anthony.laundrypro.entity.Employee
import com.anthony.laundrypro.ui.outlet.OutletAdapter

class EmpAdapter(private val context: Context,private val employees:ArrayList<Employee>):
RecyclerView.Adapter<EmpAdapter.EmpViewHolder>(){
    class EmpViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tv_name)
        val tvPhone: TextView = view.findViewById(R.id.tv_phone)
        val tvStatus: TextView = view.findViewById(R.id.tv_status)
        val tvCode: TextView = view.findViewById(R.id.tv_code_outlet)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmpViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_employee,parent,false)
        return EmpViewHolder(view)
    }

    override fun getItemCount(): Int {
       return employees.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: EmpViewHolder, position: Int) {
        val emp = employees[position]
        holder.tvName.text = emp.name
        holder.tvStatus.text = emp.status
        holder.tvPhone.text = "0"+emp.phone
        holder.tvCode.text = "Outlet "+emp.outlet_code
        holder.itemView.setOnClickListener {
            val i = Intent(context,InputUpdateEmpActivity::class.java)
            i.putExtra("NAME",emp.name)
            i.putExtra("PHONE",emp.phone)
            i.putExtra("ADDRESS",emp.address)
            i.putExtra("PASSWORD",emp.password)
            i.putExtra("CODE",emp.outlet_code)
            i.putExtra("JOB",emp.status)
            holder.itemView.context.startActivity(i)
        }

    }


}