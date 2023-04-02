package com.swj.academymanagement.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.swj.academymanagement.R
import com.swj.academymanagement.databinding.ActivityCounselDetailBinding

class CounselDetailActivity : AppCompatActivity() {

    val binding:ActivityCounselDetailBinding by lazy { ActivityCounselDetailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.tvName.text = intent.getStringExtra("name")
        binding.tilCounselRequestContent
            .editText!!.setText(intent.getStringExtra("counselRequest"))
    }
}