package com.swj.academymanagement.activities

import android.R
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.swj.academymanagement.G
import com.swj.academymanagement.adapters.SmsImageAdapter
import com.swj.academymanagement.databinding.ActivitySmsSendBinding
import com.swj.academymanagement.model.Member
import com.swj.academymanagement.network.RetrofitHelper
import com.swj.academymanagement.network.RetrofitSendMessageService
import com.swj.academymanagement.network.RetrofitStudentManagementService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone


// 선생님 권한 문자 메시지 전송 화면 (가상으로..)
class SmsSendActivity : AppCompatActivity() {

    val binding:ActivitySmsSendBinding by lazy { ActivitySmsSendBinding.inflate(layoutInflater) }

    // 문자 메시지 전송 시 첨부한 이미지 리스트
    val images:MutableList<Uri> = mutableListOf()

    // 학생 드롭다운 리스트에서 문자 보낼 학생의 인덱스 번호
    var choiceStudentIndex = -1

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

        // 문자 보낼 시 첨부할 이미지들
        binding.pager.adapter = SmsImageAdapter(this, images)
        // 이미지 아래 ... 버튼
        binding.dotsIndicator.attachTo(binding.pager)
        // 사진 선택 버튼
        binding.btnSelectImage.setOnClickListener { selectImage() }

        // 내 강좌에 수강 중인 학생의 리스트
        var str:MutableList<Member> = mutableListOf()

        // 학생 이름 드롭다운 메뉴 ( 문자 보낼 학생 이름 선택하기 위해.. )
        var nameList:MutableList<String> = mutableListOf()

        // 학생 이름 드롭다운 메뉴에 추가할 내 강좌에 수강 중인 학생 리스트 가져오기
        RetrofitHelper.getRetrofitInstance().create(RetrofitStudentManagementService::class.java)
            .studentList(
                G.member.id     // 선생님 아이디
            ).enqueue(object : Callback<MutableList<Member>> {
                override fun onResponse(
                    call: Call<MutableList<Member>>,
                    response: Response<MutableList<Member>>
                ) {
                    val studentArr = response.body()
                    if(studentArr != null) {    // 학생 리스트가 존재한다면.
                        str = studentArr        // 내 강좌에 수강 중인 학생 리스트에 넣기
                        for(i in 0 until str.size) {
                            nameList.add(str[i].name)   // 학생 이름 드롭다운 메뉴에 추가할 학생 이름 리스트 생성
                        }

                        val nameAdapter: ArrayAdapter<String> =
                            ArrayAdapter(this@SmsSendActivity, R.layout.simple_list_item_1, nameList)
                        binding.acTvStudent.setAdapter(nameAdapter)
                    }
                }

                override fun onFailure(call: Call<MutableList<Member>>, t: Throwable) {
                    AlertDialog.Builder(this@SmsSendActivity)
                        .setMessage("error : ${t.message}")
                        .setPositiveButton("OK", null).show()
                }
            })

        // 학생 이름 드롭다운 메뉴 학생 선택 시 TextView에 해당 학생의 전화번호 보여주기
        binding.acTvStudent.setOnItemClickListener { _, _, i, _ ->
            // 선택한 학생 이름의 학생 휴대폰 번호 보여주기
            binding.tvCallNumber.text = str[i].call_number

            // 선택한 학생의 인덱스 번호
            choiceStudentIndex = i
        }

        // 문자 메세지 전송 버튼
        binding.btnSendMessage.setOnClickListener {

            // 작성한 문자 메세지 내용
            val message = binding.tilSmsContent.editText?.text.toString()

            // 현재 시간 가져오기
            val sdf = SimpleDateFormat("yyyyMMddHHmmss")
            sdf.timeZone = TimeZone.getTimeZone("Asia/Seoul")

            // 현재 시간 가져와서 메세지에 첨부한 이미지 파일의 이름으로 만들기
            val name = "IMG_${sdf.format(Date())}"

            // 파이어베이스 이미지 파일 저장
            val storage = FirebaseStorage.getInstance()
            for(i in 0 until images.size) {
                val imgRef:StorageReference = storage.getReference("profileImage/${name}${i+1}")
                imgRef.putFile(images[i]).addOnSuccessListener {
                    imgRef.downloadUrl.addOnSuccessListener {
                        // 이미지를 성공적으로 전송했다면 이미지 uri 가져와서 문자 메세지 DB에 저장
                        RetrofitHelper.getRetrofitInstance().create(RetrofitSendMessageService::class.java)
                            .sendMessage(
                                str[choiceStudentIndex].id, // 문자 메세지 받을 학생의 아이디
                                G.member.id,                // 문자 메세지 보낼 선생님 아이디
                                message,                    // 문자 메세지 내용
                                it.toString(),              // 첨부한 이미지 파일 주소
                                (i+1),                      // php에서 현재 몇 번째 이미지가 전송되고 있는지 확인하기 위해..
                                images.size                 // php에서 이미지 리스트 길이를 가지고 이미지 리스트 길이만큼 insert 하기 위해서..
                            ).enqueue(object :Callback<String> {
                                override fun onResponse(call: Call<String>, response: Response<String>) {
                                    val message = response.body()
                                    if(!message.isNullOrEmpty())
                                        AlertDialog.Builder(this@SmsSendActivity)
                                            .setMessage("$message")
                                            .setPositiveButton("OK", null).show()
                                }

                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    AlertDialog.Builder(this@SmsSendActivity)
                                        .setMessage("error : ${t.message}")
                                        .setPositiveButton("OK", null).show()
                                }
                            })
                    }
                }
            }
        }
    }

    // 이미지 선택 메소드
    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
            .setType("image/*")
            .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        imagePickMultipleLauncher.launch(intent)
    }

    // 메세지에 첨부할 이미지 선택 객체
    private val imagePickMultipleLauncher:ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
                if(it.resultCode != RESULT_CANCELED) {
                    val intent = it.data
                    val clipData:ClipData = intent?.clipData!!
                    images.clear()
                    for(i in 0 until clipData.itemCount)
                        images.add(clipData.getItemAt(i).uri)
                    binding.pager.adapter!!.notifyDataSetChanged()
                }
            }

    // 바깥 화면 터치 시 소프트 키보드 숨기기
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(InputMethodManager::class.java)
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        currentFocus?.clearFocus()
        return super.dispatchTouchEvent(ev)
    }
}