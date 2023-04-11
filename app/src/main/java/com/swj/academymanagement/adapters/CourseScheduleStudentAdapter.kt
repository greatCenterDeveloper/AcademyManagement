package com.swj.academymanagement.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.gson.Gson
import com.swj.academymanagement.activities.CourseScheduleDetailStudentActivity
import com.swj.academymanagement.databinding.RecyclerItemCourseScheduleBinding
import com.swj.academymanagement.model.CourseSchedule

// 학생 권한 수업 시간표 RecyclerView 어댑터
class CourseScheduleStudentAdapter(val context: Context, val scheduleArr:MutableList<CourseSchedule>)
    : Adapter<CourseScheduleStudentAdapter.VH>() {
    inner class VH(val binding:RecyclerItemCourseScheduleBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemCourseScheduleBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = scheduleArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val cs = scheduleArr[position]
        if(cs.date != "") {
            // 강좌 코드를 강좌명으로 변경
            when(cs.course) {
                "kor"  -> holder.binding.tvCourse.text = "국어 강좌"
                "eng"  -> holder.binding.tvCourse.text = "영어 강좌"
                "math" -> holder.binding.tvCourse.text = "수학 강좌"
            }

            // 강의실 명
            holder.binding.tvRoom.text = "(${cs.room})"

            // 수업 시간표 요소 하나 클릭 ( ex. 1교시 국어 강좌 )
            holder.binding.root.setOnClickListener {
                val intent = Intent(context, CourseScheduleDetailStudentActivity::class.java)

                // 수업 시간표 상세 화면에 가져갈 수업 시간표 정보
                intent.putExtra("schedule", Gson().toJson(cs))
                context.startActivity(intent)
            }
        } else {
            holder.binding.tvCourse.text = ""
            holder.binding.tvRoom.text = ""
        }
    }
}