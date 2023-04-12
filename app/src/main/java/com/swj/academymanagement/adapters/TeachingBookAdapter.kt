package com.swj.academymanagement.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.swj.academymanagement.databinding.RecyclerItemTeachingBookBinding
import com.swj.academymanagement.model.ShoppingItem

// 선생님 / 학생 권한 네이버 오픈 API 도서 RecyclerView 어댑터
class TeachingBookAdapter(val context: Context, val items:MutableList<ShoppingItem>)
    :Adapter<TeachingBookAdapter.VH>(){
    inner class VH(val binding:RecyclerItemTeachingBookBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemTeachingBookBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item:ShoppingItem = items[position]

        // 책 제목
        holder.binding.tvTitle.text = item.title

        // 책 최저가
        holder.binding.tvLowPrice.text = "${item.lprice}원"

        // 책 판매점 명
        holder.binding.tvMall.text = item.mallName

        // 책 이미지
        Glide.with(context).load(item.image).into(holder.binding.iv)

        // 책 쇼핑몰 페이지 이동
        /*holder.binding.root.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(item.link)
            context.startActivity(intent)
        }*/
    }

}