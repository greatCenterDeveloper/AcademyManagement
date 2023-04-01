package com.swj.academymanagement.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.swj.academymanagement.R
import com.swj.academymanagement.adapters.AttendanceAdapter
import com.swj.academymanagement.databinding.ActivityAttendanceBinding
import com.swj.academymanagement.model.StudentAttendance

class AttendanceActivity : AppCompatActivity() {

    val binding:ActivityAttendanceBinding by lazy { ActivityAttendanceBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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
}