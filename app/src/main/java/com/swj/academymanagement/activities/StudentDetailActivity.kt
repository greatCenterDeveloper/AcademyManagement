package com.swj.academymanagement.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.swj.academymanagement.adapters.StudentManagementCounselAdapter
import com.swj.academymanagement.adapters.StudentManagementCourseAdapter
import com.swj.academymanagement.adapters.StudentManagementMessageAdapter
import com.swj.academymanagement.databinding.ActivityStudentDetailBinding
import com.swj.academymanagement.model.Member
import com.swj.academymanagement.model.StudentManagementCounsel
import com.swj.academymanagement.model.StudentManagementCourse
import com.swj.academymanagement.model.StudentManagementMessage

class StudentDetailActivity : AppCompatActivity() {

    val binding:ActivityStudentDetailBinding by lazy { ActivityStudentDetailBinding.inflate(layoutInflater) }

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


        lateinit var student:Member

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            student = intent.getSerializableExtra("student", Member::class.java)!!
        else
            student = intent.getSerializableExtra("student") as Member

        if(!student.profile.equals(""))
            Glide.with(this).load(student.profile).into(binding.ivProfile)

        binding.tvName.text = student.name
        binding.tvCourse.text = student.course
        binding.tvCall.text = student.call

        val requestCourseArr:MutableList<StudentManagementCourse> = mutableListOf()
        for(course in student.courseArr) {
            var i = 1
            requestCourseArr.add(StudentManagementCourse(course, "", "강사${i}", "10", "0", student.emailId))
        }

        binding.recyclerCourse.adapter = StudentManagementCourseAdapter(this, requestCourseArr)

        val counselArr:MutableList<StudentManagementCounsel> = mutableListOf()
        counselArr.add(StudentManagementCounsel("2023-01-02", student.name, "상담 상담 상담 상담"))
        counselArr.add(StudentManagementCounsel("2023-01-10", student.name, "상담을 시작합니다. 상담을 완료하였습니다."))
        counselArr.add(StudentManagementCounsel("2023-02-02", student.name, "상담을 시작할까요? 상담을 종료합니다."))
        counselArr.add(StudentManagementCounsel("2023-02-12", student.name, "훌륭한 성적을 못내서 상담하였습니다."))
        counselArr.add(StudentManagementCounsel("2023-03-22", student.name, "길동군 더 어려운 시험 없냐고 상담하였습니다."))

        binding.recyclerCounsel.adapter = StudentManagementCounselAdapter(this, counselArr)

        val messageArr:MutableList<StudentManagementMessage> = mutableListOf()
        messageArr.add(StudentManagementMessage("2023-01-19", "어디니?"))
        messageArr.add(StudentManagementMessage("2023-01-21", "출발했니?"))
        messageArr.add(StudentManagementMessage("2023-02-08", "왜 안오니?"))
        messageArr.add(StudentManagementMessage("2023-02-19", "상담 좀 하자."))
        messageArr.add(StudentManagementMessage("2023-03-29", "오늘 수업 때 문제집 꼭 가져와요."))

        binding.recyclerMessage.adapter = StudentManagementMessageAdapter(this, messageArr)
    }
}