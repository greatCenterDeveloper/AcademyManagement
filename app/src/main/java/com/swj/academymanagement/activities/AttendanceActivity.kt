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

class AttendanceActivity : AppCompatActivity() {

    val binding:ActivityAttendanceBinding by lazy { ActivityAttendanceBinding.inflate(layoutInflater) }

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

        //val teacher = Gson().fromJson(intent.getStringExtra("teacher"), Member::class.java)

        binding.ivBackspace.setOnClickListener { finish() }

        // 시작 날짜 선택
        binding.tietStartDate.setOnClickListener {
            val dialog = DatePickerDialog(this)
            dialog.setOnDateSetListener { datePicker, i, i2, i3 ->
                val month = if(i2 < 10) "0${i2+1}" else (i2+1).toString()
                val day = if(i3 < 10) "0${i3}" else i3.toString()
                val date = "${i}-${month}-${day}"
                binding.tietStartDate.setText(date)

                RetrofitHelper.getRetrofitInstance().create(RetrofitStudentManagementService::class.java)
                    .studentAttendanceStartDateList(G.member.id, binding.tietStartDate.text.toString())
                    .enqueue(object :Callback<MutableList<StudentAttendance>> {
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
                val month = if(i2 < 10) "0${i2+1}" else (i2+1).toString()
                val day = if(i3 < 10) "0${i3}" else i3.toString()
                val date = "${i}-${month}-${day}"
                binding.tietEndDate.setText(date)

                RetrofitHelper.getRetrofitInstance().create(RetrofitStudentManagementService::class.java)
                    .studentAttendanceEndDateList(
                            G.member.id,
                            binding.tietStartDate.text.toString(),
                            binding.tietEndDate.text.toString())
                    .enqueue(object :Callback<MutableList<StudentAttendance>> {
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

        binding.btnSearch.setOnClickListener {
            val name = binding.tilName.editText?.text.toString()
            RetrofitHelper.getRetrofitInstance().create(RetrofitStudentManagementService::class.java)
                .studentAttendanceNameSearch(G.member.id, name)
                .enqueue(object :Callback<MutableList<StudentAttendance>> {
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


        RetrofitHelper.getRetrofitInstance().create(RetrofitStudentManagementService::class.java)
            .studentAttendanceList(G.member.id).enqueue(object : Callback<MutableList<StudentAttendance>>{
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

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(InputMethodManager::class.java)
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        currentFocus?.clearFocus()
        return super.dispatchTouchEvent(ev)
    }
}