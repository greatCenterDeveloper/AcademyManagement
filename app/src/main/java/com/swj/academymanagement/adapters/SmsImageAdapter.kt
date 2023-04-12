package com.swj.academymanagement.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.swj.academymanagement.databinding.PagerSmsImageBinding

// 선생님 권한 문자 메세지 전송 시 첨부한 이미지 ViewPager2 어댑터
class SmsImageAdapter(val context: Context, val images:MutableList<Uri>)
    :Adapter<SmsImageAdapter.VH>() {

    inner class VH(val binding:PagerSmsImageBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(PagerSmsImageBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        // 문자 메세지 전송 시 첨부한 이미들
        Glide.with(context).load(images[position]).into(holder.binding.iv)
    }
}