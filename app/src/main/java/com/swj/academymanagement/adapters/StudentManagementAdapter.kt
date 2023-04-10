package com.swj.academymanagement.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.swj.academymanagement.activities.StudentDetailActivity
import com.swj.academymanagement.databinding.RecyclerItemStudentManagementBinding
import com.swj.academymanagement.model.Member

class StudentManagementAdapter(val context:Context, val studentArr:List<Member>, val teacherId:String)
    :Adapter<StudentManagementAdapter.VH>() {
    inner class VH(val binding: RecyclerItemStudentManagementBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemStudentManagementBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = studentArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val student:Member = studentArr[position]

        if(!student.profile.equals(""))
            Glide.with(context).load(student.profile).into(holder.binding.civProfile)

        holder.binding.tvName.text = student.name

        val courses = StringBuffer()
        for(course in student.courseArr) {
            if(course == "국어") courses.append("국어, ")
            if(course == "영어") courses.append("영어, ")
            if(course == "수학") courses.append("수학")
        }

        var courseTemp:String = courses.toString()

        lateinit var course:String
        if(courseTemp.contains("국어")) course = "국어, "
        if(courseTemp.contains("영어")) course += "영어, "
        if(courseTemp.contains("수학")) course += "수학"

        val last = course.substring(course.length-2, course.length)
        if(last == ", ") course = course.substring(0, course.length-2)

        holder.binding.tvCourse.text = course
        holder.binding.tvCall.text = student.call_number
        student.course = course

        holder.binding.btnPhoneCall.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_DIAL
            intent.data = Uri.parse("tel:${student.call_number}")
            context.startActivity(intent)
        }

        holder.binding.root.setOnClickListener {
            val intent:Intent = Intent(context, StudentDetailActivity::class.java)
            intent.putExtra("student", Gson().toJson(student))
            //intent.putExtra("teacherId", teacherId)
            context.startActivity(intent)
        }
    }
}