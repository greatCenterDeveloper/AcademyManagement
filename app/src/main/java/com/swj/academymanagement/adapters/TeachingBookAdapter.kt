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

class TeachingBookAdapter(val context: Context, val items:MutableList<ShoppingItem>)
    :Adapter<TeachingBookAdapter.VH>(){
    inner class VH(val binding:RecyclerItemTeachingBookBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemTeachingBookBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item:ShoppingItem = items[position]

        holder.binding.tvTitle.text = item.title
        holder.binding.tvLowPrice.text = "${item.lprice}원"
        holder.binding.tvMall.text = item.mallName
        Glide.with(context).load(item.image).into(holder.binding.iv)

        /*holder.binding.root.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(item.link)
            context.startActivity(intent)
        }*/
    }

}