package com.swj.academymanagement.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.google.gson.Gson
import com.swj.academymanagement.R
import com.swj.academymanagement.databinding.ActivityCourseScheduleDetailStudentBinding
import com.swj.academymanagement.model.CourseSchedule

class CourseScheduleDetailStudentActivity : AppCompatActivity() {

    val binding:ActivityCourseScheduleDetailStudentBinding by lazy {
        ActivityCourseScheduleDetailStudentBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        binding.ivBackspace.setOnClickListener { finish() }

        val schedule = Gson().fromJson(intent.getStringExtra("schedule"), CourseSchedule::class.java)

        binding.tvDate.text = schedule.date
        binding.tvDay.text = "(${schedule.day})"
        binding.tvCourse.text = "${schedule.course} 강좌"
        binding.tvRoom.text = "(${schedule.room})"

        when(schedule.period) {
            "1" -> {
                binding.tvStartTime.text = "13:00"
                binding.tvEndTime.text = "14:00"
            }
            "2" -> {
                binding.tvStartTime.text = "14:00"
                binding.tvEndTime.text = "15:00"
            }
            else -> {
                binding.tvStartTime.text = "15:00"
                binding.tvEndTime.text = "16:00"
            }
        }

        binding.btnSave.setOnClickListener {
            val content = binding.tilContent.editText?.text.toString()
            Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
        }
    }
}