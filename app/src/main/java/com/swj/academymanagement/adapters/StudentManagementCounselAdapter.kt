package com.swj.academymanagement.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.swj.academymanagement.databinding.RecyclerItemStudentManagementCounselBinding
import com.swj.academymanagement.model.StudentManagementCounsel

class StudentManagementCounselAdapter(val context:Context, val counselArr:MutableList<StudentManagementCounsel>)
    : Adapter<StudentManagementCounselAdapter.VH>() {

    inner class VH(val binding:RecyclerItemStudentManagementCounselBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemStudentManagementCounselBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = counselArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val counsel = counselArr[position]
        holder.binding.tvDate.text = counsel.date
        holder.binding.tvName.text = counsel.name
        holder.binding.tvCounselCentent.text = counsel.content
    }
}