package com.swj.academymanagement.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.gson.Gson
import com.swj.academymanagement.G
import com.swj.academymanagement.databinding.ActivityCounselDetailUpdateBinding
import com.swj.academymanagement.model.CounselCurrent
import com.swj.academymanagement.network.RetrofitCounselService
import com.swj.academymanagement.network.RetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 선생님 권한 상담 내용 수정 화면
class CounselDetailUpdateActivity : AppCompatActivity() {

    val binding:ActivityCounselDetailUpdateBinding by lazy {
        ActivityCounselDetailUpdateBinding.inflate(layoutInflater)
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

        // 상담 현황 리스트에서 상담 내용 수정을 위해 기존에 입력된 상담 내용 가져오기
        val counsel = Gson().fromJson(intent.getStringExtra("counsel"), CounselCurrent::class.java)

        // 상담 신청 학생 이름
        binding.tvName.text = counsel.studentName

        // 학생이 신청한 상담 신청 내용
        binding.tilCounselRequestContent.editText?.setText(counsel.counselRequestContent)

        // 상담 내용 입력에 포커스 주기
        binding.tilCounselContent.editText?.requestFocus()

        // 기존에 입력된 상담 내용
        binding.tilCounselContent.editText?.setText(counsel.counselContent)

        // 작성한 상담 내용 수정 버튼
        binding.ivSave.setOnClickListener {
            // 수정한 상담 내용
            val counselContent = binding.tilCounselContent.editText?.text.toString()

            // 수정한 상담한 내용 상담 객체에 저장
            val counselCurrent = CounselCurrent(
                counselRequestCode= counsel.counselRequestCode, // 상담 신청 코드 FK ( 학생의 상담 신청 테이블의 상담 신청 코드 PK )
                studentId= counsel.studentId,                   // 학생 아이디 FK ( 학생 테이블의 학생 아이디 PK )
                teacherId= G.member.id,                         // 선생 아이디 FK ( 선생 테이블의 선생 아이디 PK )
                date= counsel.date,                             // 상담 일자
                counselContent=  counselContent,                // 상담 내용
                counselCode = counsel.counselCode)              // 상담 코드 PK

            // 상담 내용 수정
            RetrofitHelper.getRetrofitInstance().create(RetrofitCounselService::class.java)
                .counselUpdate(counselCurrent).enqueue(object :Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        val message = response.body()

                        Toast.makeText(this@CounselDetailUpdateActivity, message, Toast.LENGTH_SHORT).show()

                        if(message?.contains("완료") ?: false) { // 넘어오는 문자열 : 상담 수정 완료 이므로..
                            startActivity(Intent(this@CounselDetailUpdateActivity,
                                CounselActivity::class.java))
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(this@CounselDetailUpdateActivity, "error : ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    // 바깥 화면 터치 시 소프트 키보드 숨기기
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if(binding.tilCounselContent.editText?.text.toString() != "") {
            val imm: InputMethodManager = getSystemService(InputMethodManager::class.java)
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            currentFocus?.clearFocus()
        }
        return super.dispatchTouchEvent(ev)
    }
}