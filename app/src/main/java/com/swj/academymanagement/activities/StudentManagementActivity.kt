package com.swj.academymanagement.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import com.swj.academymanagement.G
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
    //val studentArr:MutableList<Member>? = null
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

        //val teacher = Gson().fromJson(intent.getStringExtra("teacher"), Member::class.java)

        val courseAdapter:ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, G.member.courseArr)
        binding.acTvCourse.setAdapter(courseAdapter)

        binding.btnSearch.setOnClickListener {
            val name = binding.tilName.editText?.text.toString()
            RetrofitHelper.getRetrofitInstance().create(RetrofitStudentManagementService::class.java)
                .studentNameSearch(G.member.id, name).enqueue(object : Callback<MutableList<Member>> {
                    override fun onResponse(
                        call: Call<MutableList<Member>>,
                        response: Response<MutableList<Member>>
                    ) {
                        val studentArr = response.body()
                        if(studentArr != null)
                            binding.recycler.adapter =
                                StudentManagementAdapter(this@StudentManagementActivity, studentArr, G.member.id)
                    }

                    override fun onFailure(call: Call<MutableList<Member>>, t: Throwable) {
                        AlertDialog.Builder(this@StudentManagementActivity)
                            .setMessage("error : ${t.message}")
                            .setPositiveButton("OK", null).show()
                    }
                })

            binding.tilName.editText?.setText("")
        }

        binding.acTvCourse.setOnItemClickListener { _, _, i, _ ->
            RetrofitHelper.getRetrofitInstance().create(RetrofitStudentManagementService::class.java)
                .studentCourseSearch(G.member.id, G.member.courseArr[i]).enqueue(object :Callback<MutableList<Member>> {
                    override fun onResponse(
                        call: Call<MutableList<Member>>,
                        response: Response<MutableList<Member>>
                    ) {
                        val studentArr = response.body()
                        if(studentArr != null)
                            binding.recycler.adapter =
                                StudentManagementAdapter(this@StudentManagementActivity, studentArr, G.member.id)
                    }

                    override fun onFailure(call: Call<MutableList<Member>>, t: Throwable) {
                        AlertDialog.Builder(this@StudentManagementActivity)
                            .setMessage("error : ${t.message}")
                            .setPositiveButton("OK", null).show()
                    }
                })
        }

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
            .studentList(G.member.id).enqueue(object : Callback<MutableList<Member>> {
                override fun onResponse(
                    call: Call<MutableList<Member>>,
                    response: Response<MutableList<Member>>
                ) {
                    val studentArr = response.body()
                    if(studentArr != null)
                        binding.recycler.adapter =
                            StudentManagementAdapter(this@StudentManagementActivity, studentArr, G.member.id)
                }

                override fun onFailure(call: Call<MutableList<Member>>, t: Throwable) {
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