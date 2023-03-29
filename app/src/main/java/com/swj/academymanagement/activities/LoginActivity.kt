package com.swj.academymanagement.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.swj.academymanagement.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    val binding:ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 학원 아이디로 로그인 버튼
        binding.btnAcademyAccount.setOnClickListener {
            startActivity(Intent(this, AcademyLoginActivity::class.java))
        }

        // 네이버 아이디로 로그인 버튼
        binding.btnNaverAccount.setOnClickListener {}

        // 회원 가입 버튼
        binding.btnSignUp.setOnClickListener {
            binding.layoutLogin.visibility = View.GONE
            binding.layoutSignup.visibility = View.VISIBLE
        }

        // 학원 아이디로 가입하기 버튼
        binding.btnAcademyAccountSignup.setOnClickListener {
            startActivity(Intent(this, AcademySignupActivity::class.java))
        }

        // 네이버 아이디로 가입하기 버튼
        binding.btnNaverAccountSignup.setOnClickListener {}
    }
}