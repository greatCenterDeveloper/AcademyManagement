package com.swj.academymanagement.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import com.swj.academymanagement.adapters.StudentManagementAdapter
import com.swj.academymanagement.databinding.ActivityStudentManagementBinding
import com.swj.academymanagement.model.Member
import com.swj.academymanagement.network.RetrofitHelper
import com.swj.academymanagement.network.RetrofitStudentManagementService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        binding.ivBackspace.setOnClickListener { finish() }

        val teacher = Gson().fromJson(intent.getStringExtra("teacher"), Member::class.java)

        val courseAdapter:ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, teacher.courseArr)
        binding.acTvCourse.setAdapter(courseAdapter)

        /*val courseArr = mutableListOf<String>()
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

        binding.recycler.adapter = StudentManagementAdapter(this, studentArr)*/
        RetrofitHelper.getRetrofitInstance().create(RetrofitStudentManagementService::class.java)
            .studentManagementList(teacher.id).enqueue(object : Callback<List<Member>> {
                override fun onResponse(
                    call: Call<List<Member>>,
                    response: Response<List<Member>>
                ) {
                    val studentArr = response.body()
                    binding.recycler.adapter = StudentManagementAdapter(this@StudentManagementActivity, studentArr!!)
                }

                override fun onFailure(call: Call<List<Member>>, t: Throwable) {
                    AlertDialog.Builder(this@StudentManagementActivity)
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