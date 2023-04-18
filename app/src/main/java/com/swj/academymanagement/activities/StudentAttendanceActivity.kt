package com.swj.academymanagement.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.swj.academymanagement.G
import com.swj.academymanagement.databinding.ActivityStudentAttendanceBinding
import com.swj.academymanagement.network.RetrofitAttendanceService
import com.swj.academymanagement.network.RetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

// 학생 권한 등원 / 하원 버튼 클릭 화면
class StudentAttendanceActivity : AppCompatActivity() {

    val binding:ActivityStudentAttendanceBinding by lazy {
        ActivityStudentAttendanceBinding.inflate(layoutInflater)
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

        // 현재 날짜 가져오기
        var sdf = SimpleDateFormat("yyyy-MM-dd")
        sdf.timeZone = TimeZone.getTimeZone("Asia/Seoul")

        // 현재 날짜 : "yyyy-MM-dd"
        val toDayDate = sdf.format(Date())
        // 등원 시간
        var attendanceTime = ""
        // 하원 시간
        var theLowerHouseTime = ""

        // 등원 버튼
        binding.btnAttendance.setOnClickListener {
            // 등원 버튼 눌렀을 당시의 시간 가져오기
            sdf = SimpleDateFormat("HH:mm")
            sdf.timeZone = TimeZone.getTimeZone("Asia/Seoul")

            // 등원 시간에 가져온 시간 대입
            attendanceTime = sdf.format(Date())

            // retrofit attendance Table Insert 작업
            RetrofitHelper.getRetrofitInstance().create(RetrofitAttendanceService::class.java)
                .studentAttendance(
                    G.member.id,        // 학생 아이디
                    attendanceTime      // 등원 시간
                ).enqueue(object :Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        val message = response.body()

                        if(message != null) {
                            // attendance 테이블에 insert 처리된 경우 넘어오는 문자열이 [attendanceCode],[학생 이름] 등원 완료! 이므로..
                            val messageArr = message.split(",")

                            // attendance 테이블에 insert 처리된 경우 넘어오는 문자열이 [attendanceCode],[학생 이름] 등원 완료! 이므로.. size는 2가 된다.
                            if(messageArr.size > 1) {
                                // attendance 테이블에 Insert한 후 하원 버튼 클릭시 attendance의 PK인 attendanceCode가 필요하므로..
                                // 글로벌 전역 변수 클래스인 G 클래스의 attendanceCode에 넣어준다.
                                G.attendanceCode = messageArr[0]

                                Toast.makeText(this@StudentAttendanceActivity, messageArr[1], Toast.LENGTH_SHORT).show()

                                if(messageArr[1].contains("완료")) {  // [학생 이름] 등원 완료! 이므로..
                                    startActivity(Intent(this@StudentAttendanceActivity, StudentActivity::class.java))
                                    finish()
                                }
                            } else {
                                // 이미 attendance 테이블에 insert 처리된 경우 넘어오는 문자열이 이미 등원 처리되었습니다. 이므로.. size는 1이 된다.
                                Toast.makeText(this@StudentAttendanceActivity, messageArr[0], Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@StudentAttendanceActivity, StudentActivity::class.java))
                                finish()
                            }
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(this@StudentAttendanceActivity, "error : ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        // 하원 버튼
        binding.btnGohome.setOnClickListener {
            // 하원 버튼 눌렀을 당시의 시간 가져오기
            sdf = SimpleDateFormat("HH:mm")
            sdf.timeZone = TimeZone.getTimeZone("Asia/Seoul")

            // 하원 시간에 가져온 시간 대입
            theLowerHouseTime = sdf.format(Date())

            // retrofit attendance Table Update 작업
            RetrofitHelper.getRetrofitInstance().create(RetrofitAttendanceService::class.java)
                .studentTheLowerHouse(
                    G.attendanceCode,       // attendance 테이블의 attendanceCode (PK)
                    G.member.id,            // 학생 아이디
                    theLowerHouseTime       // 하원 시간
                ).enqueue(object :Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        val message = response.body()

                        if(message != null) {
                            Toast.makeText(this@StudentAttendanceActivity, message, Toast.LENGTH_SHORT).show()

                            if(message.contains("완료")) {  // [학생 이름] 하원 완료! 이므로..
                                startActivity(Intent(this@StudentAttendanceActivity, StudentActivity::class.java))
                                finish()
                            }
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(this@StudentAttendanceActivity, "error : ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}