package com.swj.academymanagement.activities

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.swj.academymanagement.G
import com.swj.academymanagement.R
import com.swj.academymanagement.databinding.ActivityMainBinding
import com.swj.academymanagement.databinding.DialogMyInfoUpdateBinding
import com.swj.academymanagement.databinding.DialogPasswordUpdateBinding
import com.swj.academymanagement.model.Member
import com.swj.academymanagement.network.RetrofitHelper
import com.swj.academymanagement.network.RetrofitMemberService
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

// 선생님 권한 로그인 시 보여줄 초기 화면
class MainActivity : AppCompatActivity() {

    val binding:ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    // 내 정보 수정 좌측 드로우어 메뉴 토글
    lateinit var drawerToggle:ActionBarDrawerToggle

    // 내 정보 수정 좌측 드로우어 메뉴에서 프로필 이미지 변경 시 가져올 이미지 주소
    var profile:Uri? = null

    // 내 정보 수정 좌측 드로우어 메뉴의 프로필 이미지
    lateinit var civ:CircleImageView

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
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 내 정보 수정 좌측 드로우어 메뉴
        drawerToggle = ActionBarDrawerToggle(this,
            binding.drawerLayout, binding.toolbar, R.string.open, R.string.close)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        drawerToggle.syncState()
        binding.drawerLayout.addDrawerListener(drawerToggle)

