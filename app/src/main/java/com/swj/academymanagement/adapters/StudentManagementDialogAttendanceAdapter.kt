package com.swj.academymanagement.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.swj.academymanagement.databinding.RecyclerItemStudentManagementDialogAttendanceBinding
import com.swj.academymanagement.model.StudentManagementDialogAttendance

class StudentManagementDialogAttendanceAdapter(val context:Context, val attendanceArr:MutableList<StudentManagementDialogAttendance>)
    : Adapter<StudentManagementDialogAttendanceAdapter.VH>() {
    inner class VH(val binding:RecyclerItemStudentManagementDialogAttendanceBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemStudentManagementDialogAttendanceBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = attendanceArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val smda = attendanceArr[position]
        holder.binding.tvDate.text = smda.date
        holder.binding.tvAttendanceTime.text = smda.attendanceTime
        holder.binding.tvAttendanceState.text = smda.attendanceState
        holder.binding.tvGohomeTime.text = smda.gohomeTime
        holder.binding.tvGohomeState.text = smda.gohomeState
    }
}