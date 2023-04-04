package com.swj.academymanagement.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.swj.academymanagement.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    val binding:ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
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

        // 로그인 버튼
        binding.btnLogin.setOnClickListener {
            binding.layoutLogin.visibility = View.VISIBLE
            binding.layoutSignup.visibility = View.GONE
        }

        // 학원 아이디로 가입하기 버튼
        binding.btnAcademyAccountSignup.setOnClickListener {
            startActivity(Intent(this, AcademySignupActivity::class.java))
        }

        // 네이버 아이디로 가입하기 버튼
        binding.btnNaverAccountSignup.setOnClickListener {}
    }
}