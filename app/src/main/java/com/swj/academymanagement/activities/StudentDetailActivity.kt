package com.swj.academymanagement.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.swj.academymanagement.adapters.StudentManagementCounselAdapter
import com.swj.academymanagement.adapters.StudentManagementCourseAdapter
import com.swj.academymanagement.adapters.StudentManagementMessageAdapter
import com.swj.academymanagement.databinding.ActivityStudentDetailBinding
import com.swj.academymanagement.model.Member
import com.swj.academymanagement.model.StudentManagementCounsel
import com.swj.academymanagement.model.StudentManagementCourse
import com.swj.academymanagement.model.StudentManagementMessage
import com.swj.academymanagement.network.RetrofitHelper
import com.swj.academymanagement.network.RetrofitStudentManagementService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentDetailActivity : AppCompatActivity() {

    val binding:ActivityStudentDetailBinding by lazy { ActivityStudentDetailBinding.inflate(layoutInflater) }

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

        binding.ivBackspace.setOnClickListener { finish() }


        val student:Member = Gson().fromJson(intent.getStringExtra("student"), Member::class.java)
        val teacherId:String = intent.getStringExtra("teacherId") ?: ""

        if(!student.profile.equals(""))
            Glide.with(this).load(student.profile).into(binding.ivProfile)

        binding.tvName.text = student.name
        binding.tvCourse.text = student.course
        binding.tvCall.text = student.call_number

        RetrofitHelper.getRetrofitInstance().create(RetrofitStudentManagementService::class.java)
            .studentManagementCourseList(student.id, teacherId).enqueue(object : Callback<MutableList<StudentManagementCourse>> {
                override fun onResponse(
                    call: Call<MutableList<StudentManagementCourse>>,
                    response: Response<MutableList<StudentManagementCourse>>
                ) {
                    val requestCourseArr = response.body()
                    if(requestCourseArr != null)
                        binding.recyclerCourse.adapter =
                            StudentManagementCourseAdapter(this@StudentDetailActivity, requestCourseArr)
                }

                override fun onFailure(
                    call: Call<MutableList<StudentManagementCourse>>,
                    t: Throwable
                ) {
                    AlertDialog.Builder(this@StudentDetailActivity)
                        .setMessage("error : ${t.message}")
                        .setPositiveButton("OK", null).show()
                }
            })

        val counselArr:MutableList<StudentManagementCounsel> = mutableListOf()
        counselArr.add(StudentManagementCounsel("2023-01-02", student.name, "상담 상담 상담 상담"))
        counselArr.add(StudentManagementCounsel("2023-01-10", student.name, "상담을 시작합니다. 상담을 완료하였습니다."))
        counselArr.add(StudentManagementCounsel("2023-02-02", student.name, "상담을 시작할까요? 상담을 종료합니다."))
        counselArr.add(StudentManagementCounsel("2023-02-12", student.name, "훌륭한 성적을 못내서 상담하였습니다."))
        counselArr.add(StudentManagementCounsel("2023-03-22", student.name, "길동군 더 어려운 시험 없냐고 상담하였습니다."))

        binding.recyclerCounsel.adapter = StudentManagementCounselAdapter(this, counselArr)


        RetrofitHelper.getRetrofitInstance().create(RetrofitStudentManagementService::class.java)
            .studentManagementMessageList(student.id, teacherId).enqueue(object : Callback<MutableList<StudentManagementMessage>>{
                override fun onResponse(
                    call: Call<MutableList<StudentManagementMessage>>,
                    response: Response<MutableList<StudentManagementMessage>>
                ) {
                    val messageArr = response.body()
                    if(messageArr != null)
                        binding.recyclerMessage.adapter =
                            StudentManagementMessageAdapter(this@StudentDetailActivity, messageArr)
                    else {
                        binding.tvNoMessage.visibility = View.VISIBLE
                        binding.recyclerMessage.visibility = View.GONE
                    }
                }

                override fun onFailure(
                    call: Call<MutableList<StudentManagementMessage>>,
                    t: Throwable
                ) {
                    AlertDialog.Builder(this@StudentDetailActivity)
                        .setMessage("error : ${t.message}")
                        .setPositiveButton("OK", null).show()
                }
            })
    }
}