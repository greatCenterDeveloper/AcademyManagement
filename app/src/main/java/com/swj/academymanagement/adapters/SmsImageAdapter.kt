package com.swj.academymanagement.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.swj.academymanagement.databinding.PagerSmsImageBinding

class SmsImageAdapter(val context: Context, val images:MutableList<Uri>)
    :Adapter<SmsImageAdapter.VH>() {

    inner class VH(val binding:PagerSmsImageBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(PagerSmsImageBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        Glide.with(context).load(images[position]).into(holder.binding.iv)
    }
}