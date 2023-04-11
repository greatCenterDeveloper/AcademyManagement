package com.swj.academymanagement.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.swj.academymanagement.databinding.RecyclerItemStudentManagementMessageBinding
import com.swj.academymanagement.model.StudentManagementMessage

// 선생님 권한 학생 상세 페이지에서 학생에게 보낸 문자 메세지 RecyclerView 어댑터
class StudentManagementMessageAdapter(val context:Context, val messageArr:MutableList<StudentManagementMessage>)
    :Adapter<StudentManagementMessageAdapter.VH>() {

    inner class VH(val binding: RecyclerItemStudentManagementMessageBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemStudentManagementMessageBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = messageArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val message = messageArr[position]
        // 문자 보낸 날짜
        holder.binding.tvDate.text = message.date

        // 문자 메세지 내용
        holder.binding.tvMessage.text = message.message
    }
}