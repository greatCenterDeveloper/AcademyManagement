package com.swj.academymanagement.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.google.gson.Gson
import com.swj.academymanagement.adapters.CourseScheduleStudentListAdapter
import com.swj.academymanagement.databinding.ActivityCourseScheduleDetailBinding
import com.swj.academymanagement.model.CourseScheduleTeacher

// 선생님 권한 해당 교시의 강좌에 수강 중인 학생 리스트 조회
class CourseScheduleDetailActivity : AppCompatActivity() {

    val binding:ActivityCourseScheduleDetailBinding by lazy {
        ActivityCourseScheduleDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 화면 전체 다 먹기
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        // 뒤로 가기
        binding.ivBackspace.setOnClickListener { finish() }

        // CourseScheduleAdapter 에서 가져온 강좌 정보와 이 강좌에 수강 중인 학생들 리스트
        val cst:CourseScheduleTeacher = Gson().fromJson(intent.getStringExtra("schedule"), CourseScheduleTeacher::class.java)

        // 강의 날짜 ( ex. 2023-04-11 )
        binding.tvDate.text = cst.date

        // 요일
        binding.tvDay.text = "(${cst.day})"

        // 강좌명
        binding.tvCourse.text = "${cst.course} 강좌"

        // 강의실 명
        binding.tvRoom.text = "(${cst.room})"

        // 몇 교시 수업 인지?
        when(cst.period) {
            "1" -> {    // 1교시일 경우 수업 시간
                binding.tvStartTime.text = "13:00"  // 수업 시작 시간
                binding.tvEndTime.text = "13:50"    // 수업 마지막 시간
            }
            "2" -> {    // 2교시일 경우 수업 시간
                binding.tvStartTime.text = "14:00"  // 수업 시작 시간
                binding.tvEndTime.text = "14:50"    // 수업 마지막 시간
            }
            else -> {   // 3교시일 경우 수업 시간
                binding.tvStartTime.text = "15:00"  // 수업 시작 시간
                binding.tvEndTime.text = "15:50"    // 수업 마지막 시간
            }
        }

        if(cst.studentArr.size > 0) {
            // 내 강좌에 수강 중인 학생 리스트가 있다면 학생 리스트 조회
            binding.recycler.adapter = CourseScheduleStudentListAdapter(this, cst.courseScheduleCode, cst.studentArr)
        } else {
            // 학생 RecyclerView 숨기기
            binding.recycler.visibility = View.GONE

            // 내 강좌에 수강 중인 학생 리스트가 없으니.. 학생이 없다는 문구 출력
            binding.tvNoStudent.visibility = View.VISIBLE
        }
    }

    // 바깥 화면 터치 시 현재 포커스 클리어
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        currentFocus?.clearFocus()
        return super.dispatchTouchEvent(ev)
    }
}