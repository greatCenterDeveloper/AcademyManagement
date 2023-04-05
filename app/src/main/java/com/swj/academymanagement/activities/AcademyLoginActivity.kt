package com.swj.academymanagement.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import com.swj.academymanagement.R
import com.swj.academymanagement.databinding.ActivityAcademyLoginBinding
import com.swj.academymanagement.databinding.DialogFindIdBinding
import com.swj.academymanagement.databinding.DialogFindPasswordBinding
import com.swj.academymanagement.model.Member

class AcademyLoginActivity : AppCompatActivity() {

    val binding:ActivityAcademyLoginBinding by lazy { ActivityAcademyLoginBinding.inflate(layoutInflater) }

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

        binding.toolbar.setNavigationIcon(R.drawable.ic_action_backspace)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, AcademySignupActivity::class.java))
            finish()
        }

        binding.tilEmailId.editText?.setOnClickListener { binding.tilEmailId.editText?.selectAll() }
        binding.tilPassword.editText?.setOnClickListener { binding.tilPassword.editText?.selectAll() }

        // 아이디 찾기
        binding.btnFindId.setOnClickListener {
            val dialogBinding = DialogFindIdBinding.inflate(layoutInflater)
            val dialog: AlertDialog = AlertDialog.Builder(this)
                .setView(dialogBinding.root)
                .create()
            dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }
            dialogBinding.ivClose.setOnClickListener { dialog.dismiss() }
            dialog.show()
            dialogBinding.btnFind.setOnClickListener {
                val call:String = dialogBinding.tilInputCall.editText?.text.toString()
                Toast.makeText(this, "휴대폰 번호 : ${call}", Toast.LENGTH_SHORT).show()
            }
        }

        // 비밀번호 찾기
        binding.btnFindPassword.setOnClickListener {
            val dialogBinding = DialogFindPasswordBinding.inflate(layoutInflater)
            val dialog: AlertDialog = AlertDialog.Builder(this)
                .setView(dialogBinding.root)
                .create()
            dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }
            dialogBinding.ivClose.setOnClickListener { dialog.dismiss() }
            dialog.show()
            dialogBinding.btnFind.setOnClickListener {
                val id:String = dialogBinding.tilInputId.editText?.text.toString()
                val call:String = dialogBinding.tilInputCall.editText?.text.toString()
                Toast.makeText(this, "아이디 : ${id}\n휴대폰 번호 : ${call}", Toast.LENGTH_SHORT).show()
            }
        }

        // 로그인
        binding.btnLogin.setOnClickListener {
            val emailId:String = binding.tilEmailId.editText?.text.toString()
            val password:String = binding.tilPassword.editText?.text.toString()

            var authority = "선생님"
            var profile = ""
            var tempEmailId = "aaa"
            var tempPassword = "aaa"
            var name = "sam"
            val courseArr:MutableList<String> = mutableListOf()
            courseArr.add("국어")
            courseArr.add("수학")
            courseArr.add("영어")
            var call = "010-1234-5678"

            // 선생님 로그인
            if(emailId.equals(tempEmailId) && password.equals(tempPassword)) {
                val teacherMember = Member(authority, profile, emailId, password, name, courseArr, call)
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("teacher", Gson().toJson(teacherMember))
                startActivity(intent)
                finish()
            } else { // 로그인 실패
                binding.tilEmailId.editText?.requestFocus()
                binding.tilEmailId.editText?.selectAll()
            }

            authority = "학생"
            profile = ""
            tempEmailId = "sss"
            tempPassword = "sss"
            name = "robin"
            call = "010-1111-2222"
            courseArr.clear()
            courseArr.add("국어")
            courseArr.add("영어")

            // 학생 로그인
            if(emailId.equals(tempEmailId) && password.equals(tempPassword)) {
                val studentMember = Member(authority, profile, emailId, password, name, courseArr, call)
                val intent = Intent(this, StudentActivity::class.java)
                intent.putExtra("student", Gson().toJson(studentMember))
                startActivity(intent)
                finish()
            } else { // 로그인 실패
                binding.tilEmailId.editText?.requestFocus()
                binding.tilEmailId.editText?.selectAll()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if(binding.tilEmailId.editText?.text.toString() != "" && binding.tilPassword.editText?.text.toString() != "") {
            val imm:InputMethodManager = getSystemService(InputMethodManager::class.java)
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            currentFocus?.clearFocus()
        }
        return super.dispatchTouchEvent(ev)
    }
}