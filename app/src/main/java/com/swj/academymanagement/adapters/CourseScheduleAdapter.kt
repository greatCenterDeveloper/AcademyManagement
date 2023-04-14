package com.swj.academymanagement.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.gson.Gson
import com.swj.academymanagement.activities.CourseScheduleDetailActivity
import com.swj.academymanagement.databinding.RecyclerItemCourseScheduleBinding
import com.swj.academymanagement.model.CourseScheduleTeacher

// 선생님 권한 수업 시간표 RecyclerView 어댑터
class CourseScheduleAdapter(val context: Context, val scheduleArr:MutableList<CourseScheduleTeacher>)
    :Adapter<CourseScheduleAdapter.VH>() {
    inner class VH(val binding:RecyclerItemCourseScheduleBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemCourseScheduleBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = scheduleArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val cs = scheduleArr[position]

        // 강좌 코드를 강좌명으로 변경..
        when(cs.course) {
            "kor"  -> holder.binding.tvCourse.text = "국어 강좌"
            "eng"  -> holder.binding.tvCourse.text = "영어 강좌"
            "math" -> holder.binding.tvCourse.text = "수학 강좌"
        }

        // 강의실 명
        holder.binding.tvRoom.text = "(${cs.room})"

        // 내가 강의하는 강좌가 아니라면 연보라색으로 표시
        if(cs.studentArr.size == 0) {
            holder.binding.tvCourse.setTextColor(
                ContextCompat.getColor(context, com.google.android.material.R.color.m3_ref_palette_dynamic_tertiary70)
            )
            holder.binding.tvRoom.setTextColor(
                ContextCompat.getColor(context, com.google.android.material.R.color.m3_ref_palette_dynamic_tertiary70)
            )
        }

        // 수업 시간표 요소 하나 클릭 ( ex. 1교시 국어 강좌 )
        holder.binding.root.setOnClickListener {
            val intent = Intent(context, CourseScheduleDetailActivity::class.java)

            // 수업 시간표 상세 화면에 가져갈 수업 시간표 정보 및 그 강좌에 수강 중인 학생 리스트
            intent.putExtra("schedule", Gson().toJson(cs))
            context.startActivity(intent)
        }
    }
}