package com.swj.academymanagement.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import com.swj.academymanagement.adapters.AttendanceAdapter
import com.swj.academymanagement.databinding.ActivityAttendanceBinding
import com.swj.academymanagement.model.Member
import com.swj.academymanagement.model.StudentAttendance

class AttendanceActivity : AppCompatActivity() {

    val binding:ActivityAttendanceBinding by lazy { ActivityAttendanceBinding.inflate(layoutInflater) }

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

        val teacher = Gson().fromJson(intent.getStringExtra("teacher"), Member::class.java)

        /*binding.ivHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("teacher", Gson().toJson(teacher))
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }*/

        binding.ivBackspace.setOnClickListener { finish() }

        val saa:MutableList<StudentAttendance> = mutableListOf()
        saa.add(StudentAttendance("국어", "가강사", "2023/02/11", "로빈", "10:30", "15:47"))
        saa.add(StudentAttendance("영어", "나강사", "2023/02/15", "로빈", "10:19", "15:31"))
        saa.add(StudentAttendance("국어", "가강사", "2023/02/19", "홍길동", "12:51", "15:57"))
        saa.add(StudentAttendance("영어", "나강사", "2023/02/21", "홍길동", "12:33", "15:49"))
        saa.add(StudentAttendance("수학", "다강사", "2023/02/25", "홍길동", "13:01", "15:52"))
        saa.add(StudentAttendance("국어", "가강사", "2023/02/26", "해밍턴", "12:48", "15:48"))
        saa.add(StudentAttendance("영어", "나강사", "2023/02/28", "해밍턴", "12:25", "15:51"))
        saa.add(StudentAttendance("수학", "다강사", "2023/03/11", "로빈", "13:11", "15:43"))
        saa.add(StudentAttendance("수학", "다강사", "2023/03/27", "해밍턴", "12:59", "15:57"))

        binding.recycler.adapter = AttendanceAdapter(this, saa)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(InputMethodManager::class.java)
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        currentFocus?.clearFocus()
        return super.dispatchTouchEvent(ev)
    }
}