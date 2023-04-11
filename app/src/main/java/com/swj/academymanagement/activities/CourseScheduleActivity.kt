package com.swj.academymanagement.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.swj.academymanagement.G
import com.swj.academymanagement.adapters.CourseScheduleAdapter
import com.swj.academymanagement.adapters.CourseScheduleStudentAdapter
import com.swj.academymanagement.databinding.ActivityCourseScheduleBinding
import com.swj.academymanagement.model.CourseSchedule
import com.swj.academymanagement.model.CourseScheduleTeacher
import com.swj.academymanagement.model.Week
import com.swj.academymanagement.model.WeekDay
import com.swj.academymanagement.network.RetrofitCourseScheduleService
import com.swj.academymanagement.network.RetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 선생님 / 학생 권한 수업 시간표 조회 화면
class CourseScheduleActivity : AppCompatActivity() {

    val binding:ActivityCourseScheduleBinding by lazy { ActivityCourseScheduleBinding.inflate(layoutInflater) }

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

        if(G.member.authority == "teacher") {
            // 선생님 권한으로 로그인한 경우 보여줄 강좌 시간표 리스트
            RetrofitHelper.getRetrofitInstance().create(RetrofitCourseScheduleService::class.java)
                .courseScheduleList(
                    G.member.authority,     // 선생님 권한 -> "teacher"
                    G.member.id             // 선생님 아이디
                ).enqueue(object : Callback<MutableList<CourseScheduleTeacher>> {
                    override fun onResponse(
                        call: Call<MutableList<CourseScheduleTeacher>>,
                        response: Response<MutableList<CourseScheduleTeacher>>
                    ) {
                        val courseScheduleArr = response.body()

                        if(courseScheduleArr != null)
                            binding.recycler.adapter = CourseScheduleAdapter(this@CourseScheduleActivity, courseScheduleArr)
                    }

                    override fun onFailure(
                        call: Call<MutableList<CourseScheduleTeacher>>,
                        t: Throwable
                    ) {
                        AlertDialog.Builder(this@CourseScheduleActivity)
                            .setMessage("error : ${t.message}")
                            .setPositiveButton("OK", null).show()
                    }
                })
        } else {
            // 학생 권한으로 로그인 한 경우 보여줄 강좌 시간표 리스트
            val week:Week = WeekDay.getWeekDate()
            val courseScheduleArr:MutableList<CourseSchedule> = mutableListOf()
            courseScheduleArr.add(CourseSchedule(week.monday.date, week.monday.day, "국어", "1", "101호"))
            courseScheduleArr.add(CourseSchedule(week.tuesday.date, week.tuesday.day, "영어", "1", "103호"))
            courseScheduleArr.add(CourseSchedule())
            courseScheduleArr.add(CourseSchedule(week.thursday.date, week.thursday.day, "국어", "1", "101호"))
            courseScheduleArr.add(CourseSchedule(week.friday.date, week.friday.day, "영어", "1", "101호"))

            courseScheduleArr.add(CourseSchedule(week.monday.date, week.monday.day, "영어", "2", "103호"))
            courseScheduleArr.add(CourseSchedule())
            courseScheduleArr.add(CourseSchedule(week.wednesday.date, week.wednesday.day, "국어", "2", "101호"))
            courseScheduleArr.add(CourseSchedule())
            courseScheduleArr.add(CourseSchedule())

            courseScheduleArr.add(CourseSchedule())
            courseScheduleArr.add(CourseSchedule(week.tuesday.date, week.tuesday.day, "국어", "3", "101호"))
            courseScheduleArr.add(CourseSchedule(week.wednesday.date, week.wednesday.day, "영어", "3", "103호"))
            courseScheduleArr.add(CourseSchedule(week.thursday.date, week.thursday.day, "영어", "3", "103호"))
            courseScheduleArr.add(CourseSchedule(week.friday.date, week.friday.day, "국어", "3", "101호"))

            binding.recycler.adapter = CourseScheduleStudentAdapter(this, courseScheduleArr)
        }
    }
}