package com.swj.academymanagement.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.google.gson.Gson
import com.swj.academymanagement.adapters.CourseScheduleStudentListAdapter
import com.swj.academymanagement.databinding.ActivityCourseScheduleDetailBinding
import com.swj.academymanagement.model.CourseScheduleTeacher

class CourseScheduleDetailActivity : AppCompatActivity() {

    val binding:ActivityCourseScheduleDetailBinding by lazy {
        ActivityCourseScheduleDetailBinding.inflate(layoutInflater)
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

        val cst:CourseScheduleTeacher = Gson().fromJson(intent.getStringExtra("schedule"), CourseScheduleTeacher::class.java)

        binding.tvDate.text = cst.date
        binding.tvDay.text = "(${cst.day})"
        binding.tvCourse.text = "${cst.course} 강좌"
        binding.tvRoom.text = "(${cst.room})"

        when(cst.period) {
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

        if(cst.students.size > 0)
            binding.recycler.adapter = CourseScheduleStudentListAdapter(this, cst.students)
        else {
            binding.recycler.visibility = View.GONE
            binding.btnSave.visibility = View.GONE
            binding.tvNoStudent.visibility = View.VISIBLE
        }

    }
}