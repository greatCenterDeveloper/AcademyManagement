package com.swj.academymanagement.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.swj.academymanagement.databinding.DialogStudentManagementAttendanceBinding
import com.swj.academymanagement.databinding.RecyclerItemStudentManagementCourseBinding
import com.swj.academymanagement.model.StudentManagementCourse
import com.swj.academymanagement.model.StudentManagementDialogAttendance
import com.swj.academymanagement.network.RetrofitHelper
import com.swj.academymanagement.network.RetrofitStudentManagementService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 선생님 권한 학생 상세 페이지에서 학생이 수강 중인 강좌 RecyclerView 어댑터
class StudentManagementCourseAdapter(val context:Context, val courseArr:MutableList<StudentManagementCourse>)
    : Adapter<StudentManagementCourseAdapter.VH>() {

    inner class VH(val binding: RecyclerItemStudentManagementCourseBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemStudentManagementCourseBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = courseArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val smc = courseArr[position]

        // 강좌 코드를 강좌명으로 변경
        when(smc.course) {
            "kor"  -> holder.binding.tvCourse.text = "국어"
            "eng"  -> holder.binding.tvCourse.text = "영어"
            "math" -> holder.binding.tvCourse.text = "수학"
        }

        // 선생님 프로필 이미지
        if(!smc.profile.equals(""))
            Glide.with(context).load(smc.profile).into(holder.binding.civProfile)

        // 선생님 이름
        holder.binding.tvName.text = "${smc.teacher} 선생님"

        // 출석 현황
        holder.binding.tvAttendance.text = smc.attendance

        // 결석 현황
        holder.binding.tvAbsence.text = smc.absence

        // 출석부 버튼 클릭
        holder.binding.btnAttendance.setOnClickListener {
            val dialogBinding = DialogStudentManagementAttendanceBinding.inflate(LayoutInflater.from(context))
            val dialog:AlertDialog = AlertDialog.Builder(context)
                                    .setView(dialogBinding.root)
                                    .create()
            // 강좌 코드를 강좌명으로 변경
            when(smc.course) {
                "kor"  -> dialogBinding.tvCourse.text = "국어"
                "eng"  -> dialogBinding.tvCourse.text = "영어"
                "math" -> dialogBinding.tvCourse.text = "수학"
            }

            // 닫기 이미지 클릭시 다이얼로그 닫기
            dialogBinding.ivClose.setOnClickListener { dialog.dismiss() }

            // 해당 강좌의 출석 현황 가져오기
            RetrofitHelper.getRetrofitInstance().create(RetrofitStudentManagementService::class.java)
                .studentCourseAttendanceList(
                    smc.course,         // 학생이 수강 중인 강좌명
                    smc.studentId,      // 학생 아이디
                    smc.teacher         // 선생 아이디
                ).enqueue(object : Callback<MutableList<StudentManagementDialogAttendance>> {
                    override fun onResponse(
                        call: Call<MutableList<StudentManagementDialogAttendance>>,
                        response: Response<MutableList<StudentManagementDialogAttendance>>
                    ) {
                        val attendanceArr = response.body()

                        if(attendanceArr != null)
                            dialogBinding.recycler.adapter = StudentManagementDialogAttendanceAdapter(context, attendanceArr)
                        else {
                            dialogBinding.recycler.visibility = View.GONE
                            dialogBinding.tvNoAttendance.visibility = View.VISIBLE
                        }
                    }

                    override fun onFailure(
                        call: Call<MutableList<StudentManagementDialogAttendance>>,
                        t: Throwable
                    ) {
                        AlertDialog.Builder(context)
                            .setMessage("error : ${t.message}")
                            .setPositiveButton("OK", null).show()
                    }
                })
            dialog.show()
        }
    }
}