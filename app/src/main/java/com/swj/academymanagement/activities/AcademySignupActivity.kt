package com.swj.academymanagement.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.android.material.radiobutton.MaterialRadioButton
import com.swj.academymanagement.databinding.ActivityAcademySignupBinding
import com.swj.academymanagement.model.Member
import com.swj.academymanagement.network.RetrofitHelper
import com.swj.academymanagement.network.RetrofitMemberService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AcademySignupActivity : AppCompatActivity() {

    val binding:ActivityAcademySignupBinding by lazy { ActivityAcademySignupBinding.inflate(layoutInflater) }
    // 학생, 선생님 둘 중 하나 선택했는지 확인..
    var authCheck = false
    // 아이디 중복검사 했는지 확인..
    var idCheck = false
    // 비밀번호가 서로 일치하는지 확인..
    var passwordCheck = false
    // 핸드폰 중복검사 했는지 확인..
    var callCheck = false
    // 사진 경로
    var profile:String = ""
    // 아이디
    var id:String = ""
    // 휴대폰 번호
    var call = ""

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

        binding.btnBack.setOnClickListener { finish() }
        //binding.btnCancel.setOnClickListener {  }
        binding.btnSighup.setOnClickListener { signUp() }
        binding.btnProfileSelect.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*")
            imagePickResultLauncher.launch(intent)
        }


        // 아이디 중복 체크
        binding.btnIdCheck.setOnClickListener{
            id = binding.tietId.text.toString()
            if(id == "") {
                AlertDialog.Builder(this).setMessage("아이디를 입력하세요.").setPositiveButton("OK", null).show()
                idCheck = false
            } else {
                // 중복된 아이디가 없는지 Retrofit
                RetrofitHelper.getRetrofitInstance().create(RetrofitMemberService::class.java)
                    .memberSignUpIdCheck(id).enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            val result = response.body()
                            AlertDialog.Builder(this@AcademySignupActivity)
                                .setMessage(result)
                                .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                                    if(result!!.contains("가능")) idCheck = true
                                    else binding.tietId.requestFocus()
                                }).show()
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            AlertDialog.Builder(this@AcademySignupActivity)
                                .setMessage("error : ${t.message}")
                                .setPositiveButton("OK", null).show()
                        }
                    })
            }
        }

        // 휴대폰 번호 중복 체크
        binding.btnCallCheck.setOnClickListener {
            var call1 = ""
            if (binding.tilCall1.editText!!.text.toString().length == 3) {
                call1 = binding.tilCall1.editText!!.text.toString()
            }

            var call2 = ""
            if (binding.tilCall2.editText!!.text.toString().length == 4) {
                call2 = binding.tilCall2.editText!!.text.toString()
            }

            var call3 = ""
            if (binding.tilCall3.editText!!.text.toString().length == 4) {
                call3 = binding.tilCall3.editText!!.text.toString()
            }

            if (call1.equals("") || call2.equals("") || call3.equals("")) {
                AlertDialog.Builder(this).setMessage("휴대폰 번호를 입력하세요.").setPositiveButton("OK", null).show()
                callCheck = false
            }  else {
                // 중복된 휴대폰 번호가 있는지 Retrofit
                call = "${call1}-${call2}-${call3}"
                RetrofitHelper.getRetrofitInstance().create(RetrofitMemberService::class.java)
                    .memberSignUpCallNumberCheck(call).enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            val result = response.body()
                            AlertDialog.Builder(this@AcademySignupActivity)
                                .setMessage(result)
                                .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                                    if(result!!.contains("가능")) callCheck = true
                                    else binding.tilCall1.editText?.requestFocus()
                                }).show()
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            AlertDialog.Builder(this@AcademySignupActivity)
                                .setMessage(t.message)
                                .setPositiveButton("OK", null).show()
                        }
                    })

            }
        }
    }

    private val imagePickResultLauncher:ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                if(it.resultCode != RESULT_CANCELED) {
                    val intent:Intent = it.data!!
                    profile = intent.data.toString()
                    Glide.with(this).load(intent.data).into(binding.ivProfile)
                }
            }
        )

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        // 비밀번호 확인까지 입력하고 프로필 사진을 등록하는 경우도 있으므로 소프트 키보드 숨기기
        // 이름 입력하면 강좌 선택해야하므로 소프트 키보드 숨기기
        // 전화번호 전부 입력하면 가입 버튼 눌러야 하므로 소프트 키보드 숨기기
        if(binding.tilPasswordCheck.editText?.text.toString() != "" ||
           binding.tilName.editText?.text.toString() != "" ||
          (binding.tilCall1.editText?.text.toString() != "" &&
           binding.tilCall2.editText?.text.toString() != "" &&
           binding.tilCall3.editText?.text.toString() != "")) {
            val imm: InputMethodManager = getSystemService(InputMethodManager::class.java)
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            currentFocus?.clearFocus()
        }
        return super.dispatchTouchEvent(ev)
    }

    // 가입 버튼 클릭
    private fun signUp() {
        // 권한 가져오기 ( 학생, 선생님 )
        var authority = ""

        // 학생이나 선생님 중 둘 중 하나라도 선택을 했다면..
        if (binding.rbStudent.isChecked || binding.rbTeacher.isChecked) {
            val authorityTemp = findViewById<MaterialRadioButton>(binding.rgAuthority.checkedRadioButtonId).text.toString()
            authority = if(authorityTemp == "선생님") "teacher" else "student"
            authCheck = true
        }


        // 사진 경로
        //val profile = ""


        // 비밀번호
        var password:String = binding.tilPassword.editText!!.text.toString()

        // 비밀번호 확인과 일치하는지?
        passwordCheck = binding.tilPasswordCheck.editText!!.text.toString() == password

        var name:String = ""
        if (!binding.tilName.editText!!.text.toString().equals(""))
            name = binding.tilName.editText!!.text.toString()

        val courseArr:MutableList<String> = mutableListOf()
        if (binding.cbKor.isChecked) courseArr.add("kor")
        if (binding.cbEng.isChecked) courseArr.add("eng")
        if (binding.cbMath.isChecked) courseArr.add("math")

        // 권한 선택함
        // 아이디가 중복되지 않음
        // 비밀번호 / 비밀번호 확인이 서로 일치
        // 이름 입력함
        // 강좌 선택함
        // 휴대폰 번호 중복되지 않을 경우 가입 승인
        if(authCheck && idCheck && passwordCheck && callCheck && !name.equals("") && courseArr.size != 0) {
            // 가입 처리 Retrofit
            val member = Member(authority, profile, id, password, name, courseArr, call)

            val retrofit = RetrofitHelper.getRetrofitInstance()
            retrofit.create(RetrofitMemberService::class.java).memberSignUp(member).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val result = response.body()
                    AlertDialog.Builder(this@AcademySignupActivity)
                        .setMessage(result)
                        .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                            if(result?.contains("성공") == true) {
                                // 가입 완료 후 다시 로그인 화면으로...
                                startActivity(Intent(this@AcademySignupActivity, AcademyLoginActivity::class.java))
                                finish()
                            }
                        }).show()
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    AlertDialog.Builder(this@AcademySignupActivity)
                        .setMessage(t.message)
                        .setPositiveButton("OK", null).show()
                }
            })
        } else {
            if(!authCheck)
                AlertDialog.Builder(this).setMessage("학생, 선생님 체크해 주세요.").setPositiveButton("OK", null).show()
            else if(!idCheck)
                AlertDialog.Builder(this).setMessage("아이디 중복 확인을 해주세요.").setPositiveButton("OK", null).show()
            else if(!passwordCheck)
                AlertDialog.Builder(this).setMessage("비밀번호가 일치하지 않습니다.").setPositiveButton("OK", null).show()
            else if(!callCheck)
                AlertDialog.Builder(this).setMessage("휴대폰 번호 중복 확인을 해주세요.").setPositiveButton("OK", null).show()
            else
                AlertDialog.Builder(this).setMessage("미 입력된 회원 정보가 있습니다.").setPositiveButton("OK", null).show()
        }
    }
}