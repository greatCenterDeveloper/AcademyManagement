package com.swj.academymanagement.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import com.swj.academymanagement.G
import com.swj.academymanagement.databinding.ActivityCounselDetailBinding
import com.swj.academymanagement.model.CounselCurrent
import com.swj.academymanagement.model.CounselRequestTeacher
import com.swj.academymanagement.network.RetrofitCounselService
import com.swj.academymanagement.network.RetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 선생님 권한 상담 내용 저장 화면
class CounselDetailActivity : AppCompatActivity() {

    val binding:ActivityCounselDetailBinding by lazy { ActivityCounselDetailBinding.inflate(layoutInflater) }

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

        // 학생의 상담 신청 리스트에서 상담 내용 작성을 위해 상담 내용 작성 버튼 클릭 시 기존에 학생이 입력한 상담 신청 내용 가져오기
        val crt = Gson().fromJson(intent.getStringExtra("counselRequest"), CounselRequestTeacher::class.java)

        // 상담 신청 학생 이름
        binding.tvName.text = crt.name

        // 학생이 신청한 상담 신청 내용
        binding.tilCounselRequestContent.editText!!.setText(crt.counselContent)

        // 작성한 상담 내용 저장 버튼
        binding.ivSave.setOnClickListener {
            // 상담 내용
            val counselContent = binding.tilCounselContent.editText?.text.toString()

            // 상담한 내용 상담 객체에 저장
            val counselCurrent = CounselCurrent(
                counselRequestCode= crt.counselRequestCode, // 상담 신청 코드 FK ( 학생의 상담 신청 테이블의 상담 신청 코드 PK )
                studentId= crt.studentId,                   // 학생 아이디 FK ( 학생 테이블의 학생 아이디 PK )
                teacherId= G.member.id,                     // 선생 아이디 FK ( 선생 테이블의 선생 아이디 PK )
                date= crt.counselDate,                      // 상담 일자
                counselContent=  counselContent)            // 상담 내용

            // 상담 내용 저장
            RetrofitHelper.getRetrofitInstance().create(RetrofitCounselService::class.java)
                .counselInsert(counselCurrent).enqueue(object :Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        val message = response.body()
                        AlertDialog.Builder(this@CounselDetailActivity)
                            .setMessage(message)
                            .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                                if(message?.contains("완료") ?: false) { // 넘어오는 문자열 : 상담 작성 완료 이므로..
                                    startActivity(Intent(this@CounselDetailActivity, CounselActivity::class.java))
                                    finish()
                                }
                            }).show()
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        AlertDialog.Builder(this@CounselDetailActivity)
                            .setMessage("error : ${t.message}")
                            .setPositiveButton("OK", null).show()
                    }
                })
        }

        // 선생님 권한 학생의 상담 신청 리스트에서 상담 내용 작성 화면으로 이동 시 화면 전환 효과
        binding.tilCounselRequestContent.editText?.transitionName = "counselDetailTeacher"
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