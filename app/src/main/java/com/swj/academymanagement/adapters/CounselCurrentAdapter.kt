package com.swj.academymanagement.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.swj.academymanagement.databinding.RecyclerItemCounselCurrentBinding
import com.swj.academymanagement.model.CounselCurrent

class CounselCurrentAdapter(val context: Context, val counselCurrentArr:MutableList<CounselCurrent>)
    :Adapter<CounselCurrentAdapter.VH>(){
    inner class VH(val binding:RecyclerItemCounselCurrentBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemCounselCurrentBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = counselCurrentArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val cca = counselCurrentArr[position]
        holder.binding.tvTeacher.text = cca.teacher
        holder.binding.tvDate.text = cca.date
        holder.binding.tvStudent.text = cca.student
        holder.binding.tvCounselContent.text = cca.counselContent
    }
}