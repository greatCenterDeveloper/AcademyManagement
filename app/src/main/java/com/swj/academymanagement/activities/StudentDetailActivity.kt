package com.swj.academymanagement.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.swj.academymanagement.G
import com.swj.academymanagement.adapters.StudentManagementCounselAdapter
import com.swj.academymanagement.adapters.StudentManagementCourseAdapter
import com.swj.academymanagement.adapters.StudentManagementMessageAdapter
import com.swj.academymanagement.databinding.ActivityStudentDetailBinding
import com.swj.academymanagement.model.CounselCurrent
import com.swj.academymanagement.model.CounselRequestTeacher
import com.swj.academymanagement.model.Member
import com.swj.academymanagement.model.StudentManagementCourse
import com.swj.academymanagement.model.StudentManagementMessage
import com.swj.academymanagement.network.RetrofitCounselService
import com.swj.academymanagement.network.RetrofitHelper
import com.swj.academymanagement.network.RetrofitMemberService
import com.swj.academymanagement.network.RetrofitStudentManagementService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

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

        // member 테이블의 Primary Key인 id로 FirebaseStorage에 저장된 profile 이미지 이름 가져오기
        RetrofitHelper.getRetrofitInstance().create(RetrofitMemberService::class.java)
            .getMemberProfile(
                student.id     // 학생 아이디
            ).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {

                    // 디비에 저장된 프로필 사진 이름 가져오기
                    val profile = response.body()

                    // FirebaseStorage에서 불러온 사진 프로필 이미지 공간에 붙이기
                    val storage = FirebaseStorage.getInstance()
                    val imgRef: StorageReference = storage.getReference().child("profileImage/$profile")
                    imgRef.downloadUrl.addOnSuccessListener {

                        // 학생 프로필 이미지
                        Glide.with(this@StudentDetailActivity).load(it).into(binding.ivProfile)
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {}
            })


        // 학생 이름
        binding.tvName.text = student.name

        // 학생이 수강 중인 강좌들
        binding.tvCourse.text = student.course

        // 학생 휴대폰 번호
        binding.tvCall.text = student.call_number

        // 상담 하기 버튼
        binding.btnCounsel.setOnClickListener {
            val intent = Intent(this, CounselDetailActivity::class.java)

            // 상담 테이블의 외래키로 상담 신청 테이블의 상담 신청 코드가 설정 되어 있으므로..
            // 여기서 화면 전환을 한다면 상담 신청 테이블에 하나 insert 해라. (php에서 이 값을 보고 insert 처리)
            val counselRequestCode = "insert"

            // 학생의 아이디
            val studentId = student.id

            // 학생 이름
            val studentName = student.name

            // 현재 날짜 가져오기
            var sdf = SimpleDateFormat("yyyy-MM-dd")
            sdf.timeZone = TimeZone.getTimeZone("Asia/Seoul")

            // 현재 날짜
            val date = sdf.format(Date())

            // 현재 시간 가져오기
            sdf = SimpleDateFormat("HH:mm")
            sdf.timeZone = TimeZone.getTimeZone("Asia/Seoul")

            // 상담 시작 시간
            val counselStartTime = sdf.format(Date())

            // 상담 마지막 시간 -> 아직 상담 내용 입력 전이므로 ""으로..
            val counselEndTime = ""

            // 상담 신청 내용 -> 학생이 상담 신청해서 넘어간게 아니라 선생님 임의 상담이므로..
            val counselContent = "선생님 임의 상담"

            // 상담 내용 입력 화면에 넘겨줄 상담 신청 객체
            val crt = CounselRequestTeacher(
                            counselRequestCode,     // 상담 신청 코드 (상담 신청 테이블에 추가하기 위해 "insert" 문자열 넘겨줌.)
                            studentId,              // 학생 아이디
                            studentName,            // 학생 이름
                            date,                   // 상담 신청일
                            date,                   // 상담일
                            counselStartTime,       // 상담 시작 시간
                            counselEndTime,         // 상담 마지막 시간
                            counselContent          // 상담 신청 내용
                      )
            intent.putExtra("counselRequest", Gson().toJson(crt))
            startActivity(intent)
            finish()
        }

        // 문자 보내기 버튼
        binding.btnSendMessage.setOnClickListener {
            val intent = Intent(this, SmsSendActivity::class.java)

            // 학생 아이디
            intent.putExtra("studentId", student.id)

            // 학생 이름
            intent.putExtra("studentName", student.name)

            // 학생 휴대폰 번호
            intent.putExtra("studentCall", student.call_number)

            startActivity(intent)
            finish()
        }

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
                    Toast.makeText(this@StudentDetailActivity, "error : ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })


        // 학생과 상담한 상담 내역 리스트
        RetrofitHelper.getRetrofitInstance().create(RetrofitCounselService::class.java)
            .studentCounselList(
                student.id,     // 학생 아이디
                G.member.id     // 선생 아이디
            ).enqueue(object :Callback<MutableList<CounselCurrent>> {
                override fun onResponse(
                    call: Call<MutableList<CounselCurrent>>,
                    response: Response<MutableList<CounselCurrent>>
                ) {
                    val counselCurrentArr = response.body()
                    if(counselCurrentArr != null) {
                        if (counselCurrentArr.size > 0) {
                            binding.recyclerCounsel.adapter =
                                StudentManagementCounselAdapter(this@StudentDetailActivity, counselCurrentArr)
                        } else {
                            binding.tvNoCounsel.visibility = View.VISIBLE
                            binding.recyclerCounsel.visibility = View.GONE
                        }
                    }
                }

                override fun onFailure(call: Call<MutableList<CounselCurrent>>, t: Throwable) {
                    Toast.makeText(this@StudentDetailActivity, "error : ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })


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
                    Toast.makeText(this@StudentDetailActivity, "error : ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}