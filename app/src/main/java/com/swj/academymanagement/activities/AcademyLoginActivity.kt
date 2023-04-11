package com.swj.academymanagement.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.swj.academymanagement.G
import com.swj.academymanagement.R
import com.swj.academymanagement.databinding.ActivityAcademyLoginBinding
import com.swj.academymanagement.databinding.DialogFindIdBinding
import com.swj.academymanagement.databinding.DialogFindPasswordBinding
import com.swj.academymanagement.model.Member
import com.swj.academymanagement.network.RetrofitHelper
import com.swj.academymanagement.network.RetrofitMemberService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 학원 계정 로그인 화면
class AcademyLoginActivity : AppCompatActivity() {

    val binding:ActivityAcademyLoginBinding by lazy { ActivityAcademyLoginBinding.inflate(layoutInflater) }

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

        binding.toolbar.setNavigationIcon(R.drawable.ic_action_backspace)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 학원 계정으로 회원가입
        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, AcademySignupActivity::class.java))
            finish()
        }

        // 아이디 찾기
        binding.btnFindId.setOnClickListener {
            val dialogBinding = DialogFindIdBinding.inflate(layoutInflater)
            val dialog: AlertDialog = AlertDialog.Builder(this)
                .setView(dialogBinding.root)
                .create()
            dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }
            dialogBinding.ivClose.setOnClickListener { dialog.dismiss() }
            dialog.show()

            // 찾기 버튼 클릭
            dialogBinding.btnFind.setOnClickListener {
                val call:String = dialogBinding.tilInputCall.editText?.text.toString()
                Toast.makeText(this, "휴대폰 번호 : ${call}", Toast.LENGTH_SHORT).show()
            }
        }

        // 비밀번호 찾기
        binding.btnFindPassword.setOnClickListener {
            val dialogBinding = DialogFindPasswordBinding.inflate(layoutInflater)
            val dialog: AlertDialog = AlertDialog.Builder(this)
                .setView(dialogBinding.root)
                .create()
            dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }
            dialogBinding.ivClose.setOnClickListener { dialog.dismiss() }
            dialog.show()

            // 찾기 버튼 클릭
            dialogBinding.btnFind.setOnClickListener {
                val id:String = dialogBinding.tilInputId.editText?.text.toString()
                val call:String = dialogBinding.tilInputCall.editText?.text.toString()
                Toast.makeText(this, "아이디 : ${id}\n휴대폰 번호 : ${call}", Toast.LENGTH_SHORT).show()
            }
        }

        // 로그인
        binding.btnLogin.setOnClickListener {
            val id:String = binding.tilId.editText?.text.toString()
            val password:String = binding.tilPassword.editText?.text.toString()

            // 로그인 처리 retrofit
            RetrofitHelper.getRetrofitInstance().create(RetrofitMemberService::class.java)
                .memberLogin(id, password).enqueue(object : Callback<Member> {
                    override fun onResponse(call: Call<Member>, response: Response<Member>) {
                        val result = response.body()
                        // 디버깅용 코드
                        /*AlertDialog.Builder(this@AcademyLoginActivity)
                            .setMessage("message : ${result?.authority}")
                            .setPositiveButton("OK", null).show()*/
                        when(result?.authority) {
                            "teacher" -> {  // 선생님 로그인
                                // G 클래스의 companion object에 선생님 객체 주입
                                G.member = result

                                // 선생님 강의 강좌 리스트의 첫 번째에 추가할 내용
                                G.member.courseArr.add(0, "선택안함")
                                startActivity(Intent(this@AcademyLoginActivity, MainActivity::class.java))
                                finish()
                            }
                            "student" -> {  // 학생 로그인
                                // G 클래스의 companion object에 학생 객체 주입
                                G.member = result

                                // 학생 수강 신청 강좌 리스트의 첫 번째에 추가할 내용
                                G.member.courseArr.add(0, "선택안함")
                                startActivity(Intent(this@AcademyLoginActivity, StudentActivity::class.java))
                                finish()
                            }
                        }
                    }
                    override fun onFailure(call: Call<Member>, t: Throwable) {
                        // 로그인 실패 시 에러 발생하므로..
                        AlertDialog.Builder(this@AcademyLoginActivity)
                            .setMessage("아이디나 비밀번호가 맞지 않습니다.")
                            .setPositiveButton("OK", null).show()
                    }
                })
        }
    }

    // 뒤로 가기
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    // 바깥 화면 터치 시 소프트 키보드 숨기기
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if(binding.tilId.editText?.text.toString() != "" && binding.tilPassword.editText?.text.toString() != "") {
            val imm:InputMethodManager = getSystemService(InputMethodManager::class.java)
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            currentFocus?.clearFocus()
        }
        return super.dispatchTouchEvent(ev)
    }
}