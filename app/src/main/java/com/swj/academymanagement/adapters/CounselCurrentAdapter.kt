package com.swj.academymanagement.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.swj.academymanagement.databinding.RecyclerItemCounselCurrentBinding
import com.swj.academymanagement.model.CounselCurrent

// 선생님 권한 상담 현황 RecyclerView 어댑터
class CounselCurrentAdapter(val context: Context, val counselCurrentArr:MutableList<CounselCurrent>)
    :Adapter<CounselCurrentAdapter.VH>(){
    inner class VH(val binding:RecyclerItemCounselCurrentBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemCounselCurrentBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = counselCurrentArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val cca = counselCurrentArr[position]

        // 상담 선생님 이름
        holder.binding.tvTeacher.text = cca.teacherName

        // 상담 일자
        holder.binding.tvDate.text = cca.date

        // 상담 학생 이름
        holder.binding.tvStudent.text = cca.studentName

        // 상담 내용
        holder.binding.tvCounselContent.text = cca.counselContent
    }
}