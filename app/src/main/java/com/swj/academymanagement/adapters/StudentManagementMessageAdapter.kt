package com.swj.academymanagement.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.swj.academymanagement.databinding.RecyclerItemStudentManagementMessageBinding
import com.swj.academymanagement.model.StudentManagementMessage

class StudentManagementMessageAdapter(val context:Context, val messageArr:MutableList<StudentManagementMessage>)
    :Adapter<StudentManagementMessageAdapter.VH>() {

    inner class VH(val binding: RecyclerItemStudentManagementMessageBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemStudentManagementMessageBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = messageArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val message = messageArr[position]
        holder.binding.tvDate.text = message.date
        holder.binding.tvMessage.text = message.message
    }


}