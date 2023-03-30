package com.swj.academymanagement.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.swj.academymanagement.databinding.RecyclerItemStudentManagementCourseBinding
import com.swj.academymanagement.model.StudentManagementCourse

class StudentManagementCourseAdapter(val context:Context, val courseArr:MutableList<StudentManagementCourse>)
    : Adapter<StudentManagementCourseAdapter.VH>() {

    inner class VH(val binding: RecyclerItemStudentManagementCourseBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemStudentManagementCourseBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = courseArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val smc = courseArr[position]
        holder.binding.tvCourse.text = smc.course

        if(!smc.profile.equals(""))
            Glide.with(context).load(smc.profile).into(holder.binding.civProfile)

        holder.binding.tvName.text = "${smc.teacher} 선생님"
        holder.binding.tvAttendance.text = smc.attendance
        holder.binding.tvAbsence.text = smc.absence
    }
}