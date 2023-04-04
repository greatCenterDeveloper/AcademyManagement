package com.swj.academymanagement.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.gson.Gson
import com.swj.academymanagement.databinding.ActivityCounselDetailBinding
import com.swj.academymanagement.model.CounselRequestTeacher

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

        val crt = Gson().fromJson(intent.getStringExtra("counselRequest"), CounselRequestTeacher::class.java)
        binding.tvName.text = crt.name
        binding.tilCounselRequestContent
            .editText!!.setText(crt.counselContent)

        binding.ivSave.setOnClickListener {
            val counselContent = binding.tilCounselContent.editText?.text.toString()
            Toast.makeText(this, counselContent, Toast.LENGTH_SHORT).show()
            // finish()
        }

        binding.tilCounselRequestContent.editText?.transitionName = "counselDetailTeacher"
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if(binding.tilCounselContent.editText?.text.toString() != "") {
            val imm: InputMethodManager = getSystemService(InputMethodManager::class.java)
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            currentFocus?.clearFocus()
        }
        return super.dispatchTouchEvent(ev)
    }
}