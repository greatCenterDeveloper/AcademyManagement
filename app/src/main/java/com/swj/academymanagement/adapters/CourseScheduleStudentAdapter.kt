package com.swj.academymanagement.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.gson.Gson
import com.swj.academymanagement.activities.CourseScheduleDetailStudentActivity
import com.swj.academymanagement.databinding.RecyclerItemCourseScheduleBinding
import com.swj.academymanagement.model.CourseSchedule

class CourseScheduleStudentAdapter(val context: Context, val scheduleArr:MutableList<CourseSchedule>)
    : Adapter<CourseScheduleStudentAdapter.VH>() {
    inner class VH(val binding:RecyclerItemCourseScheduleBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemCourseScheduleBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = scheduleArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val cs = scheduleArr[position]
        if(cs.date != "") {
            holder.binding.tvCourse.text = "${cs.course} 강좌"
            holder.binding.tvRoom.text = "(${cs.room})"
            holder.binding.root.setOnClickListener {
                val intent = Intent(context, CourseScheduleDetailStudentActivity::class.java)
                intent.putExtra("schedule", Gson().toJson(cs))
                context.startActivity(intent)
            }
        } else {
            holder.binding.tvCourse.text = ""
            holder.binding.tvRoom.text = ""
        }
    }
}