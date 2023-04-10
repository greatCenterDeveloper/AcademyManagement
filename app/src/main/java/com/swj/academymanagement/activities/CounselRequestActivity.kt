package com.swj.academymanagement.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.swj.academymanagement.G
import com.swj.academymanagement.adapters.CounselRequestAdapter
import com.swj.academymanagement.databinding.ActivityCounselRequestBinding
import com.swj.academymanagement.model.CounselRequest
import com.swj.academymanagement.network.RetrofitCounselStudentService
import com.swj.academymanagement.network.RetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CounselRequestActivity : AppCompatActivity() {

    val binding:ActivityCounselRequestBinding by lazy {
        ActivityCounselRequestBinding.inflate(layoutInflater)
    }
    //lateinit var student:Member

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

        //student = Gson().fromJson(intent.getStringExtra("student"), Member::class.java)

        binding.ivBackspace.setOnClickListener { finish() }
        binding.fab.setOnClickListener {
            //val intent = Intent(this, CounselInputActivity::class.java)
            //intent.putExtra("student", Gson().toJson(G.member))
            //startActivity(intent)
            startActivity(Intent(this, CounselInputActivity::class.java))
        }



        /*val counselRequestArr:MutableList<CounselRequest> = mutableListOf()
        counselRequestArr.add(CounselRequest(
            "2023/03/01", "16:00", "16:30", "상담이 필요합니다.")
        )
        counselRequestArr.add(CounselRequest(
            "2023/03/05", "16:10", "16:40", "상담이 필요하오.")
        )
        counselRequestArr.add(CounselRequest(
            "2023/03/10", "15:00", "15:30", "상담이 필요하옵니다.")
        )

        if(counselRequestArr.size == 0) {
            binding.tvNoCounselRequest.visibility = View.VISIBLE
            binding.recycler.visibility = View.GONE
        }
        else binding.recycler.adapter = CounselRequestAdapter(this, counselRequestArr)*/

        retrofitCounselRequestList()
    }

    override fun onResume() {
        super.onResume()
        retrofitCounselRequestList()
    }

    private fun retrofitCounselRequestList() {
        RetrofitHelper.getRetrofitInstance().create(RetrofitCounselStudentService::class.java)
            .counselRequestList(G.member.id).enqueue(object :Callback<MutableList<CounselRequest>> {
                override fun onResponse(
                    call: Call<MutableList<CounselRequest>>,
                    response: Response<MutableList<CounselRequest>>
                ) {
                    val counselRequestArr = response.body()
                    if(counselRequestArr != null) {
                        if(counselRequestArr.size == 0) {
                            binding.tvNoCounselRequest.visibility = View.VISIBLE
                            binding.recycler.visibility = View.GONE
                        } else binding.recycler.adapter =
                            CounselRequestAdapter(this@CounselRequestActivity, counselRequestArr)
                    }
                }

                override fun onFailure(call: Call<MutableList<CounselRequest>>, t: Throwable) {
                    AlertDialog.Builder(this@CounselRequestActivity)
                        .setMessage("error : ${t.message}")
                        .setPositiveButton("OK", null).show()
                }
            })
    }
}