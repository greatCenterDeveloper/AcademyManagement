package com.swj.academymanagement.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.gson.Gson
import com.swj.academymanagement.R
import com.swj.academymanagement.databinding.ActivityCounselRequestUpdateBinding
import com.swj.academymanagement.model.CounselRequest
import com.swj.academymanagement.network.RetrofitCounselStudentService
import com.swj.academymanagement.network.RetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

// 학생 권한 상담 신청 내용 수정 화면
class CounselRequestUpdateActivity : AppCompatActivity() {

    val binding:ActivityCounselRequestUpdateBinding by lazy {
        ActivityCounselRequestUpdateBinding.inflate(layoutInflater)
    }

    // 상담 시작 시간
    private var startTime:String = ""

    // 상담 마지막 시간
    private var endTime:String = ""

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

        // 상담 신청 내용 수정을 위해 상담 신청 내용 가져오기
        val counselRequest = Gson().fromJson(intent.getStringExtra("counselRequest"), CounselRequest::class.java)

        // 뒤로 가기
        binding.ivBackspace.setOnClickListener { finish() }

        // 상담 시작 / 상담 마지막 시간 시간 리스트
        val timeList = resources.getStringArray(R.array.time_list)

        // 기존에 입력된 상담 시작 시간 및 마지막 시간 입력
        binding.acTvStartTime.setText(counselRequest.startTime)
        binding.acTvEndTime.setText(counselRequest.endTime)

        // 기존에 입력된 상담 시작 시간 및 마지막 시간 시간 변수에 대입
        startTime = counselRequest.startTime
        endTime = counselRequest.endTime

        // 상담 시작 시간 / 상담 마지막 시간 리스트를 시간 드롭다운 어댑터에 넣기
        val timeAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, timeList)
        binding.acTvStartTime.setAdapter(timeAdapter)
        binding.acTvEndTime.setAdapter(timeAdapter)

        binding.acTvStartTime.setOnItemClickListener { _, _, i, _ ->
            // 아이템 배열에서 선택된 아이템 가져오기 (시작 시간)
            startTime = timeList[i]
        }
        binding.acTvEndTime.setOnItemClickListener { _, _, i, _ ->
            // 아이템 배열에서 선택된 아이템 가져오기 (끝 시간)
            endTime = timeList[i]
        }

        // 학생 권한 상담 신청 리스트에서 상담 신청 수정 화면으로 이동 시 화면 전환 효과
        binding.tilCounselContent.editText?.transitionName = "counselUpdate"

        // 현재 날짜 가져오기
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        sdf.timeZone = TimeZone.getTimeZone("Asia/Seoul")

        // 상담 신청일
        binding.tvCounselDate.text = sdf.format(Date())

        // 입력된 상담 신청 내용 가져오기
        binding.tilCounselContent.editText?.setText(counselRequest.content)

        // 상담 신청 내용 수정
        binding.btnCounselRequest.setOnClickListener {
            // 상담 신청일
            val date = binding.tvCounselDate.text.toString()

            // 상담 신청 내용
            val counselContent = binding.tilCounselContent.editText?.text.toString()

            // 상담 신청 내용 상담 신청 객체에 저장
            val counselRequestUpdate =
                CounselRequest(date, startTime, endTime, counselContent, counselRequest.studentId, counselRequest.counselRequestCode)

            // 상담 시작 시간 및 마지막 시간 입력 안했을 시..
            if(startTime == "" || endTime == "") {
                Toast.makeText(this, "상담 시간을 선택하세요.", Toast.LENGTH_SHORT).show()
            } else {
                // 상담 신청 내용 수정
                RetrofitHelper.getRetrofitInstance().create(RetrofitCounselStudentService::class.java)
                    .counselRequestUpdate(counselRequestUpdate).enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            val message = response.body()

                            Toast.makeText(this@CounselRequestUpdateActivity, message, Toast.LENGTH_SHORT).show()

                            if(message?.contains("완료") ?: false) {  // 넘어오는 문자열 : 상담 신청 수정 완료 이므로..
                                startActivity(Intent(this@CounselRequestUpdateActivity, CounselRequestActivity::class.java))
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Toast.makeText(this@CounselRequestUpdateActivity, "error : ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
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