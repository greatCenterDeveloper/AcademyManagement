package com.swj.academymanagement.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.swj.academymanagement.databinding.RecyclerItemStudentManagementDialogAttendanceBinding
import com.swj.academymanagement.model.StudentManagementDialogAttendance

// 선생님 권한 학생 상세 페이지에서 학생의 특정 강좌 출석부 다이얼로그 출석 현황 RecyclerView 어댑터
class StudentManagementDialogAttendanceAdapter(val context:Context, val attendanceArr:MutableList<StudentManagementDialogAttendance>)
    : Adapter<StudentManagementDialogAttendanceAdapter.VH>() {
    inner class VH(val binding:RecyclerItemStudentManagementDialogAttendanceBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemStudentManagementDialogAttendanceBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = attendanceArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val smda = attendanceArr[position]
        // 요일
        holder.binding.tvDay.text = "${smda.day}요일"

        // 교시
        holder.binding.tvPeriod.text = "${smda.period} 교시"

        // 출석 시간 (년-월-일 시:분:초)
        holder.binding.tvAttendanceTime.text = smda.attendanceTime

        // 출석 상태 ( 출석, 결석, 지각 )
        holder.binding.tvAttendanceState.text = smda.attendanceState
    }
}