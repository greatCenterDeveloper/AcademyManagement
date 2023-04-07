package com.swj.academymanagement.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import com.swj.academymanagement.adapters.CourseScheduleAdapter
import com.swj.academymanagement.adapters.CourseScheduleStudentAdapter
import com.swj.academymanagement.databinding.ActivityCourseScheduleBinding
import com.swj.academymanagement.model.CourseSchedule
import com.swj.academymanagement.model.CourseScheduleTeacher
import com.swj.academymanagement.model.Member
import com.swj.academymanagement.model.Week
import com.swj.academymanagement.model.WeekDay
import com.swj.academymanagement.network.RetrofitCourseScheduleService
import com.swj.academymanagement.network.RetrofitHelper
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CourseScheduleActivity : AppCompatActivity() {

    val binding:ActivityCourseScheduleBinding by lazy { ActivityCourseScheduleBinding.inflate(layoutInflater) }

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

        val gson = Gson()

        val member:Member =
        if(gson.fromJson(intent.getStringExtra("teacher"), Member::class.java) != null)
            gson.fromJson(intent.getStringExtra("teacher"), Member::class.java)
        else gson.fromJson(intent.getStringExtra("student"), Member::class.java)


        if(member.authority == "teacher") {
            /*val courseArr = mutableListOf<String>()
            courseArr.add("국어")
            courseArr.add("수학")

            val studentArr:MutableList<Member> = mutableListOf()
            val studentZeroArr:MutableList<Member> = mutableListOf()
            studentArr.add(Member("student", "", "aaa@aaa.com", "aaa", "sam", courseArr, "010-1234-5678"))
            studentArr.add(Member("student", "", "bbb@bbb.com", "aaa", "robin", courseArr, "010-1111-2222"))
            studentArr.add(Member("student", "", "ccc@ccc.com", "aaa", "hong", courseArr, "010-5678-5678"))
            studentArr.add(Member("student", "", "fsd@ddf.com", "aaa", "kim", courseArr, "010-2234-4328"))
            studentArr.add(Member("student", "", "few@fes.com", "aaa", "rosa", courseArr, "010-6856-6578"))
            studentArr.add(Member("student", "", "gre@tff.com", "aaa", "wang", courseArr, "010-6205-5681"))
            studentArr.add(Member("student", "", "rgs@aaa.com", "aaa", "bin", courseArr, "010-5854-3564"))
            studentArr.add(Member("student", "", "hhg@eee.com", "aaa", "kong", courseArr, "010-1357-5987"))
            studentArr.add(Member("student", "", "yed@eee.com", "aaa", "song", courseArr, "010-2687-2585"))
            studentArr.add(Member("student", "", "wqe@ddd.com", "aaa", "tom", courseArr, "010-4675-2655"))

            val week:Week = WeekDay.getWeekDate()

            val courseScheduleArr:MutableList<CourseScheduleTeacher> = mutableListOf()
            courseScheduleArr.add(CourseScheduleTeacher(week.monday.date, week.monday.day, "국어", "1", "101호", studentArr))
            courseScheduleArr.add(CourseScheduleTeacher(week.tuesday.date, week.tuesday.day, "영어", "1", "103호", studentArr))
            courseScheduleArr.add(CourseScheduleTeacher(week.wednesday.date, week.wednesday.day, "수학", "1", "102호", studentZeroArr))
            courseScheduleArr.add(CourseScheduleTeacher(week.thursday.date, week.thursday.day, "국어", "1", "101호", studentArr))
            courseScheduleArr.add(CourseScheduleTeacher(week.friday.date, week.friday.day, "영어", "1", "103호", studentZeroArr))

            courseScheduleArr.add(CourseScheduleTeacher(week.monday.date, week.monday.day, "영어", "2", "103호", studentArr))
            courseScheduleArr.add(CourseScheduleTeacher(week.tuesday.date, week.tuesday.day, "수학", "2", "102호", studentZeroArr))
            courseScheduleArr.add(CourseScheduleTeacher(week.wednesday.date, week.wednesday.day, "국어", "2", "101호", studentZeroArr))
            courseScheduleArr.add(CourseScheduleTeacher(week.thursday.date, week.thursday.day, "수학", "2", "102호", studentZeroArr))
            courseScheduleArr.add(CourseScheduleTeacher(week.friday.date, week.friday.day, "수학", "2", "102호", studentArr))

            courseScheduleArr.add(CourseScheduleTeacher(week.monday.date, week.monday.day, "수학", "3", "102호", studentZeroArr))
            courseScheduleArr.add(CourseScheduleTeacher(week.tuesday.date, week.tuesday.day, "국어", "3", "101호", studentZeroArr))
            courseScheduleArr.add(CourseScheduleTeacher(week.wednesday.date, week.wednesday.day, "영어", "3", "103호", studentArr))
            courseScheduleArr.add(CourseScheduleTeacher(week.thursday.date, week.thursday.day, "영어", "3", "103호", studentArr))
            courseScheduleArr.add(CourseScheduleTeacher(week.friday.date, week.friday.day, "국어", "3", "101호", studentArr))

            binding.recycler.adapter = CourseScheduleAdapter(this, courseScheduleArr)*/

            RetrofitHelper.getRetrofitInstance().create(RetrofitCourseScheduleService::class.java)
                .courseScheduleList().enqueue(object : Callback<String> {
                    override fun onResponse(
                        call: Call<String>,
                        response: Response<String>
                    ) {
                        val courseScheduleJsonArr = response.body()
                        val courseScheduleArr:MutableList<CourseScheduleTeacher> = mutableListOf()

                        val jsonArray:JSONArray = JSONArray(courseScheduleJsonArr)

                        //Log.i("dateeeeeeeeeeeeeeeeeeeeeeeee", jsonArray.getJSONObject(0).getString("date"))


                        for(i in 0 until jsonArray.length()) {
                            val jo = jsonArray.getJSONObject(i)
                            val cst:CourseScheduleTeacher = CourseScheduleTeacher(
                                jo.getString("date"),
                                jo.getString("day"),
                                jo.getString("course"),
                                jo.getString("period"),
                                jo.getString("room")
                            )


                            Log.i("studenttttttttttttttttttttttttt", jo.getJSONArray("studentArr").toString())

                            val studentJsonArray:JSONArray = JSONArray(jo.getJSONArray("studentArr"))
                            val students:MutableList<Member> = mutableListOf()
                            for(j in 0 until studentJsonArray.length()) {
                                val studentJo = studentJsonArray.getJSONObject(j)

                                val student:Member = Member(
                                    authority= studentJo.getString("authority"),
                                    profile= studentJo.getString("profile"),
                                    id= studentJo.getString("id"),
                                    password= studentJo.getString("password"),
                                    name= studentJo.getString("name"),
                                    call_number= studentJo.getString("call_number")
                                )

                                val courseJsonArray:JSONArray = JSONArray(studentJo.getJSONArray("courseArr"))
                                val courseArr:MutableList<String> = mutableListOf()
                                for(k in 0 until courseJsonArray.length()) {
                                    val courseJo = courseJsonArray.getJSONObject(k)
                                    courseArr.add(courseJo.toString())
                                }
                                student.courseArr = courseArr
                                students.add(student)
                            }
                            cst.students = students
                            courseScheduleArr.add(cst)
                        }

                        /*AlertDialog.Builder(this@CourseScheduleActivity)
                            .setMessage("message : ${jsonArray[0]}")
                            .setPositiveButton("OK", null).show()*/

                        if(courseScheduleArr != null)
                            binding.recycler.adapter = CourseScheduleAdapter(this@CourseScheduleActivity, courseScheduleArr)
                    }

                    override fun onFailure(
                        call: Call<String>,
                        t: Throwable
                    ) {
                        AlertDialog.Builder(this@CourseScheduleActivity)
                            .setMessage("error : ${t.message}")
                            .setPositiveButton("OK", null).show()
                    }
                })
        } else {
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