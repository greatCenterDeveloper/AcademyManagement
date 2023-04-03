package com.swj.academymanagement.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.swj.academymanagement.databinding.ActivityCounselDetailBinding

class CounselDetailActivity : AppCompatActivity() {

    val binding:ActivityCounselDetailBinding by lazy { ActivityCounselDetailBinding.inflate(layoutInflater) }

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

        binding.tvName.text = intent.getStringExtra("name")
        binding.tilCounselRequestContent
            .editText!!.setText(intent.getStringExtra("counselRequest"))

        binding.tilCounselRequestContent.editText?.transitionName = "counselDetailTeacher"
    }
}