package com.swj.academymanagement.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.swj.academymanagement.G
import com.swj.academymanagement.adapters.CounselRequestAdapter
import com.swj.academymanagement.databinding.ActivityCounselRequestBinding
import com.swj.academymanagement.model.CounselRequest
import com.swj.academymanagement.network.RetrofitCounselStudentService
import com.swj.academymanagement.network.RetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 학생 권한 상담 신청 리스트 조회 화면
class CounselRequestActivity : AppCompatActivity() {

    val binding:ActivityCounselRequestBinding by lazy {
        ActivityCounselRequestBinding.inflate(layoutInflater)
    }

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

        // 상담 신청 작성
        binding.fab.setOnClickListener {
            startActivity(Intent(this, CounselRequestInsertActivity::class.java))
            finish()
        }

        // 상담 신청 리스트 조회
        retrofitCounselRequestList()

        // 학생이 학원에 이미 로그인 한 상태인데.. 학생이 상담 신청한 신청 내용을 선생님과 상담하게되면
        // 해당 상담 신청은 상담 신청 리스트에서 없어져야 하므로 SwipeRefreshLayout이 필요하다.
        binding.swipeRefreshLayout.setOnRefreshListener {
            retrofitCounselRequestList(binding.swipeRefreshLayout)
        }
    }

    override fun onResume() {
        super.onResume()

        // 상담 신청 리스트 조회
        retrofitCounselRequestList()
    }

    // 학생이 작성한 상담 신청 리스트 조회
    private fun retrofitCounselRequestList(swipeRefreshLayout: SwipeRefreshLayout? = null) {
        RetrofitHelper.getRetrofitInstance().create(RetrofitCounselStudentService::class.java)
            .counselRequestList(
                G.member.id     // 학생 아이디
            ).enqueue(object :Callback<MutableList<CounselRequest>> {
                override fun onResponse(
                    call: Call<MutableList<CounselRequest>>,
                    response: Response<MutableList<CounselRequest>>
                ) {
                    val counselRequestArr = response.body()
                    if(counselRequestArr != null) {
                        // 작성한 상담 신청 리스트가 없다면..
                        if(counselRequestArr.size == 0) {
                            // 상담 신청 내역이 없다는 문구 출력
                            binding.tvNoCounselRequest.visibility = View.VISIBLE

                            // 기존의 RecyclerView 숨기기
                            binding.recycler.visibility = View.GONE
                        } else {
                            binding.recycler.adapter =
                                CounselRequestAdapter(this@CounselRequestActivity, counselRequestArr)
                            if(swipeRefreshLayout != null) swipeRefreshLayout.isRefreshing = false
                        }
                    }
                }

                override fun onFailure(call: Call<MutableList<CounselRequest>>, t: Throwable) {
                    if(!(this@CounselRequestActivity).isFinishing) {
                        Toast.makeText(this@CounselRequestActivity, "error : ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }
}