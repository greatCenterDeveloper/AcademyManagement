package com.swj.academymanagement.activities

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.swj.academymanagement.databinding.ActivityAcademySignupBinding
import com.swj.academymanagement.model.Member
import com.swj.academymanagement.network.RetrofitHelper
import com.swj.academymanagement.network.RetrofitMemberService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

// 학원 계정 회원 가입 화면
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
    var profile:Uri? = null
    // 아이디
    var id:String = ""
    // 휴대폰 번호
    var call = ""

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

        // 뒤로가기 버튼
        binding.btnBack.setOnClickListener { finish() }
        //binding.btnCancel.setOnClickListener {  }

        // 회원가입 버튼
        binding.btnSighup.setOnClickListener { signUp() }

        // 프로필 이미지 선택
        binding.btnProfileSelect.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).setType("image/*")
            imagePickResultLauncher.launch(intent)
        }


        // 아이디 중복 체크
        binding.btnIdCheck.setOnClickListener{
            id = binding.tietId.text.toString()
            if(id == "") {
                AlertDialog.Builder(this).setMessage("아이디를 입력하세요.").setPositiveButton("OK", null).show()
                idCheck = false
            } else {
                // 중복된 아이디 있는지 확인
                RetrofitHelper.getRetrofitInstance().create(RetrofitMemberService::class.java)
                    .memberSignUpIdCheck(id).enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            val result = response.body()
                            AlertDialog.Builder(this@AcademySignupActivity)
                                .setMessage(result)
                                .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                                    idCheck = if(result!!.contains("가능")) true
                                              else {
                                                  binding.tietId.requestFocus()
                                                  binding.tietId.setText("")
                                                  false
                                              }
                                }).show()
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Toast.makeText(this@AcademySignupActivity, "error : ${t.message}", Toast.LENGTH_SHORT).show()
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
                AlertDialog.Builder(this)
                    .setMessage("휴대폰 번호를 입력하세요.")
                    .setPositiveButton("OK", null).show()
                callCheck = false
            }  else {
                // 중복된 휴대폰 번호가 있는지 확인 (member 테이블 unique 키)
                call = "${call1}-${call2}-${call3}"
                RetrofitHelper.getRetrofitInstance().create(RetrofitMemberService::class.java)
                    .memberSignUpCallNumberCheck(call).enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            val result = response.body()
                            AlertDialog.Builder(this@AcademySignupActivity)
                                .setMessage(result)
                                .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                                    if(result!!.contains("가능")) callCheck = true
                                    else {
                                        binding.tilCall1.editText?.requestFocus()

                                        binding.tilCall1.editText?.setText("")
                                        binding.tilCall2.editText?.setText("")
                                        binding.tilCall3.editText?.setText("")

                                        callCheck = false
                                    }
                                }).show()
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Toast.makeText(this@AcademySignupActivity, "error : ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })

            }
        }
    }

    // 프로필 이미지 사진 선택 객체
    private val imagePickResultLauncher:ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                if(it.resultCode != RESULT_CANCELED) {
                    if(it.data != null) {
                        val intent:Intent = it.data!!
                        profile = intent.data
                        Glide.with(this).load(profile).into(binding.ivProfile)
                    } else {
                        Toast.makeText(this, "이미지가 선택되지 않았습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )

    // 바깥 화면 터치 시 소프트 키보드 숨기기
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

        // 비밀번호
        var password:String = binding.tilPassword.editText!!.text.toString()

        // 비밀번호 확인과 일치하는지?
        passwordCheck = binding.tilPasswordCheck.editText!!.text.toString() == password

        // 이름이 비어있지 않다면 name 변수에 이름 값 넣기
        var name:String = ""
        if (!binding.tilName.editText!!.text.toString().equals(""))
            name = binding.tilName.editText!!.text.toString()

        // 강좌 리스트에 강좌들 넣기
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

            // 프로필 이미지 이름
            var imgFile:String = ""

            // 프로필 사진을 선택했을 경우 FirebaseStorage에 저장
            if(profile != null) {
                // 현재 시간 가져오기
                val sdf = SimpleDateFormat("yyyyMMddHHmmss")
                sdf.timeZone = TimeZone.getTimeZone("Asia/Seoul")

                // 현재 시간 가져와서 메세지에 첨부한 이미지 파일의 이름으로 만들기
                val name = "IMG_${sdf.format(Date())}"

                // FirebaseStorage 이미지 파일 저장
                val storage = FirebaseStorage.getInstance()
                imgFile = "${name}.jpg"
                val imgRef: StorageReference = storage.getReference("profileImage/$imgFile")
                imgRef.putFile(profile!!)
            }


            // member 객체에 회원 가입하려고 입력한 내용들 주입
            val member = Member(authority, imgFile, id, password, name, courseArr, call)

            // 가입 처리 Retrofit
            val retrofit = RetrofitHelper.getRetrofitInstance()
            retrofit.create(RetrofitMemberService::class.java).memberSignUp(member).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val result = response.body()

                    Toast.makeText(this@AcademySignupActivity, result, Toast.LENGTH_SHORT).show()

                    if(result?.contains("성공") == true) { // 넘어오는 문자열 : 회원 가입 성공 이므로..
                        // 가입 완료 후 다시 로그인 화면으로...
                        startActivity(Intent(this@AcademySignupActivity, AcademyLoginActivity::class.java))
                        finish()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(this@AcademySignupActivity, "error : ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            if(!authCheck)          //  권한을 체크하지 않았다면..
                AlertDialog.Builder(this).setMessage("학생, 선생님 체크해 주세요.").setPositiveButton("OK", null).show()
            else if(!idCheck)       // 아이디 중복 확인을 하지 않았다면..
                AlertDialog.Builder(this).setMessage("아이디 중복 확인을 해주세요.").setPositiveButton("OK", null).show()
            else if(!passwordCheck) // 비밀번호가 서로 일치하지 않는다면..
                AlertDialog.Builder(this).setMessage("비밀번호가 일치하지 않습니다.").setPositiveButton("OK", null).show()
            else if(!callCheck)     // 휴대폰 번호 중복 확인을 하지 않았다면..
                AlertDialog.Builder(this).setMessage("휴대폰 번호 중복 확인을 해주세요.").setPositiveButton("OK", null).show()
            else                    // 그 밖에 입력하지 않은 정보가 있다면.. ( 프로필 사진 제외 )
                AlertDialog.Builder(this).setMessage("미 입력된 회원 정보가 있습니다.").setPositiveButton("OK", null).show()
        }
    }
}