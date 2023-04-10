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


class SmsSendActivity : AppCompatActivity() {

    val binding:ActivitySmsSendBinding by lazy { ActivitySmsSendBinding.inflate(layoutInflater) }
    val images:MutableList<Uri> = mutableListOf()
    var choiceStudentIndex = -1

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
        binding.pager.adapter = SmsImageAdapter(this, images)
        binding.dotsIndicator.attachTo(binding.pager)
        binding.btnSelectImage.setOnClickListener { selectImage() }

        //val teacher = Gson().fromJson(intent.getStringExtra("teacher"), Member::class.java)

        var str:MutableList<Member> = mutableListOf()
        var nameList:MutableList<String> = mutableListOf()

        RetrofitHelper.getRetrofitInstance().create(RetrofitStudentManagementService::class.java)
            .studentList(G.member.id).enqueue(object : Callback<MutableList<Member>> {
                override fun onResponse(
                    call: Call<MutableList<Member>>,
                    response: Response<MutableList<Member>>
                ) {
                    val studentArr = response.body()
                    if(studentArr != null) {
                        str = studentArr
                        for(i in 0 until str.size) {
                            nameList.add(str[i].name)
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

        binding.acTvStudent.setOnItemClickListener { _, _, i, _ ->
            binding.tvCallNumber.text = str[i].call_number
            choiceStudentIndex = i
        }

        binding.btnSendMessage.setOnClickListener {
            val message = binding.tilSmsContent.editText?.text.toString()


            // 파이어베이스 이미지 파일 저장
            val sdf = SimpleDateFormat("yyyyMMddHHmmss")
            sdf.timeZone = TimeZone.getTimeZone("Asia/Seoul")
            val name = "IMG_${sdf.format(Date())}"

            val storage = FirebaseStorage.getInstance()

            for(i in 0 until images.size) {
                val imgRef:StorageReference = storage.getReference("profileImage/${name}${i+1}")
                imgRef.putFile(images[i]).addOnSuccessListener {
                    imgRef.downloadUrl.addOnSuccessListener {
                        RetrofitHelper.getRetrofitInstance().create(RetrofitSendMessageService::class.java)
                            .sendMessage(str[choiceStudentIndex].id, G.member.id, message, it.toString(), (i+1), images.size)
                            .enqueue(object :Callback<String> {
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

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
            .setType("image/*")
            .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        imagePickMultipleLauncher.launch(intent)
    }

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

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(InputMethodManager::class.java)
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        currentFocus?.clearFocus()
        return super.dispatchTouchEvent(ev)
    }
}