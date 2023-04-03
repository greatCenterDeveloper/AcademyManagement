package com.swj.academymanagement.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.swj.academymanagement.adapters.CourseScheduleAdapter
import com.swj.academymanagement.databinding.ActivityCourseScheduleBinding
import com.swj.academymanagement.model.CourseScheduleTeacher
import com.swj.academymanagement.model.Member
import com.swj.academymanagement.model.Week
import com.swj.academymanagement.model.WeekDay

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

        val courseArr = mutableListOf<String>()
        courseArr.add("국어")
        courseArr.add("수학")

        val studentArr:MutableList<Member> = mutableListOf()
        val studentZeroArr:MutableList<Member> = mutableListOf()
        studentArr.add(Member("학생", "", "aaa@aaa.com", "aaa", "sam", courseArr, "010-1234-5678"))
        studentArr.add(Member("학생", "", "bbb@bbb.com", "aaa", "robin", courseArr, "010-1111-2222"))
        studentArr.add(Member("학생", "", "ccc@ccc.com", "aaa", "hong", courseArr, "010-5678-5678"))
        studentArr.add(Member("학생", "", "fsd@ddf.com", "aaa", "kim", courseArr, "010-2234-4328"))
        studentArr.add(Member("학생", "", "few@fes.com", "aaa", "rosa", courseArr, "010-6856-6578"))
        studentArr.add(Member("학생", "", "gre@tff.com", "aaa", "wang", courseArr, "010-6205-5681"))
        studentArr.add(Member("학생", "", "rgs@aaa.com", "aaa", "bin", courseArr, "010-5854-3564"))
        studentArr.add(Member("학생", "", "hhg@eee.com", "aaa", "kong", courseArr, "010-1357-5987"))
        studentArr.add(Member("학생", "", "yed@eee.com", "aaa", "song", courseArr, "010-2687-2585"))
        studentArr.add(Member("학생", "", "wqe@ddd.com", "aaa", "tom", courseArr, "010-4675-2655"))

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

        binding.recycler.adapter = CourseScheduleAdapter(this, courseScheduleArr)
    }
}