package com.swj.academymanagement.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.gson.Gson
import com.swj.academymanagement.databinding.ActivityCourseScheduleDetailStudentBinding
import com.swj.academymanagement.model.CourseSchedule

// 학생 권한 수업 시간표 상세 화면
class CourseScheduleDetailStudentActivity : AppCompatActivity() {

    val binding:ActivityCourseScheduleDetailStudentBinding by lazy {
        ActivityCourseScheduleDetailStudentBinding.inflate(layoutInflater)
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

        val schedule = Gson().fromJson(intent.getStringExtra("schedule"), CourseSchedule::class.java)

        // 강의 날짜 ( ex. 2023-04-11 )
        binding.tvDate.text = schedule.date

        // 요일
        binding.tvDay.text = "(${schedule.day})"

        // 강좌명 ( 강좌 코드를 강좌명으로 변경 )
        when(schedule.course) {
            "kor"  -> binding.tvCourse.text = "국어 강좌"
            "eng"  -> binding.tvCourse.text = "영어 강좌"
            "math" -> binding.tvCourse.text = "수학 강좌"
        }

        // 강의실 명
        binding.tvRoom.text = "(${schedule.room})"

        // 몇 교시 수업 인지?
        when(schedule.period) {
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

        // 해당 교시의 수업 시간에 작성할 내용 작성 후 저장 버튼
        binding.btnSave.setOnClickListener {
            // 수업 시간에 메모한 내용
            val content = binding.tilContent.editText?.text.toString()
            Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
        }
    }

    // 바깥 화면 터치 시 소프트 키보드 숨기기
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(InputMethodManager::class.java)
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        currentFocus?.clearFocus()
        return super.dispatchTouchEvent(ev)
    }
}