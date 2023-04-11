package com.swj.academymanagement.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.swj.academymanagement.databinding.RecyclerItemStudentManagementCounselBinding
import com.swj.academymanagement.model.StudentManagementCounsel

// 선생님 권한 학생 상세 화면에서 학생과 상담한 상담 현황 RecyclerView 어댑터
class StudentManagementCounselAdapter(val context:Context, val counselArr:MutableList<StudentManagementCounsel>)
    : Adapter<StudentManagementCounselAdapter.VH>() {

    inner class VH(val binding:RecyclerItemStudentManagementCounselBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemStudentManagementCounselBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = counselArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val counsel = counselArr[position]

        // 상담 일자
        holder.binding.tvDate.text = counsel.date

        // 학생 이름
        holder.binding.tvName.text = counsel.name

        // 상담 내용
        holder.binding.tvCounselCentent.text = counsel.content
    }
}