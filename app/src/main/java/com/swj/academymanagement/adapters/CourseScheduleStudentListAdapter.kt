package com.swj.academymanagement.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.swj.academymanagement.databinding.RecyclerItemCourseScheduleStudentBinding
import com.swj.academymanagement.model.Member

// 선생님 권한 특정 수업 시간표 클릭 시 해당 강좌에 수강 중인 학생 RecyclerView 어댑터
class CourseScheduleStudentListAdapter(val context: Context, val studentArr:MutableList<Member>)
    : Adapter<CourseScheduleStudentListAdapter.VH>() {
    inner class VH(val binding: RecyclerItemCourseScheduleStudentBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(RecyclerItemCourseScheduleStudentBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = studentArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val member = studentArr[position]

        // 학생 이름
        holder.binding.tvName.text = member.name

        // 해당 학생이 수강 중인 강좌
        holder.binding.tvCourse.text = member.course
    }

}