package com.swj.academymanagement.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.swj.academymanagement.databinding.ActivityAcademyLoginBinding

class AcademyLoginActivity : AppCompatActivity() {

    val binding:ActivityAcademyLoginBinding by lazy { ActivityAcademyLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}