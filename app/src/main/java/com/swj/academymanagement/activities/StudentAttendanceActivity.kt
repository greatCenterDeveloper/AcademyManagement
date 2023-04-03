package com.swj.academymanagement.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.google.gson.Gson
import com.swj.academymanagement.databinding.ActivityStudentAttendanceBinding
import com.swj.academymanagement.model.Member
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class StudentAttendanceActivity : AppCompatActivity() {

    val binding:ActivityStudentAttendanceBinding by lazy {
        ActivityStudentAttendanceBinding.inflate(layoutInflater)
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

        val student = Gson().fromJson(intent.getStringExtra("student"), Member::class.java)
        val studentId = student.emailId

        var sdf = SimpleDateFormat("yyyy-MM-dd")
        sdf.timeZone = TimeZone.getTimeZone("Asia/Seoul")

        // 현재 날짜 : "yyyy-MM-dd"
        val toDayDate = sdf.format(Date())
        // 등원 시간
        var attendanceTime = ""
        // 하원 시간
        var gohomeTime = ""

        binding.btnAttendance.setOnClickListener {
            sdf = SimpleDateFormat("HH:mm")
            sdf.timeZone = TimeZone.getTimeZone("Asia/Seoul")
            attendanceTime = sdf.format(Date())
            Toast.makeText(this, attendanceTime, Toast.LENGTH_SHORT).show()
        }

        binding.btnGohome.setOnClickListener {
            sdf = SimpleDateFormat("HH:mm")
            sdf.timeZone = TimeZone.getTimeZone("Asia/Seoul")
            gohomeTime = sdf.format(Date())
            Toast.makeText(this, gohomeTime, Toast.LENGTH_SHORT).show()
        }
    }
}