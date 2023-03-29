package com.swj.academymanagement.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.swj.academymanagement.R
import com.swj.academymanagement.databinding.ActivityAcademyLoginBinding
import com.swj.academymanagement.model.Member

class AcademyLoginActivity : AppCompatActivity() {

    val binding:ActivityAcademyLoginBinding by lazy { ActivityAcademyLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationIcon(R.drawable.ic_action_backspace)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, AcademySignupActivity::class.java))
            finish()
        }

        binding.btnLogin.setOnClickListener {
            val emailId:String = binding.tilEmailId.editText?.text.toString()
            val password:String = binding.tilPassword.editText?.text.toString()

            var authority = "선생님"
            var profile = ""
            var tempEmailId = "aaa"
            var tempPassword = "aaa"
            var name = "sam"
            val courseArr:MutableList<String> = mutableListOf()
            courseArr.add("중등 국어")
            courseArr.add("중등 수학")
            courseArr.add("중등 영어")
            var call = "010-1234-5678"

            // 선생님 로그인
            if(emailId.equals(tempEmailId) && password.equals(tempPassword)) {
                val teacherMember = Member(authority, profile, emailId, password, name, courseArr, call)
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("teacher", teacherMember)
                startActivity(intent)
                finish()
            }

            authority = "학생"
            profile = ""
            tempEmailId = "sss"
            tempPassword = "sss"
            name = "robin"
            call = "010-1111-2222"

            // 학생 로그인
            if(emailId.equals(tempEmailId) && password.equals(tempPassword)) {
                val studentMember = Member(authority, profile, emailId, password, name, courseArr, call)
                val intent = Intent(this, StudentActivity::class.java)
                intent.putExtra("student", studentMember);
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}