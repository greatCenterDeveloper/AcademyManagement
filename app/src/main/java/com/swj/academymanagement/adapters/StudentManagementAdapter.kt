package com.swj.academymanagement.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.swj.academymanagement.databinding.RecyclerItemStudentManagementBinding
import com.swj.academymanagement.model.Member

class StudentManagementAdapter(val context:Context, val studentArr:MutableList<Member>) :Adapter<StudentManagementAdapter.VH>() {
    inner class VH(val binding: RecyclerItemStudentManagementBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemStudentManagementBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = studentArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val student:Member = studentArr[position]

        if(!student.profile.equals(""))
            Glide.with(context).load(student.profile).into(holder.binding.civProfile)

        holder.binding.tvName.text = student.name

        val courses:StringBuffer = StringBuffer()
        for(course in student.courseArr) {
            if(course.contains("국어")) courses.append("국어, ")
            if(course.contains("수학")) courses.append("수학, ")
            if(course.contains("영어")) courses.append("영어")
        }

        var course:String = courses.toString()
        val last = course.substring(course.length-2, course.length)
        if(last.equals(", ")) course = course.substring(0, course.length-1)

        holder.binding.tvCourse.text = course
        holder.binding.tvCall.text = student.call

        holder.binding.root.setOnClickListener {

        }
    }


}