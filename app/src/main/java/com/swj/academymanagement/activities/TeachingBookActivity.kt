package com.swj.academymanagement.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.swj.academymanagement.adapters.TeachingBookAdapter
import com.swj.academymanagement.databinding.ActivityTeachingBookBinding
import com.swj.academymanagement.model.NaverSearchApiResponse
import com.swj.academymanagement.network.RetrofitNaverService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

// 선생님 / 학생 권한 네이버 오픈 API로 도서 리스트 조회 화면
class TeachingBookActivity : AppCompatActivity() {

    val binding:ActivityTeachingBookBinding by lazy { ActivityTeachingBookBinding.inflate(layoutInflater) }

    val retrofit:Retrofit by lazy {
        Retrofit.Builder()
        .baseUrl("https://openapi.naver.com")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
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

        // 책 제목 검색
        binding.btnSearch.setOnClickListener {
            // 입력한 책 제목
            val bookName = binding.tilBookName.editText?.text.toString()

            retrofit.create(RetrofitNaverService::class.java)
                .searchBookData(
                    bookName
                ).enqueue(object :Callback<NaverSearchApiResponse> {
                    override fun onResponse(
                        call: Call<NaverSearchApiResponse>,
                        response: Response<NaverSearchApiResponse>
                    ) {
                        val result = response.body()
                        if (result != null) {
                            if(result.items.size > 0)
                                binding.recycler.adapter = TeachingBookAdapter(this@TeachingBookActivity, result.items)
                            else Toast.makeText(this@TeachingBookActivity, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<NaverSearchApiResponse>, t: Throwable) {
                        Toast.makeText(this@TeachingBookActivity, "error : ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
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