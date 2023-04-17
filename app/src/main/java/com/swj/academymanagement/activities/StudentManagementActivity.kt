package com.swj.academymanagement.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import com.swj.academymanagement.G
import com.swj.academymanagement.adapters.StudentManagementAdapter
import com.swj.academymanagement.databinding.ActivityStudentManagementBinding
import com.swj.academymanagement.model.Member
import com.swj.academymanagement.network.RetrofitHelper
import com.swj.academymanagement.network.RetrofitStudentManagementService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 선생님 권한 학생 관리의 선생님 강좌에 수강 중인 학생 리스트 조회 화면
class StudentManagementActivity : AppCompatActivity() {
    val binding:ActivityStudentManagementBinding by lazy { ActivityStudentManagementBinding.inflate(layoutInflater) }
    //val studentArr:MutableList<Member>? = null
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

        // 선생님 강좌 리스트 드롭다운 박스에 넣기
        val courseAdapter:ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, G.member.courseArr)
        binding.acTvCourse.setAdapter(courseAdapter)

        // 학생 이름 검색 ( 입력한 이름이 포함된 모든 학생 리스트 )
        binding.btnSearch.setOnClickListener {
            val name = binding.tilName.editText?.text.toString()
            RetrofitHelper.getRetrofitInstance().create(RetrofitStudentManagementService::class.java)
                .studentNameSearch(
                    G.member.id,    // 선생 아이디
                    name            // 학생 이름
                ).enqueue(object : Callback<MutableList<Member>> {
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
                        Toast.makeText(this@StudentManagementActivity, "error : ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })

            binding.tilName.editText?.setText("")
        }

        // 선생님 강좌 드롭다운 박스에서 특정 강좌 선택시 그 강좌에 수강 중인 학생 리스트
        binding.acTvCourse.setOnItemClickListener { _, _, i, _ ->
            RetrofitHelper.getRetrofitInstance().create(RetrofitStudentManagementService::class.java)
                .studentCourseSearch(
                    G.member.id,            // 선생 아이디
                    G.member.courseArr[i]   // 선생 강좌
                ).enqueue(object :Callback<MutableList<Member>> {
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
                        Toast.makeText(this@StudentManagementActivity, "error : ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        // 선생님 강좌에 수강 중인 모든 학생 리스트 조회
        RetrofitHelper.getRetrofitInstance().create(RetrofitStudentManagementService::class.java)
            .studentList(
                G.member.id     // 선생 아이디
            ).enqueue(object : Callback<MutableList<Member>> {
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
                    Toast.makeText(this@StudentManagementActivity, "error : ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // 바깥 화면 터치 시 소프트 키보드 숨기기
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(InputMethodManager::class.java)
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        currentFocus?.clearFocus()
        return super.dispatchTouchEvent(ev)
    }
}