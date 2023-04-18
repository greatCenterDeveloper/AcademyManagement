package com.swj.academymanagement.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
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

        // 학생 이름
        holder.binding.tvName.text = student.name

        // 강좌 코드를 강좌명으로 변경
        val courses = StringBuffer()
        for(course in student.courseArr) {
            when(course) {
                "kor"  -> courses.append("국어, ")
                "eng"  -> courses.append("영어, ")
                "math" -> courses.append("수학")
            }
        }

        // 강좌의 [, ] 삭제하기 위해서...
        var course = courses.toString()
        val last = course.substring(course.length-2, course.length)
        if(last == ", ") course = course.substring(0, course.length-2)


        // 해당 학생이 수강 중인 강좌
        holder.binding.tvCourse.text = course

        // 학생 출결 상태
        var attendanceState = ""

        // 출결 라디오 버튼에 체크를 했다면..
        holder.binding.rgAttendanceState.setOnCheckedChangeListener { radioGroup, i ->
            // 출결 체크한 라디오 버튼 객체 가져오기
            val rb = holder.binding.root.findViewById<AppCompatRadioButton>(radioGroup.checkedRadioButtonId)

            // 출결 체크한 라디오 버튼의 문자열 값 가져오기
            attendanceState = rb.text.toString()
        }


        // 출결 체크가 이미 되어 있다면 DB로부터 읽어와서 체크 처리
        RetrofitHelper.getRetrofitInstance().create(RetrofitCourseScheduleService::class.java)
            .courseScheduleAttendanceRead(
                courseScheduleCode,     // 수업 시간표 코드
                student.id              // 학생 아이디
            ).enqueue(object :Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val attendanceState = response.body()

                    // 출결 체크가 되어 있다면..
                    if(attendanceState != "") {

                        val checkId = when(attendanceState) {
                            // 출석으로 출결 체크가 되어 있는가?
                            holder.binding.rbAttendance.text.toString() -> holder.binding.rbAttendance.id
                            // 결석으로 출결 체크가 되어 있는가?
                            holder.binding.rbAbsence.text.toString() -> holder.binding.rbAbsence.id
                            // 지각으로 출결 체크가 되어 있는가?
                            holder.binding.rbLateness.text.toString() -> holder.binding.rbLateness.id
                            // 혹시나.. 잘못 저장되어 있는가?
                            else -> 0
                        }

                        // 잘못 저장되어 있는 경우가 아니라면 출결 라디오 버튼 체크 하기
                        if(checkId != 0) holder.binding.rgAttendanceState.check(checkId)
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {}
            })


        holder.binding.btnSave.setOnClickListener {
            // 저장 버튼 눌렀을 당시의 시간 가져오기
            val sdf = SimpleDateFormat("HH:mm")
            sdf.timeZone = TimeZone.getTimeZone("Asia/Seoul")

            // 출석, 결석, 지각 체크 시간
            val attendanceTime = sdf.format(Date())

            // 출석, 결석, 지각 처리
            RetrofitHelper.getRetrofitInstance().create(RetrofitCourseScheduleService::class.java)
                .courseScheduleAttendanceCheck(
                    courseScheduleCode,     // 수업 시간표 코드
                    student.id,             // 학생 아이디
                    attendanceState,        // 출결 체크 문자열
                    attendanceTime          // 출결 체크 시간
                ).enqueue(object :Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        val message = response.body()
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(context, "error : ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}