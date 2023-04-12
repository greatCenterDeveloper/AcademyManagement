package com.swj.academymanagement.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.swj.academymanagement.databinding.RecyclerItemAttendanceBinding
import com.swj.academymanagement.model.StudentAttendance

// 학생 권한 등원 / 하원 RecyclerView 어댑터
class AttendanceAdapter(val context: Context, val attendanceArr:MutableList<StudentAttendance>)
    :Adapter<AttendanceAdapter.VH>() {
    inner class VH(val binding:RecyclerItemAttendanceBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemAttendanceBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = attendanceArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val att = attendanceArr[position]

        // 등원 일자
        holder.binding.tvAttendanceDate.text = att.attendanceDate

        // 학생 이름
        holder.binding.tvStudent.text = att.student

        // 등원 시간
        holder.binding.tvAttendanceTime.text = att.attendanceTime

        // 하원 시간
        holder.binding.tvGohomeTime.text = att.gohomeTime
    }


}