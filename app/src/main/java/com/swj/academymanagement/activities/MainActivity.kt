package com.swj.academymanagement.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.swj.academymanagement.databinding.ActivityMainBinding
import com.swj.academymanagement.model.Member

class MainActivity : AppCompatActivity() {

    val binding:ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

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

        lateinit var teacher:Member

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            teacher = intent.getSerializableExtra("teacher", Member::class.java)!!
        else
            teacher = intent.getSerializableExtra("teacher") as Member

        teacher.courseArr.add(0,"선택안함")
        binding.tvTeacherName.text = "${teacher.name} 선생님 어서오세요."

        // 학생 관리
        binding.btnStudentManagement.setOnClickListener {
            val intent = Intent(this, StudentManagementActivity::class.java)
            intent.putExtra("teacher", teacher)
            startActivity(intent)
        }
        // 수업 목록
        binding.btnClassDayList.setOnClickListener {  }

        // 출결 현황
        binding.btnAttendance.setOnClickListener {  }
        // 문자 보내기
        binding.btnSendMessage.setOnClickListener {  }

        // 일정
        binding.btnSchedule.setOnClickListener {  }
        // 상담 현황
        binding.btnCounsel.setOnClickListener {  }

        // 교재 검색
        binding.btnTeachingBook.setOnClickListener {  }
        // 선생님 노트
        binding.btnTeacherNote.setOnClickListener {  }
    }
}