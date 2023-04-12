package com.swj.academymanagement.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.swj.academymanagement.G
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

// 선생님 권한 학생 관리 리스트에서 특정 학생 상세 정보 화면으로 이동한 화면
class StudentDetailActivity : AppCompatActivity() {

    val binding:ActivityStudentDetailBinding by lazy { ActivityStudentDetailBinding.inflate(layoutInflater) }

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

        // 학생 관리 리스트에서 학생 상세 정보 화면으로 오기 위해 학생 상세 정보 가져오기
        val student:Member = Gson().fromJson(intent.getStringExtra("student"), Member::class.java)

        // 학생 프로필 이미지
        if(!student.profile.equals(""))
            Glide.with(this).load(student.profile).into(binding.ivProfile)

        // 학생 이름
        binding.tvName.text = student.name

        // 학생이 수강 중인 강좌들
        binding.tvCourse.text = student.course

        // 학생 휴대폰 번호
        binding.tvCall.text = student.call_number

        // 이 학생이 수강 중인 강좌 리스트 조회
        RetrofitHelper.getRetrofitInstance().create(RetrofitStudentManagementService::class.java)
            .studentCourseList(
                student.id,     // 학생 아이디
                G.member.id     // 선생 아이디
            ).enqueue(object : Callback<MutableList<StudentManagementCourse>> {
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

        // 학생에게 보낸 문자 리스트 조회
        RetrofitHelper.getRetrofitInstance().create(RetrofitStudentManagementService::class.java)
            .studentMessageList(
                student.id,     // 학생 아이디
                G.member.id     // 선생 아이디
            ).enqueue(object : Callback<MutableList<StudentManagementMessage>>{
                override fun onResponse(
                    call: Call<MutableList<StudentManagementMessage>>,
                    response: Response<MutableList<StudentManagementMessage>>
                ) {
                    val messageArr = response.body()
                    if(messageArr != null) {
                        if(messageArr.size > 0) {
                            binding.recyclerMessage.adapter =
                                StudentManagementMessageAdapter(this@StudentDetailActivity, messageArr)
                        } else {
                            binding.tvNoMessage.visibility = View.VISIBLE
                            binding.recyclerMessage.visibility = View.GONE
                        }
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