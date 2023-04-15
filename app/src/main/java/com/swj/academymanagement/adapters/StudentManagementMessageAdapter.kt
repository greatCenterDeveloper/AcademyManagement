package com.swj.academymanagement.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.swj.academymanagement.G
import com.swj.academymanagement.R
import com.swj.academymanagement.databinding.RecyclerItemStudentManagementMessageBinding
import com.swj.academymanagement.model.StudentManagementMessage
import com.swj.academymanagement.network.RetrofitHelper
import com.swj.academymanagement.network.RetrofitStudentManagementService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 선생님 권한 학생 상세 페이지에서 학생에게 보낸 문자 메세지 RecyclerView 어댑터
class StudentManagementMessageAdapter(val context:Context, val messageArr:MutableList<StudentManagementMessage>)
    :Adapter<StudentManagementMessageAdapter.VH>() {

    inner class VH(val binding: RecyclerItemStudentManagementMessageBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemStudentManagementMessageBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = messageArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val message = messageArr[position]
        // 문자 보낸 날짜
        holder.binding.tvDate.text = message.date

        // 문자 메세지 내용
        holder.binding.tvMessage.text = message.message

        holder.binding.root.setOnClickListener {
            val popMenu = PopupMenu(context, holder.binding.root)
            popMenu.menuInflater.inflate(R.menu.menu_message, popMenu.menu)
            popMenu.show()

            popMenu.setOnMenuItemClickListener {
                if(it.itemId == R.id.menu_delete) {   // 문자 삭제
                    AlertDialog.Builder(context)
                        .setMessage("삭제하시겠습니까?")
                        .setNegativeButton("NO", null)
                        .setPositiveButton("OK") { _, _ ->

                            // 문자 테이블에 있는 FirebaseStorage에 저장된 이미지 경로들 가져와서
                            // FirebaseStorage 이미지 삭제
                            // 문자 테이블에서 해당 문자 삭제
                            studentDeleteMessageAndImage(
                                message.studentId,      // 학생 아이디
                                G.member.id,            // 선생 아이디
                                message.message,        // 문자 보낸 내용
                                message.date,           // 문자 보낸 날짜
                                message,                // 문자 DB 삭제 처리 완료 후 MutableList 에서 삭제할 메세지 객체
                                position                // 어댑터에게 notifyItemRemoved 할 때 전달할 포지션 넘버
                            )
                        }.show()
                }
                false
            }
        }
    }


    // 문자 삭제하기 전에 FirebaseStorage에 저장된 이미지 경로 불러오기
    private fun studentDeleteMessageAndImage(
        studentId:String,
        teacherId:String,
        message:String,
        date:String,
        deleteMessage:StudentManagementMessage,
        position:Int) {

        // 문자 테이블에 PK가 없으므로.. 입력된 네 개의 데이터가 모두 일치해야만
        // 문자 테이블에 저장된 이미지의 경로를 가져온다.
        RetrofitHelper.getRetrofitInstance().create(RetrofitStudentManagementService::class.java)
            .studentMessageGetImageUri(
                studentId,      // 학생 아이디
                teacherId,      // 선생 아이디
                message,        // 문자 메세지
                date            // 날짜
            ).enqueue(object :Callback<MutableList<String>> {
                override fun onResponse(
                    call: Call<MutableList<String>>,
                    response: Response<MutableList<String>>
                ) {
                    val imageArr = response.body()

                    if(imageArr != null) {
                        // 문자 테이블에 이미지의 경로가 존재한다면. FirebaseStorage에 존재하는 이미지 파일 삭제
                        deleteFirebaseStorageSavedImage(imageArr)

                        // 문자 테이블에 있는 해당 문자 레코드들 삭제
                        studentMessageDelete(studentId, teacherId, message, date)

                        messageArr.remove(deleteMessage)
                        this@StudentManagementMessageAdapter.notifyItemRemoved(position)
                    }
                }

                override fun onFailure(call: Call<MutableList<String>>, t: Throwable) {
                    Toast.makeText(context, "error : ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }


    // FirebaseStorage에 저장된 이미지들 삭제하기
    private fun deleteFirebaseStorageSavedImage(imageUriArr:MutableList<String>) {
        val storage = FirebaseStorage.getInstance()

        for(imageUri in imageUriArr) {
            // 이미지 가져오기
            val imgRef: StorageReference = storage.getReference().child("profileImage/$imageUri")

            // 이미지 삭제
            imgRef.delete().addOnSuccessListener {}
                .addOnFailureListener { Toast.makeText(context, "이미지 삭제 실패", Toast.LENGTH_SHORT).show() }
        }
    }


    // 문자 테이블의 문자 레코드들 삭제
    // 문자 테이블에는 primary Key가 없으므로.. 네 개의 데이터가 모두 일치해야만 삭제된다
    private fun studentMessageDelete(studentId:String, teacherId:String, message:String, date:String) {

        RetrofitHelper.getRetrofitInstance().create(RetrofitStudentManagementService::class.java)
            .studentMessageDelete(
                studentId,      // 학생 아이디
                teacherId,      // 선생 아이디
                message,        // 문자 메세지
                date            // 날짜
            ).enqueue(object :Callback<String> {
                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {
                    val message = response.body()
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    AlertDialog.Builder(context)
                        .setMessage("error : ${t.message}")
                        .setPositiveButton("OK", null).show()
                }
            })
    }
}