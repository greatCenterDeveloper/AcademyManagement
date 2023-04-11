package com.swj.academymanagement.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.swj.academymanagement.adapters.TeachingBookAdapter
import com.swj.academymanagement.databinding.ActivityTeachingBookBinding
import com.swj.academymanagement.model.ShoppingItem

// 선생님 / 학생 권한 네이버 오픈 API로 도서 리스트 조회 화면
class TeachingBookActivity : AppCompatActivity() {

    val binding:ActivityTeachingBookBinding by lazy { ActivityTeachingBookBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 화면 전체 다 먹기
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        // 뒤로 가기
        binding.ivBackspace.setOnClickListener { finish() }

        val teachingBookArr:MutableList<ShoppingItem> = mutableListOf()

        teachingBookArr.add(
            ShoppingItem(
            "안드로이드", "", "", "10000", "30000", "교보문고")
        )
        teachingBookArr.add(ShoppingItem(
            "아이폰", "", "", "15000", "29000", "중앙문고")
        )
        teachingBookArr.add(ShoppingItem(
            "하이브리드웹앱", "", "", "11000", "25000", "서울문고")
        )
        binding.recycler.adapter = TeachingBookAdapter(this, teachingBookArr)
    }

    // 바깥 화면 터치 시 소프트 키보드 숨기기
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(InputMethodManager::class.java)
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        currentFocus?.clearFocus()
        return super.dispatchTouchEvent(ev)
    }
}