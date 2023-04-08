package com.swj.academymanagement.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.swj.academymanagement.R
import com.swj.academymanagement.databinding.ActivityStudentBinding
import com.swj.academymanagement.databinding.DialogMyInfoUpdateBinding
import com.swj.academymanagement.databinding.DialogPasswordUpdateBinding
import com.swj.academymanagement.model.Member
import de.hdodenhof.circleimageview.CircleImageView

class StudentActivity : AppCompatActivity() {

    val binding:ActivityStudentBinding by lazy { ActivityStudentBinding.inflate(layoutInflater) }
    lateinit var drawerToggle:ActionBarDrawerToggle
    var profile:String = ""
    lateinit var civ:CircleImageView

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
        supportActionBar?.setDisplayShowTitleEnabled(false)

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
                dialogBinding.ivClose.setOnClickListener { dialog.dismiss() }
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
                dialogBinding.ivClose.setOnClickListener { dialog.dismiss() }
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

        val headerView = binding.nav.getHeaderView(0)
        civ = headerView.findViewById(R.id.civ_profile)
        civ.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*")
            imagePickResultLauncher.launch(intent)
        }

        val student = Gson().fromJson(intent.getStringExtra("student"), Member::class.java)
        student.courseArr.add(0, "선택안함")
        binding.tvStudentName.text = "${student.name} 학생 오늘도 열심히 공부!"

        // 출결 인증
        binding.btnAttendanceConfirm.setOnClickListener {
            val intent = Intent(this, StudentAttendanceActivity::class.java)
            intent.putExtra("student", Gson().toJson(student))
            startActivity(intent)
        }

        // 수업 목록
        binding.btnClassDayList.setOnClickListener {
            val intent = Intent(this, CourseScheduleActivity::class.java)
            // 권한이 선생이면 모든 강좌 시간표 보기
            // 권한이 학생이면 수강 중인 강좌의 시간표 보기
            // 권한(선생, 학생)을 보고 권한에 맞는 수업 목록 상세 화면 이동
            intent.putExtra("student", Gson().toJson(student))
            startActivity(intent)
        }

        // 상담 신청
        binding.btnCounselRequest.setOnClickListener {
            val intent = Intent(this, CounselRequestActivity::class.java)
            intent.putExtra("student", Gson().toJson(student))
            startActivity(intent)
        }

        // 수업 노트
        binding.btnClassNote.setOnClickListener {
            val intent = Intent(this, ClassNoteActivity::class.java)
            intent.putExtra("student", Gson().toJson(student))
            startActivity(intent)
        }

        // 교재 검색
        binding.btnTeachingBook.setOnClickListener {
            val intent = Intent(this, TeachingBookActivity::class.java)
            intent.putExtra("student", Gson().toJson(student))
            startActivity(intent)
        }
    }

    private val imagePickResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                if(it.resultCode != RESULT_CANCELED) {
                    val intent:Intent = it.data!!
                    profile = intent.data.toString()

                    // 디비 프로필 사진 경로 변경..

                    Glide.with(this).load(intent.data).into(civ)
                }
            }
        )

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_logout) {
            AlertDialog.Builder(this)
                .setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                })
                .setNegativeButton("NO", null)
                .show()
        }
        return super.onOptionsItemSelected(item)
    }
}