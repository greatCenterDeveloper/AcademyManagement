package com.swj.academymanagement.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.gson.Gson
import com.swj.academymanagement.activities.CourseScheduleDetailActivity
import com.swj.academymanagement.databinding.RecyclerItemCourseScheduleBinding
import com.swj.academymanagement.model.CourseScheduleTeacher

class CourseScheduleAdapter(val context: Context, val scheduleArr:MutableList<CourseScheduleTeacher>)
    :Adapter<CourseScheduleAdapter.VH>() {
    inner class VH(val binding:RecyclerItemCourseScheduleBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemCourseScheduleBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = scheduleArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val cs = scheduleArr[position]

        when(cs.course) {
            "kor"  -> holder.binding.tvCourse.text = "국어 강좌"
            "eng"  -> holder.binding.tvCourse.text = "영어 강좌"
            "math" -> holder.binding.tvCourse.text = "수학 강좌"
        }
        holder.binding.tvRoom.text = "(${cs.room})"

        holder.binding.root.setOnClickListener {
            val intent = Intent(context, CourseScheduleDetailActivity::class.java)
            intent.putExtra("schedule", Gson().toJson(cs))
            context.startActivity(intent)
        }
    }
}