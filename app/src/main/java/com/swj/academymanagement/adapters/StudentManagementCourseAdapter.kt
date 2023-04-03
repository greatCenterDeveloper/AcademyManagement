package com.swj.academymanagement.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.swj.academymanagement.databinding.DialogStudentManagementAttendanceBinding
import com.swj.academymanagement.databinding.RecyclerItemStudentManagementCourseBinding
import com.swj.academymanagement.model.StudentManagementCourse
import com.swj.academymanagement.model.StudentManagementDialogAttendance

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

        holder.binding.btnAttendance.setOnClickListener {
            val dialogBinding = DialogStudentManagementAttendanceBinding.inflate(LayoutInflater.from(context))
            val dialog:AlertDialog = AlertDialog.Builder(context)
                                    .setView(dialogBinding.root)
                                    .create()
            //dialog.setContentView(dialogBinding.root)

            dialogBinding.tvCourse.text = smc.course
            dialogBinding.ivClose.setOnClickListener { dialog.dismiss() }

            val attendanceArr:MutableList<StudentManagementDialogAttendance> = mutableListOf()
            attendanceArr.add(StudentManagementDialogAttendance(
                "2023-01-02",
                "11:37", "등원",
                "15:36", "하원"))

            attendanceArr.add(StudentManagementDialogAttendance(
                "2023-01-07",
                "11:17", "등원",
                "15:26", "하원"))

            attendanceArr.add(StudentManagementDialogAttendance(
                "2023-01-08",
                "11:27", "등원",
                "15:16", "하원"))

            dialogBinding.recycler.adapter = StudentManagementDialogAttendanceAdapter(context, attendanceArr)
            dialog.show()
        }
    }
}