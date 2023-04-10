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
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import com.swj.academymanagement.R
import com.swj.academymanagement.databinding.ActivityCounselRequestUpdateBinding
import com.swj.academymanagement.model.CounselRequest
import com.swj.academymanagement.model.Member
import com.swj.academymanagement.network.RetrofitCounselStudentService
import com.swj.academymanagement.network.RetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class CounselRequestUpdateActivity : AppCompatActivity() {

    val binding:ActivityCounselRequestUpdateBinding by lazy {
        ActivityCounselRequestUpdateBinding.inflate(layoutInflater)
    }

    var startTime:String = ""
    var endTime:String = ""

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

        val counselRequest = Gson().fromJson(intent.getStringExtra("counselRequest"), CounselRequest::class.java)

        binding.ivBackspace.setOnClickListener { finish() }


        val timeList = resources.getStringArray(R.array.time_list)
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

        binding.tilCounselContent.editText?.transitionName = "counselUpdate"

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        sdf.timeZone = TimeZone.getTimeZone("Asia/Seoul")
        binding.tvCounselDate.text = sdf.format(Date())

        // 입력된 데이터 가져오기
        //binding.acTvStartTime.
        //binding.acTvEndTime.setText(counselRequest.endTime)
        binding.tilCounselContent.editText?.setText(counselRequest.content)


        binding.btnCounselRequest.setOnClickListener {
            val date = binding.tvCounselDate.text.toString()
            val counselContent = binding.tilCounselContent.editText?.text.toString()
            val counselRequestUpdate =
                CounselRequest(date, startTime, endTime, counselContent, counselRequest.studentId, counselRequest.counselRequestCode)

            if(startTime == "" && endTime == "") {
                AlertDialog.Builder(this@CounselRequestUpdateActivity)
                    .setMessage("상담 시간을 선택하세요.")
                    .setPositiveButton("OK", null).show()
            } else {
                RetrofitHelper.getRetrofitInstance().create(RetrofitCounselStudentService::class.java)
                    .counselRequestUpdate(counselRequestUpdate).enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            val message = response.body()
                            AlertDialog.Builder(this@CounselRequestUpdateActivity)
                                .setMessage(message)
                                .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                                    if(message?.contains("완료") ?: false) {
                                        startActivity(Intent(this@CounselRequestUpdateActivity, CounselRequestActivity::class.java))
                                        finish()
                                    }
                                }).show()
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            AlertDialog.Builder(this@CounselRequestUpdateActivity)
                                .setMessage("error : ${t.message}")
                                .setPositiveButton("OK", null).show()
                        }
                    })
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(InputMethodManager::class.java)
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        currentFocus?.clearFocus()
        return super.dispatchTouchEvent(ev)
    }
}