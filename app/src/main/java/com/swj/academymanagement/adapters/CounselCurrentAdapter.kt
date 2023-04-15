package com.swj.academymanagement.adapters

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.gson.Gson
import com.swj.academymanagement.R
import com.swj.academymanagement.activities.CounselActivity
import com.swj.academymanagement.activities.CounselDetailUpdateActivity
import com.swj.academymanagement.databinding.RecyclerItemCounselCurrentBinding
import com.swj.academymanagement.model.CounselCurrent
import com.swj.academymanagement.network.RetrofitCounselService
import com.swj.academymanagement.network.RetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 선생님 권한 상담 현황 RecyclerView 어댑터
class CounselCurrentAdapter(val context: Context, val counselCurrentArr:MutableList<CounselCurrent>)
    :Adapter<CounselCurrentAdapter.VH>(){
    inner class VH(val binding:RecyclerItemCounselCurrentBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemCounselCurrentBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = counselCurrentArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val cca = counselCurrentArr[position]

        // 상담 선생님 이름
        holder.binding.tvTeacher.text = cca.teacherName

        // 상담 일자
        holder.binding.tvDate.text = cca.date

        // 상담 학생 이름
        holder.binding.tvStudent.text = cca.studentName

        // 상담 내용
        holder.binding.tvCounselContent.text = cca.counselContent

        // 상담 내용 수정 / 삭제 PopupMenu
        holder.binding.root.setOnClickListener {
            val popMenu = PopupMenu(context, holder.binding.root)
            popMenu.menuInflater.inflate(R.menu.menu_options, popMenu.menu)
            popMenu.show()

            popMenu.setOnMenuItemClickListener {
                if(it.itemId == R.id.menu_update) {     // 상담 내용 수정
                    val intent = Intent(context, CounselDetailUpdateActivity::class.java)

                    // 상담 내용 수정 화면에 가져갈 상담 내용 정보
                    intent.putExtra("counsel", Gson().toJson(cca))

                    // 상담 내용 수정 화면으로 화면 전환 효과
                    val options: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            context as CounselActivity, Pair(holder.binding.tvCounselContent, "counsel")
                        )
                    context.startActivity(intent, options.toBundle())

                } else if(it.itemId == R.id.menu_delete) {  // 상담 내용 삭제

                    AlertDialog.Builder(context)
                        .setMessage("삭제하시겠습니까?")
                        .setNegativeButton("NO", null)
                        .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                            RetrofitHelper.getRetrofitInstance().create(RetrofitCounselService::class.java)
                                .counselDelete(
                                    cca.counselCode     // 상담 코드 (PK)
                                ).enqueue(object : Callback<String> {
                                    override fun onResponse(
                                        call: Call<String>,
                                        response: Response<String>
                                    ) {
                                        val message = response.body()

                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                                        // 넘어오는 문자열 : 상담 내용 삭제 완료 이므로..
                                        if(message?.contains("완료") ?: false) {
                                            counselCurrentArr.remove(cca)
                                            this@CounselCurrentAdapter.notifyItemRemoved(holder.adapterPosition)
                                        }
                                    }

                                    override fun onFailure(call: Call<String>, t: Throwable) {
                                        AlertDialog.Builder(context)
                                            .setMessage("error : ${t.message}")
                                            .setPositiveButton("OK", null).show()
                                    }
                                })
                        }).show()
                }
                false
            }
        }
    }
}