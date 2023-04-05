package com.swj.academymanagement.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.swj.academymanagement.databinding.RecyclerItemCourseScheduleStudentBinding
import com.swj.academymanagement.model.Member

class CourseScheduleStudentListAdapter(val context: Context, val studentArr:MutableList<Member>)
    : Adapter<CourseScheduleStudentListAdapter.VH>() {
    inner class VH(val binding: RecyclerItemCourseScheduleStudentBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(RecyclerItemCourseScheduleStudentBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = studentArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val member = studentArr[position]
        holder.binding.tvName.text = member.name
        holder.binding.tvCourse.text = member.course
    }

}