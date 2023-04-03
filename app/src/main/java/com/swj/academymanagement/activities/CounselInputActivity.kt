package com.swj.academymanagement.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.swj.academymanagement.R
import com.swj.academymanagement.databinding.ActivityCounselInputBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class CounselInputActivity : AppCompatActivity() {

    val binding:ActivityCounselInputBinding by lazy {
        ActivityCounselInputBinding.inflate(layoutInflater)
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
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        sdf.timeZone = TimeZone.getTimeZone("Asia/Seoul")
        binding.tvCounselDate.text = sdf.format(Date())

        binding.btnCounselRequest.setOnClickListener {
            val date = binding.tvCounselDate.text.toString()
            val startTime = ""
            val endTime = ""
            binding.acTvStartTime.setOnItemClickListener { adapterView, view, i, l ->
                // 아이템 배열에서 선택된 아이템 가져오기 (시작 시간)
            }
            binding.acTvEndTime.setOnItemClickListener { adapterView, view, i, l ->
                // 아이템 배열에서 선택된 아이템 가져오기 (끝 시간)
            }
            val counselContent = binding.tilCounselContent.editText?.text.toString()
            Toast.makeText(this, counselContent, Toast.LENGTH_SHORT).show()
        }
    }
}