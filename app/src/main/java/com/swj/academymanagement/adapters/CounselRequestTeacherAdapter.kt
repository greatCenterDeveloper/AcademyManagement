package com.swj.academymanagement.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.gson.Gson
import com.swj.academymanagement.activities.CounselActivity
import com.swj.academymanagement.activities.CounselDetailActivity
import com.swj.academymanagement.databinding.RecyclerItemCounselRequestTeacherBinding
import com.swj.academymanagement.model.CounselRequestTeacher

// 선생님 권한 선생님 강좌에 수강 중인 학생 상담 신청 RecyclerView 어댑터
class CounselRequestTeacherAdapter(val context: Context, val counselRequestTeacherArr:MutableList<CounselRequestTeacher>)
    :Adapter<CounselRequestTeacherAdapter.VH>(){
    inner class VH(val binding:RecyclerItemCounselRequestTeacherBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemCounselRequestTeacherBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = counselRequestTeacherArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val crt = counselRequestTeacherArr[position]

        // 상담 학생 이름
        holder.binding.tvName.text = crt.name

        // 상담 신청일
        holder.binding.tvDate.text = crt.date

        // 상담 희망일
        holder.binding.tvCounselDate.text = crt.counselDate

        // 상담 시작 시간
        holder.binding.tvCounselStartTime.text = crt.counselStartTime

        // 상담 마지막 시간
        holder.binding.tvCounselEndTime.text = crt.counselEndTime

        // 학생이 작성한 상담 신청 내용
        holder.binding.tvCounselContent.text = crt.counselContent

        // 상담하기 버튼 ( 상담 상세 화면 이동 )
        holder.binding.btnCounsel.setOnClickListener {
            val intent = Intent(context, CounselDetailActivity::class.java)

            // 상담 입력 화면에 가져갈 상담 신청 객체
            intent.putExtra("counselRequest", Gson().toJson(crt))

            // 상담 상세 화면으로 화면 전환 효과
            val options: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context as CounselActivity, Pair(holder.binding.tvCounselContent, "counselDetailTeacher")
                )
            context.startActivity(intent, options.toBundle())
        }
    }
}