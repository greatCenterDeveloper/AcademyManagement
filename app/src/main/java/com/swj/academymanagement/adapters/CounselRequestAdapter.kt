package com.swj.academymanagement.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.swj.academymanagement.databinding.RecyclerItemCounselRequestBinding
import com.swj.academymanagement.model.CounselRequest

class CounselRequestAdapter(val context: Context, val counselRequestArr:MutableList<CounselRequest>)
    :Adapter<CounselRequestAdapter.VH>() {
    inner class VH(val binding:RecyclerItemCounselRequestBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemCounselRequestBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = counselRequestArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val counselRequest = counselRequestArr[position]
        holder.binding.tvCounselDate.text = counselRequest.date
        holder.binding.tvCounselStartTime.text = counselRequest.startTime
        holder.binding.tvCounselEndTime.text = counselRequest.endTime
        holder.binding.tvCounselContent.text = counselRequest.content
    }

}