        // 내 정보 수정 좌측 드로우어 메뉴에서 아이템 선택 시..
        binding.nav.setNavigationItemSelectedListener {
            if(it.itemId == R.id.menu_my_info_update) { // NavigationView 메뉴 내 정보 수정
                val dialogBinding = DialogMyInfoUpdateBinding.inflate(layoutInflater)
                val dialog: AlertDialog = AlertDialog.Builder(this)
                    .setView(dialogBinding.root)
                    .create()
                dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }
                dialogBinding.ivClose.setOnClickListener { dialog.dismiss() }
                dialog.show()

                // 휴대폰 번호 중복 확인 안 한걸로 초기화
                var callCheck = false

                // 휴대폰 번호 중복 확인 버튼
                dialogBinding.btnCallCheck.setOnClickListener {

                    var call1 = ""
                    if (dialogBinding.tilCall1.editText!!.text.toString().length == 3) {
                        call1 = dialogBinding.tilCall1.editText!!.text.toString()
                    }

                    var call2 = ""
                    if (dialogBinding.tilCall2.editText!!.text.toString().length == 4) {
                        call2 = dialogBinding.tilCall2.editText!!.text.toString()
                    }

                    var call3 = ""
                    if (dialogBinding.tilCall3.editText!!.text.toString().length == 4) {
                        call3 = dialogBinding.tilCall3.editText!!.text.toString()
                    }

                    if (call1.equals("") || call2.equals("") || call3.equals("")) {
                        AlertDialog.Builder(this)
                            .setMessage("휴대폰 번호를 입력하세요.")
                            .setPositiveButton("OK", null).show()
                        callCheck = false
                    }  else {

                        // 중복된 휴대폰 번호가 있는지 확인 (member 테이블 unique 키)
                        val call = "${call1}-${call2}-${call3}"
                        RetrofitHelper.getRetrofitInstance().create(RetrofitMemberService::class.java)
                            .memberSignUpCallNumberCheck(call).enqueue(object : Callback<String> {
                                override fun onResponse(call: Call<String>, response: Response<String>) {
                                    val result = response.body()
                                    AlertDialog.Builder(this@MainActivity)
                                        .setMessage(result)
                                        .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                                            if(result!!.contains("가능")) callCheck = true
                                            else {
                                                dialogBinding.tilCall1.editText?.requestFocus()

                                                dialogBinding.tilCall1.editText?.setText("")
                                                dialogBinding.tilCall2.editText?.setText("")
                                                dialogBinding.tilCall3.editText?.setText("")

                                                callCheck = false
                                            }
                                        }).show()
                                }

                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    Toast.makeText(this@MainActivity, "error : ${t.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                    }
                }


                // 내 정보 수정 버튼
                dialogBinding.btnUpdate.setOnClickListener {
                    val name = dialogBinding.tilName.editText?.text.toString()
                    val call1 = dialogBinding.tilCall1.editText?.text.toString()
                    val call2 = dialogBinding.tilCall2.editText?.text.toString()
                    val call3 = dialogBinding.tilCall3.editText?.text.toString()
                    val call = "${call1}-${call2}-${call3}"
                    val prevPassword = dialogBinding.tilPrevPassword.editText?.text.toString()
                    val password = dialogBinding.tilPassword.editText?.text.toString()
                    val passwordCheck = dialogBinding.tilPasswordCheck.editText?.text.toString()

                    if(password != passwordCheck) {
                        // 비밀번호와 비밀번호 확인이 서로 일치하지 않는 경우

                        Toast.makeText(this, "비밀번호와 비밀번호 확인이\n일치하지 않습니다.", Toast.LENGTH_SHORT).show()

                        dialogBinding.tilPassword.editText?.setText("")
                        dialogBinding.tilPasswordCheck.editText?.setText("")
                        dialogBinding.tilPassword.editText?.requestFocus()
                    } else if(!callCheck) {
                        // 휴대폰 번호 중복 확인을 하지 않은 경우
                        Toast.makeText(this, "휴대폰 번호 중복 확인을 안 하셨습니다.", Toast.LENGTH_SHORT).show()
                    } else {

                        // 내 정보 수정 retrofit
                        RetrofitHelper.getRetrofitInstance().create(RetrofitMemberService::class.java)
                            .updateMemberInfo(
                                G.member.id,        // 선생님 아이디
                                name,               // 변경할 이름
                                call,               // 변경할 휴대폰 번호
                                prevPassword,       // 이전 비밀번호
                                password            // 변경할 비밀번호
                            ).enqueue(object :Callback<String> {
                                override fun onResponse(
                                    call: Call<String>,
                                    response: Response<String>
                                ) {
                                    val message = response.body()

                                    // 이전 비밀번호가 일치한다면 "success" 문자열이 날아오니 내 정보 수정 처리됬으므로..
                                    if(message == "success") {
                                        Toast.makeText(this@MainActivity,
                                            "내 정보 수정 완료", Toast.LENGTH_SHORT).show()
                                        dialog.dismiss()
                                    } else {
                                        // 이전 비밀번호가 불일치 한다면...
                                        Toast.makeText(this@MainActivity,
                                            "이전 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()

                                        dialogBinding.tilPrevPassword.editText?.setText("")
                                        dialogBinding.tilPrevPassword.editText?.requestFocus()
                                    }
                                }

                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    Toast.makeText(this@MainActivity, "error : ${t.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                    }
                }
            } else if(it.itemId == R.id.menu_password_update) { // NavigationView 메뉴 비밀번호 수정
                val dialogBinding = DialogPasswordUpdateBinding.inflate(layoutInflater)
                val dialog: AlertDialog = AlertDialog.Builder(this)
                    .setView(dialogBinding.root)
                    .create()
                dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }
                dialogBinding.ivClose.setOnClickListener { dialog.dismiss() }
                dialog.show()

                // 비밀번호 수정 버튼
                dialogBinding.btnUpdate.setOnClickListener {
                    val prevPassword = dialogBinding.tilPrevPassword.editText?.text.toString()
                    val password = dialogBinding.tilPassword.editText?.text.toString()
                    val passwordCheck = dialogBinding.tilPasswordCheck.editText?.text.toString()

                    // 새로이 입력한 비밀번호와 비밀번호 확인이 서로 일치한다면..
                    if(password == passwordCheck) {

                        RetrofitHelper.getRetrofitInstance().create(RetrofitMemberService::class.java)
                            .updateMemberPassword(
                                prevPassword,       // 이전 비밀번호
                                password,           // 수정하려는 비밀번호
                                G.member.id         // 선생님 아이디
                            ).enqueue(object :Callback<String> {
                                override fun onResponse(
                                    call: Call<String>,
                                    response: Response<String>
                                ) {
                                    val message = response.body()

                                    // 이전 비밀번호가 일치한다면 "success" 문자열이 날아오니 비밀번호 수정 처리됬으므로..
                                    if(message == "success") {
                                        Toast.makeText(this@MainActivity,
                                            "비밀번호 수정 완료", Toast.LENGTH_SHORT).show()
                                        dialog.dismiss()
                                    } else {
                                        // 이전 비밀번호가 불일치 한다면...
                                        Toast.makeText(this@MainActivity,
                                            "이전 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()

                                        dialogBinding.tilPrevPassword.editText?.setText("")
                                        dialogBinding.tilPrevPassword.editText?.requestFocus()
                                    }
                                }

                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    Toast.makeText(this@MainActivity, "error : ${t.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                    } else {
                        Toast.makeText(this, "비밀번호와 비밀번호 확인이\n일치하지 않습니다.", Toast.LENGTH_SHORT).show()

                        dialogBinding.tilPassword.editText?.setText("")
                        dialogBinding.tilPasswordCheck.editText?.setText("")
                        dialogBinding.tilPassword.editText?.requestFocus()
                    }
                }
            } else if(it.itemId == R.id.menu_unregister) { // 회원 탈퇴 버튼 클릭 시

                AlertDialog.Builder(this@MainActivity)
                    .setMessage("정말로 탈퇴하시겠습니까?")
                    .setNegativeButton("NO", null)
                    .setPositiveButton("OK") { _, _ ->

                        // 회원 탈퇴 retrofit
                        RetrofitHelper.getRetrofitInstance().create(RetrofitMemberService::class.java)
                            .memberUnregister(
                                G.member.id     // 선생님 아이디
                            ).enqueue(object :Callback<String> {
                                override fun onResponse(call: Call<String>, response: Response<String>) {
                                    val messageResult = response.body()

                                    val messageArr = messageResult?.split(",")

                                    var toastMessage = ""

                                    if(messageArr != null) {
                                        // 프로필 이미지가 존재한다면..
                                        if(messageArr.size > 1) {
                                            // 파이어베이스에 저장된 프로필 이미지 이름
                                            val profileImage = messageArr[0]

                                            // FirebaseStorage에서 저장된 기존 프로필 이미지 삭제
                                            FirebaseStorage.getInstance().getReference()
                                                .child("profileImage/$profileImage").delete()

                                            toastMessage = messageArr[1]
                                        } else {
                                            // 프로필 이미지가 존재하지 않는다면..
                                            toastMessage = messageArr[0]
                                        }

                                        // G 클래스 member 객체에서 회원 정보 제거
                                        G.member = Member("","","","","", call_number = "")

                                        Toast.makeText(this@MainActivity, toastMessage, Toast.LENGTH_SHORT).show()

                                        val intent = Intent(this@MainActivity, AcademyLoginActivity::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        startActivity(intent)
                                    }
                                }

                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    Toast.makeText(this@MainActivity, "error : ${t.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                    }.show()
            }

            binding.drawerLayout.closeDrawer(binding.nav)
            false
        }

        // NavigationView 안에 있는 HeaderView 안에 있는 CircleImageView를 찾아오기
        val headerView = binding.nav.getHeaderView(0)
        civ = headerView.findViewById(R.id.civ_profile)

        // NavigationView 안에 있는 HeaderView 안에 있는 이름 설정
        val nameView:TextView = headerView.findViewById(R.id.tv_name)
        nameView.text = G.member.name

        // NavigationView 안에 있는 HeaderView 안에 있는 휴대폰 번호 설정
        val callView:TextView = headerView.findViewById(R.id.tv_call)
        callView.text = G.member.call_number


        // member 테이블의 Primary Key인 id로 FirebaseStorage에 저장된 profile 이미지 이름 가져오기
        RetrofitHelper.getRetrofitInstance().create(RetrofitMemberService::class.java)
            .getMemberProfile(
                G.member.id     // 선생님 아이디
            ).enqueue(object :Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {

                    // 디비에 저장된 프로필 사진 이름 가져오기
                    val profile = response.body()

                    // FirebaseStorage에서 불러온 사진 좌측 드로우어 메뉴의 프로필 이미지에 붙이기
                    val storage = FirebaseStorage.getInstance()
                    val imgRef: StorageReference = storage.getReference().child("profileImage/$profile")
                    imgRef.downloadUrl.addOnSuccessListener {
                        Glide.with(this@MainActivity).load(it).into(civ)
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {}
            })


        // 프로필 이미지 변경
        civ.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).setType("image/*")
            imagePickResultLauncher.launch(intent)
        }

        binding.tvTeacherName.text = "${G.member.name} 선생님 어서오세요."

        // 학생 관리
        binding.btnStudentManagement.setOnClickListener {
            startActivity(Intent(this, StudentManagementActivity::class.java))
        }
        // 수업 목록
        binding.btnClassDayList.setOnClickListener {
            startActivity(Intent(this, CourseScheduleActivity::class.java))
        }

        // 출결 현황
        binding.btnAttendance.setOnClickListener {
            startActivity(Intent(this, AttendanceActivity::class.java))
        }
        // 문자 보내기
        binding.btnSendMessage.setOnClickListener {
            startActivity(Intent(this, SmsSendActivity::class.java))
        }

        // 상담 현황
        binding.btnCounsel.setOnClickListener {
            startActivity(Intent(this, CounselActivity::class.java))
        }
        // 교재 검색
        binding.btnTeachingBook.setOnClickListener {
            startActivity(Intent(this, TeachingBookActivity::class.java))
        }

        // 선생님 노트
        binding.btnTeacherNote.setOnClickListener {
            startActivity(Intent(this, TeacherNoteActivity::class.java))
        }
    }

    // 내 정보 수정 좌측 드로우어 메뉴에서 프로필 이미지 선택 객체
    private val imagePickResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                if(it.resultCode != RESULT_CANCELED) {
                    if(it.data != null) {
                        val intent:Intent = it.data!!
                        profile = intent.data
                    } else {
                        Toast.makeText(this, "이미지가 선택되지 않았습니다.", Toast.LENGTH_SHORT).show()
                        return@ActivityResultCallback
                    }

                    // 디비 프로필 사진 이름 변경..
                    // member 테이블의 Primary Key인 id로 FirebaseStorage에 저장된 profile 이미지 이름 가져오기
                    RetrofitHelper.getRetrofitInstance().create(RetrofitMemberService::class.java)
                        .getMemberProfile(
                            G.member.id     // 선생님 아이디
                        ).enqueue(object :Callback<String> {
                            override fun onResponse(call: Call<String>, response: Response<String>) {

                                // 디비에 저장된 프로필 사진 이름 가져오기
                                val profile = response.body()

                                // FirebaseStorage에서 저장된 기존 프로필 이미지 삭제
                                val storage = FirebaseStorage.getInstance()
                                val imgRef: StorageReference = storage.getReference().child("profileImage/$profile")
                                imgRef.delete()

                                // 디비 프로필 사진 이름 변경..
                                updateMemberProfile()
                            }

                            override fun onFailure(call: Call<String>, t: Throwable) {}
                        })

                    Glide.with(this).load(profile).into(civ)
                }
            }
        )

    // 디비에 저장된 프로필 사진 이름 변경
    private fun updateMemberProfile() {
        // 현재 시간 가져오기
        val sdf = SimpleDateFormat("yyyyMMddHHmmss")
        sdf.timeZone = TimeZone.getTimeZone("Asia/Seoul")

        // 현재 시간 가져와서 메세지에 첨부한 이미지 파일의 이름으로 만들기
        val profileName = "IMG_${sdf.format(Date())}"

        // 디비에 저장된 프로필 이미지 이름 변경하기
        RetrofitHelper.getRetrofitInstance().create(RetrofitMemberService::class.java)
            .updateMemberProfile(
                G.member.id,            // 선생님 아이디
                profileName             // 프로필 이미지 이름
            ).enqueue(object :Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val message = response.body()

                    // 넘어오는 문자열 : 수정 성공
                    if(message?.contains("성공") ?: false) {
                        val storage = FirebaseStorage.getInstance()
                        val imgRef: StorageReference = storage.getReference().child("profileImage/$profileName")
                        imgRef.putFile(profile!!).addOnSuccessListener {
                            Toast.makeText(this@MainActivity, "프로필 이미지 변경 성공", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {}
            })
    }

    // 바깥 화면 터치 시 소프트 키보드 숨기기
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(InputMethodManager::class.java)
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        currentFocus?.clearFocus()
        return super.dispatchTouchEvent(ev)
    }

    // 우측 상단에 로그아웃 메뉴 버튼 보이기
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_logout) {   // 로그아웃
            AlertDialog.Builder(this)
                .setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                    G.member = Member("","","","","", call_number = "")

                    val intent = Intent(this, AcademyLoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                })
                .setNegativeButton("NO", null)
                .show()
        }
        return super.onOptionsItemSelected(item)
    }
}