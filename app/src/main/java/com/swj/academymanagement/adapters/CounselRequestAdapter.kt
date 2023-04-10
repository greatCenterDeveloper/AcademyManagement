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
import com.swj.academymanagement.G
import com.swj.academymanagement.R
import com.swj.academymanagement.activities.CounselRequestActivity
import com.swj.academymanagement.activities.CounselRequestUpdateActivity
import com.swj.academymanagement.databinding.RecyclerItemCounselRequestBinding
import com.swj.academymanagement.model.CounselRequest
import com.swj.academymanagement.network.RetrofitCounselStudentService
import com.swj.academymanagement.network.RetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CounselRequestAdapter(val context: Context, val counselRequestArr:MutableList<CounselRequest>)
    :Adapter<CounselRequestAdapter.VH>() {
    inner class VH(val binding:RecyclerItemCounselRequestBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemCounselRequestBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = counselRequestArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val counselRequest = counselRequestArr[position]
        holder.binding.tvCounselDate.text = counselRequest.date
        holder.binding.tvCounselStartTime.text = counselRequest.startTime
        holder.binding.tvCounselEndTime.text = counselRequest.endTime
        holder.binding.tvCounselContent.text = counselRequest.content

        holder.binding.root.setOnClickListener {
            val popMenu = PopupMenu(context, holder.binding.root)
            popMenu.menuInflater.inflate(R.menu.menu_options, popMenu.menu)
            popMenu.show()

            popMenu.setOnMenuItemClickListener {
                if(it.itemId == R.id.menu_update) {
                    val intent = Intent(context, CounselRequestUpdateActivity::class.java)
                    intent.putExtra("counselRequest", Gson().toJson(counselRequest))

                    val options: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            context as CounselRequestActivity, Pair(holder.binding.tvCounselContent, "counselUpdate")
                        )
                    context.startActivity(intent, options.toBundle())
                } else if(it.itemId == R.id.menu_delete) {
                    G.counselRequestDeletePosition = position

                    AlertDialog.Builder(context)
                        .setMessage("삭제하시겠습니까?")
                        .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                            RetrofitHelper.getRetrofitInstance().create(RetrofitCounselStudentService::class.java)
                                .counselRequestDelete(counselRequest.studentId, counselRequest.counselRequestCode)
                                .enqueue(object :Callback<String> {
                                    override fun onResponse(
                                        call: Call<String>,
                                        response: Response<String>
                                    ) {
                                        val message = response.body()
                                        AlertDialog.Builder(context)
                                            .setMessage(message)
                                            .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                                                if(message?.contains("완료") ?: false)
                                                    this@CounselRequestAdapter.notifyItemRemoved(G.counselRequestDeletePosition)
                                            }).show()
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