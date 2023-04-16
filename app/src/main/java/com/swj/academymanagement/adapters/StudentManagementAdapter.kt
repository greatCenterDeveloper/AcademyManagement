package com.swj.academymanagement.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.swj.academymanagement.G
import com.swj.academymanagement.activities.StudentDetailActivity
import com.swj.academymanagement.databinding.RecyclerItemStudentManagementBinding
import com.swj.academymanagement.model.Member
import com.swj.academymanagement.network.RetrofitHelper
import com.swj.academymanagement.network.RetrofitMemberService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 선생님 권한 학생 관리 학생 RecyclerView 어댑터
class StudentManagementAdapter(val context:Context, val studentArr:List<Member>, val teacherId:String)
    :Adapter<StudentManagementAdapter.VH>() {
    inner class VH(val binding: RecyclerItemStudentManagementBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemStudentManagementBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = studentArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val student:Member = studentArr[position]

        // member 테이블의 Primary Key인 id로 FirebaseStorage에 저장된 profile 이미지 이름 가져오기
        RetrofitHelper.getRetrofitInstance().create(RetrofitMemberService::class.java)
            .getMemberProfile(
                student.id     // 학생 아이디
            ).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {

                    // 디비에 저장된 프로필 사진 이름 가져오기
                    val profile = response.body()

                    // FirebaseStorage에서 불러온 사진 recyclerView 프로필 이미지 공간에 붙이기
                    val storage = FirebaseStorage.getInstance()
                    val imgRef: StorageReference = storage.getReference().child("profileImage/$profile")
                    imgRef.downloadUrl.addOnSuccessListener {

                        // 학생 프로필 이미지
                        Glide.with(context).load(it).into(holder.binding.civProfile)
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {}
            })


        // 학생 이름
        holder.binding.tvName.text = student.name

        // 학생이 수강 중인 강좌
        val courses = StringBuffer()
        for(course in student.courseArr) {
            if(course == "국어") courses.append("국어, ")
            if(course == "영어") courses.append("영어, ")
            if(course == "수학") courses.append("수학")
        }

        // 강좌의 [, ] 삭제하기 위해서...
        var course:String = courses.toString()
        val last = course.substring(course.length-2, course.length)
        if(last == ", ") course = course.substring(0, course.length-2)

        // 학생이 수강 중인 강좌
        holder.binding.tvCourse.text = course

        // 학생 휴대폰 번호
        holder.binding.tvCall.text = student.call_number

        // 학생이 수강 중인 강좌 리스트에서 뽑아온 강좌들을 , 붙여서 강좌에 저장
        student.course = course

        // 전화 걸기 버튼
        holder.binding.btnPhoneCall.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_DIAL
            // 학생이 휴대폰 번호가 입력된 채로 전화걸기 앱 실행
            intent.data = Uri.parse("tel:${student.call_number}")
            context.startActivity(intent)
        }

        // 학생 클릭 시 학생 상세 페이지로 이동..
        holder.binding.root.setOnClickListener {
            val intent:Intent = Intent(context, StudentDetailActivity::class.java)
            // 학생 상세 화면에 가져갈 학생 정보
            intent.putExtra("student", Gson().toJson(student))
            context.startActivity(intent)
        }
    }
}