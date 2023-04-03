package com.swj.academymanagement.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.swj.academymanagement.R
import com.swj.academymanagement.adapters.CounselRequestAdapter
import com.swj.academymanagement.databinding.ActivityCounselRequestBinding
import com.swj.academymanagement.model.CounselRequest

class CounselRequestActivity : AppCompatActivity() {

    val binding:ActivityCounselRequestBinding by lazy {
        ActivityCounselRequestBinding.inflate(layoutInflater)
    }

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
        binding.fab.setOnClickListener {
            startActivity(Intent(this, CounselInputActivity::class.java))
        }

        val counselRequestArr:MutableList<CounselRequest> = mutableListOf()
        counselRequestArr.add(CounselRequest(
            "2023/03/01", "16:00", "16:30", "가강사", "상담이 필요합니다.")
        )
        counselRequestArr.add(CounselRequest(
            "2023/03/05", "16:10", "16:40", "나강사", "상담이 필요하오.")
        )
        counselRequestArr.add(CounselRequest(
            "2023/03/10", "15:00", "15:30", "다강사", "상담이 필요하옵니다.")
        )

        if(counselRequestArr.size == 0) {
            binding.tvNoCounselRequest.visibility = View.VISIBLE
            binding.recycler.visibility = View.GONE
        }
        else binding.recycler.adapter = CounselRequestAdapter(this, counselRequestArr)

    }
}