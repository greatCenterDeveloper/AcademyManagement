package com.swj.academymanagement.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.swj.academymanagement.databinding.ActivityTeachingBookUrlBinding

// 선생님 / 학생 권한 도서 쇼핑몰 웹뷰 화면
class TeachingBookUrlActivity : AppCompatActivity() {

    val binding:ActivityTeachingBookUrlBinding by lazy {
        ActivityTeachingBookUrlBinding.inflate(layoutInflater)
    }

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

        // 현재 웹뷰 안에서 웹문서가 열리도록 설정
        binding.wv.webViewClient = WebViewClient()

        // 웹 문서 안에서 다이얼로그 같은 것이 열리도록 설정
        binding.wv.webChromeClient = WebChromeClient()

        // 웹뷰 안에서 자바스크립트 동작 허용
        binding.wv.settings.javaScriptEnabled = true

        // 어댑터로부터 가져온 책 쇼핑몰 url
        val bookUrl = intent.getStringExtra("bookUrl") ?: ""

        // 웹뷰에 도서 쇼핑몰 보여주기
        binding.wv.loadUrl(bookUrl)
    }

    override fun onBackPressed() {
        // 뒤로가기 눌렀을 때 웹뷰 안에서 움직이도록 설정
        if(binding.wv.canGoBack()) binding.wv.goBack()
        else super.onBackPressed()
    }
}