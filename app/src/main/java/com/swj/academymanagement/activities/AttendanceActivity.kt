package com.swj.academymanagement.activities

import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.swj.academymanagement.G
import com.swj.academymanagement.adapters.AttendanceAdapter
import com.swj.academymanagement.databinding.ActivityAttendanceBinding
import com.swj.academymanagement.model.StudentAttendance
import com.swj.academymanagement.network.RetrofitHelper
import com.swj.academymanagement.network.RetrofitStudentManagementService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 선생님 권한 선생님의 강좌에 수강 중인 학생들의 학원 등원 하원 리스트 조회 화면
class AttendanceActivity : AppCompatActivity() {

    val binding:ActivityAttendanceBinding by lazy { ActivityAttendanceBinding.inflate(layoutInflater) }

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

        // 시작 날짜 선택
        binding.tietStartDate.setOnClickListener {
            val dialog = DatePickerDialog(this)
            dialog.setOnDateSetListener { datePicker, i, i2, i3 ->
                // 월 -> 월이 1글자일 경우 앞에 0 붙임
                val month = if(i2 < 10) "0${i2+1}" else (i2+1).toString()

                // 일 -> 일이 1글자일 경우 앞에 0 붙임
                val day = if(i3 < 10) "0${i3}" else i3.toString()

                // 연도 문자열 ( ex.2023-04-09 )
                val date = "${i}-${month}-${day}"

                // 완성된 시작 날짜 문자열 넣기
                binding.tietStartDate.setText(date)

                // 시작 날짜부터 학원에 등원한 학생 리스트 보여주기
                RetrofitHelper.getRetrofitInstance().create(RetrofitStudentManagementService::class.java)
                    .studentAttendanceStartDateList(
                        G.member.id,                            // 선생님 아이디
                        binding.tietStartDate.text.toString()   // 시작 날짜
                    ).enqueue(object :Callback<MutableList<StudentAttendance>> {
                        override fun onResponse(
                            call: Call<MutableList<StudentAttendance>>,
                            response: Response<MutableList<StudentAttendance>>
                        ) {
                            val saa = response.body()
                            if(saa != null) binding.recycler.adapter = AttendanceAdapter(this@AttendanceActivity, saa)
                        }

                        override fun onFailure(
                            call: Call<MutableList<StudentAttendance>>,
                            t: Throwable
                        ) {
                            AlertDialog.Builder(this@AttendanceActivity)
                                .setMessage("error : ${t.message}")
                                .setPositiveButton("OK", null).show()
                        }
                    })
            }
            dialog.show()
        }

        // 마지막 날짜 선택
        binding.tietEndDate.setOnClickListener {
            val dialog = DatePickerDialog(this)
            dialog.setOnDateSetListener { datePicker, i, i2, i3 ->
                // 월 -> 월이 1글자일 경우 앞에 0 붙임
                val month = if(i2 < 10) "0${i2+1}" else (i2+1).toString()

                // 일 -> 일이 1글자일 경우 앞에 0 붙임
                val day = if(i3 < 10) "0${i3}" else i3.toString()

                // 연도 문자열 ( ex.2023-04-09 )
                val date = "${i}-${month}-${day}"

                // 완성된 마지막 날짜 문자열 넣기
                binding.tietEndDate.setText(date)

                // 마지막 날짜까지 학원에 등원한 학생 리스트 보여주기
                RetrofitHelper.getRetrofitInstance().create(RetrofitStudentManagementService::class.java)
                    .studentAttendanceEndDateList(
                            G.member.id,                            // 선생님 아이디
                            binding.tietStartDate.text.toString(),  // 시작 날짜
                            binding.tietEndDate.text.toString()     // 마지막 날짜
                    ).enqueue(object :Callback<MutableList<StudentAttendance>> {
                        override fun onResponse(
                            call: Call<MutableList<StudentAttendance>>,
                            response: Response<MutableList<StudentAttendance>>
                        ) {
                            val saa = response.body()
                            if(saa != null) binding.recycler.adapter = AttendanceAdapter(this@AttendanceActivity, saa)
                        }

                        override fun onFailure(
                            call: Call<MutableList<StudentAttendance>>,
                            t: Throwable
                        ) {
                            AlertDialog.Builder(this@AttendanceActivity)
                                .setMessage("error : ${t.message}")
                                .setPositiveButton("OK", null).show()
                        }
                    })
            }
            dialog.show()
        }

        // 학생 이름 검색 ( 입력한 이름이 포함된 모든 학생 리스트 )
        binding.btnSearch.setOnClickListener {
            val name = binding.tilName.editText?.text.toString()
            RetrofitHelper.getRetrofitInstance().create(RetrofitStudentManagementService::class.java)
                .studentAttendanceNameSearch(
                    G.member.id,    // 선생님 아이디
                    name            // 검색할 학생 이름
                ).enqueue(object :Callback<MutableList<StudentAttendance>> {
                    override fun onResponse(
                        call: Call<MutableList<StudentAttendance>>,
                        response: Response<MutableList<StudentAttendance>>
                    ) {
                        val saa = response.body()
                        if(saa != null) binding.recycler.adapter = AttendanceAdapter(this@AttendanceActivity, saa)
                    }

                    override fun onFailure(
                        call: Call<MutableList<StudentAttendance>>,
                        t: Throwable
                    ) {
                        AlertDialog.Builder(this@AttendanceActivity)
                            .setMessage("error : ${t.message}")
                            .setPositiveButton("OK", null).show()
                    }
                })
            binding.tilName.editText?.setText("")
        }

        // 내 강좌에 수강 중인 학생들의 학원 등원 하원 리스트
        RetrofitHelper.getRetrofitInstance().create(RetrofitStudentManagementService::class.java)
            .studentAttendanceList(
                G.member.id     // 선생님 이름
            ).enqueue(object : Callback<MutableList<StudentAttendance>>{
                override fun onResponse(
                    call: Call<MutableList<StudentAttendance>>,
                    response: Response<MutableList<StudentAttendance>>
                ) {
                    val saa = response.body()
                    if(saa != null) binding.recycler.adapter = AttendanceAdapter(this@AttendanceActivity, saa)
                }

                override fun onFailure(call: Call<MutableList<StudentAttendance>>, t: Throwable) {
                    AlertDialog.Builder(this@AttendanceActivity)
                        .setMessage("error : ${t.message}")
                        .setPositiveButton("OK", null).show()
                }
            })
    }

    // 바깥 화면 터치 시 소프트 키보드 숨기기
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(InputMethodManager::class.java)
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        currentFocus?.clearFocus()
        return super.dispatchTouchEvent(ev)
    }
}