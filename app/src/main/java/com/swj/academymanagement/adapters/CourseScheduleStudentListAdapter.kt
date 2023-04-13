package com.swj.academymanagement.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.swj.academymanagement.activities.CourseScheduleDetailActivity
import com.swj.academymanagement.databinding.RecyclerItemCourseScheduleStudentBinding
import com.swj.academymanagement.model.Member
import com.swj.academymanagement.network.RetrofitCourseScheduleService
import com.swj.academymanagement.network.RetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

// 선생님 권한 특정 수업 시간표 클릭 시 해당 강좌에 수강 중인 학생 RecyclerView 어댑터
class CourseScheduleStudentListAdapter(val context: Context, val courseScheduleCode:String, val studentArr:MutableList<Member>)
    : Adapter<CourseScheduleStudentListAdapter.VH>() {
    inner class VH(val binding: RecyclerItemCourseScheduleStudentBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(RecyclerItemCourseScheduleStudentBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = studentArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val student = studentArr[position]

        val csda:CourseScheduleDetailActivity = context as CourseScheduleDetailActivity

        // 학생 이름
        holder.binding.tvName.text = student.name

        // 해당 학생이 수강 중인 강좌
        holder.binding.tvCourse.text = student.course

        // 학생 출결 체크
        csda.binding.btnSave.setOnClickListener {
            // 출결 체크한 라디오 버튼
            val checkAttendance =
                holder.binding.root.findViewById<AppCompatRadioButton>(holder.binding.rgAttendanceState.checkedRadioButtonId)

            // 출결 체크한 라디오 버튼의 문자열
            val checkAttendanceName = checkAttendance.text.toString()

            // 출결 체크 Retrofit
            RetrofitHelper.getRetrofitInstance().create(RetrofitCourseScheduleService::class.java)
                .courseScheduleAttendanceCheck(
                    courseScheduleCode,     // 수업 시간표 코드 (FK)
                    student.id              // 학생 아이디 -> 학생 아이디로 수강 신청 테이블에서 수강 신청 코드를 얻어오기 위해..
                ).enqueue(object :Callback<Unit> {
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {}
                    override fun onFailure(call: Call<Unit>, t: Throwable) {}
                })

            // 현재 날짜 가져오기
            var sdf = SimpleDateFormat("yyyy-MM-dd")
            sdf.timeZone = TimeZone.getTimeZone("Asia/Seoul")

            // 현재 날짜 : "yyyy-MM-dd"
            val toDayDate = sdf.format(Date())

        }
    }

}