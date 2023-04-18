package com.swj.academymanagement.activities

import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.swj.academymanagement.adapters.CounselAdapter
import com.swj.academymanagement.databinding.ActivityCounselBinding

// 선생님 권한 상담 신청 / 상담 현황 리스트 조회 화면
class CounselActivity : AppCompatActivity() {

    val binding:ActivityCounselBinding by lazy { ActivityCounselBinding.inflate(layoutInflater) }

    // 탭 제목
    private val tabTitle = arrayOf("상담 신청", "상담 현황")

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

        // 상담 신청 / 상담 현황 Fragment 보여줄 뷰
        binding.pager.adapter = CounselAdapter(this)

        val mediator = TabLayoutMediator(binding.tabLayout, binding.pager){
                tab, position -> tab.text = tabTitle[position]
        }
        mediator.attach()

        if(intent.getStringExtra("counsel") != null) {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1))
        }
    }

    // 바깥 화면 터치 시 소프트 키보드 숨기기
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(InputMethodManager::class.java)
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        currentFocus?.clearFocus()
        return super.dispatchTouchEvent(ev)
    }
}