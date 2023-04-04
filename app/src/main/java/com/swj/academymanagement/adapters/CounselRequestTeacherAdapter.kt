package com.swj.academymanagement.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.gson.Gson
import com.swj.academymanagement.activities.CounselActivity
import com.swj.academymanagement.activities.CounselDetailActivity
import com.swj.academymanagement.databinding.RecyclerItemCounselRequestTeacherBinding
import com.swj.academymanagement.model.CounselRequestTeacher

class CounselRequestTeacherAdapter(val context: Context, val counselRequestTeacherArr:MutableList<CounselRequestTeacher>)
    :Adapter<CounselRequestTeacherAdapter.VH>(){
    inner class VH(val binding:RecyclerItemCounselRequestTeacherBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemCounselRequestTeacherBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = counselRequestTeacherArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val crt = counselRequestTeacherArr[position]
        holder.binding.tvName.text = crt.name
        holder.binding.tvDate.text = crt.date
        holder.binding.tvCounselDate.text = crt.counselDate
        holder.binding.tvCounselStartTime.text = crt.counselStartTime
        holder.binding.tvCounselEndTime.text = crt.counselEndTime
        holder.binding.tvCounselContent.text = crt.counselContent

        holder.binding.btnCounsel.setOnClickListener {
            val intent = Intent(context, CounselDetailActivity::class.java)
            intent.putExtra("counselRequest", Gson().toJson(crt))

            val options: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context as CounselActivity, Pair(holder.binding.tvCounselContent, "counselDetailTeacher")
                )
            context.startActivity(intent, options.toBundle())
        }
    }
}