package com.swj.academymanagement.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import com.swj.academymanagement.R
import com.swj.academymanagement.databinding.ActivityMainBinding
import com.swj.academymanagement.databinding.DialogMyInfoUpdateBinding
import com.swj.academymanagement.databinding.DialogPasswordUpdateBinding
import com.swj.academymanagement.model.Member

class MainActivity : AppCompatActivity() {

    val binding:ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var drawerToggle:ActionBarDrawerToggle

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
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false
        )
        drawerToggle = ActionBarDrawerToggle(this,
            binding.drawerLayout, binding.toolbar, R.string.open, R.string.close)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        drawerToggle.syncState()
        binding.drawerLayout.addDrawerListener(drawerToggle)

        binding.nav.setNavigationItemSelectedListener {
            if(it.itemId == R.id.menu_my_info_update) {
                val dialogBinding = DialogMyInfoUpdateBinding.inflate(layoutInflater)
                val dialog: AlertDialog = AlertDialog.Builder(this)
                    .setView(dialogBinding.root)
                    .create()
                dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }
                dialog.show()

                dialogBinding.btnUpdate.setOnClickListener {
                    val name = dialogBinding.tilName.editText?.text.toString()
                    val call1 = dialogBinding.tilCall1.editText?.text.toString()
                    val call2 = dialogBinding.tilCall2.editText?.text.toString()
                    val call3 = dialogBinding.tilCall3.editText?.text.toString()
                    val call = "${call1}-${call2}-${call3}"
                    val prevPassword = dialogBinding.tilPrevPassword.editText?.text.toString()
                    val password = dialogBinding.tilPassword.editText?.text.toString()
                    val passwordCheck = dialogBinding.tilPasswordCheck.editText?.text.toString()

                    if(password == passwordCheck) {
                        Toast.makeText(this, "수정 되었습니다.", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                }
            } else if(it.itemId == R.id.menu_password_update) {
                val dialogBinding = DialogPasswordUpdateBinding.inflate(layoutInflater)
                val dialog: AlertDialog = AlertDialog.Builder(this)
                    .setView(dialogBinding.root)
                    .create()
                dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }
                dialog.show()

                dialogBinding.btnUpdate.setOnClickListener {
                    val prevPassword = dialogBinding.tilPrevPassword.editText?.text.toString()
                    val password = dialogBinding.tilPassword.editText?.text.toString()
                    val passwordCheck = dialogBinding.tilPasswordCheck.editText?.text.toString()

                    if(password == passwordCheck) {
                        Toast.makeText(this, "수정 되었습니다.", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                }
            }

            binding.drawerLayout.closeDrawer(binding.nav)
            false
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
        binding.btnClassDayList.setOnClickListener {
            val intent = Intent(this, CourseScheduleActivity::class.java)
            startActivity(intent)
        }

        // 출결 현황
        binding.btnAttendance.setOnClickListener {
            val intent = Intent(this, AttendanceActivity::class.java)
            startActivity(intent)
        }
        // 문자 보내기
        binding.btnSendMessage.setOnClickListener {
            val intent = Intent(this, SmsSendActivity::class.java)
            startActivity(intent)
        }

        // 상담 현황
        binding.btnCounsel.setOnClickListener {
            val intent = Intent(this, CounselActivity::class.java)
            startActivity(intent)
        }
        // 교재 검색
        binding.btnTeachingBook.setOnClickListener {
            val intent = Intent(this, TeachingBookActivity::class.java)
            startActivity(intent)
        }

        // 선생님 노트
        binding.btnTeacherNote.setOnClickListener {
            val intent = Intent(this, TeacherNoteActivity::class.java)
            startActivity(intent)
        }
    }
}