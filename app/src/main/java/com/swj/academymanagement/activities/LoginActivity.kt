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

        binding.btnAcademyAccount.setOnClickListener {
            startActivity(Intent(this, AcademyLoginActivity::class.java))
            finish()
        }

        binding.btnNaverAccount.setOnClickListener {}

        binding.btnSignUp.setOnClickListener {
            binding.layoutLogin.visibility = View.GONE
            binding.layoutSignup.visibility = View.VISIBLE
        }

    }
}