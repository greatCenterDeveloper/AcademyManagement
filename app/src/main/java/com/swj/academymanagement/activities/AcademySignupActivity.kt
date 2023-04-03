package com.swj.academymanagement.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.gson.Gson
import com.swj.academymanagement.databinding.ActivityAcademySignupBinding
import com.swj.academymanagement.model.Member

class AcademySignupActivity : AppCompatActivity() {

    val binding:ActivityAcademySignupBinding by lazy { ActivityAcademySignupBinding.inflate(layoutInflater) }
    // 학생, 선생님 둘 중 하나 선택했는지 확인..
    var authCheck = false
    // 아이디 중복검사 했는지 확인..
    var emailIdCheck = false
    // 비밀번호가 서로 일치하는지 확인..
    var passwordCheck = false
    // 핸드폰 중복검사 했는지 확인..
    var callCheck = false
    // 사진 경로
    var profile:String = ""

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

        binding.btnSighup.setOnClickListener { signUp() }
        binding.btnProfileSelect.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*")
            imagePickResultLauncher.launch(intent)
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

    // 가입 버튼 클릭
    private fun signUp() {
        // 권한 가져오기 ( 학생, 선생님 )
        var authority = ""

        // 학생이나 선생님 중 둘 중 하나라도 선택을 했다면..
        if (binding.rbStudent.isChecked || binding.rbTeacher.isChecked) {
            authority = findViewById<MaterialRadioButton>(binding.rgAuthority.checkedRadioButtonId).text.toString()
            authCheck = true
        }

        // 사진 경로
        //val profile = ""

        // 아이디
        var emailId:String = binding.tilEmailId.editText!!.text.toString()

        // 아이디 중복 체크
        binding.btnIdCheck.setOnClickListener{
            if(emailId.equals("")) {
                AlertDialog.Builder(this).setMessage("아이디를 입력하세요.").setPositiveButton("OK", null).show()
                emailIdCheck = false
            } else {
                // 중복된 아이디가 없는지 Retrofit
            }
        }

        // 비밀번호
        var password:String = binding.tilPassword.editText!!.text.toString()

        // 비밀번호 확인과 일치하는지?
        passwordCheck = binding.tilPasswordCheck.editText!!.text.toString().equals(password)

        var name:String = ""
        if (!binding.tilName.editText!!.text.toString().equals(""))
            name = binding.tilName.editText!!.text.toString()

        val courseArr:MutableList<String> = mutableListOf()
        if (binding.cbKor.isChecked) courseArr.add(binding.cbKor.text.toString())
        if (binding.cbEng.isChecked) courseArr.add(binding.cbEng.text.toString())
        if (binding.cbMath.isChecked) courseArr.add(binding.cbMath.text.toString())

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

        var call = ""
        binding.btnCallCheck.setOnClickListener {
            if (call1.equals("") || call2.equals("") || call3.equals("")) {
                AlertDialog.Builder(this).setMessage("휴대폰 번호를 입력하세요.").setPositiveButton("OK", null).show()
                callCheck = false
            }  else {
                // 중복된 휴대폰 번호가 있는지 Retrofit

                call = "${call1}-${call2}-${call3}"
            }
        }

        // 권한 선택함
        // 아이디가 중복되지 않음
        // 비밀번호 / 비밀번호 확인이 서로 일치
        // 이름 입력함
        // 강좌 선택함
        // 휴대폰 번호 중복되지 않을 경우 가입 승인
        if(authCheck && emailIdCheck && passwordCheck && !name.equals("") && courseArr.size != 0 && !call.equals("")) {
            // 가입 처리 Retrofit
            val jsonMember:String = Gson().toJson(Member(authority, profile, emailId, password, name, courseArr, call))

            // 가입 완료 후 다시 로그인 화면으로...
            //finish()
        }
    }
}