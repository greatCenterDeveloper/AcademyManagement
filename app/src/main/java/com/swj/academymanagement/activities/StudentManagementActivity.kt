package com.swj.academymanagement.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ArrayAdapter
import com.swj.academymanagement.R
import com.swj.academymanagement.adapters.StudentManagementAdapter
import com.swj.academymanagement.databinding.ActivityStudentManagementBinding
import com.swj.academymanagement.model.Member

class StudentManagementActivity : AppCompatActivity() {
    val binding:ActivityStudentManagementBinding by lazy { ActivityStudentManagementBinding.inflate(layoutInflater) }
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

        lateinit var teacher: Member
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            teacher = intent.getSerializableExtra("teacher", Member::class.java)!!
        else
            teacher = intent.getSerializableExtra("teacher") as Member

        val courseAdapter:ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, teacher.courseArr)
        binding.acTvCourse.setAdapter(courseAdapter)

        val courseArr = mutableListOf<String>()
        courseArr.add("국어")
        courseArr.add("수학")

        val studentArr:MutableList<Member> = mutableListOf()
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

        binding.recycler.adapter = StudentManagementAdapter(this, studentArr)
    }
}