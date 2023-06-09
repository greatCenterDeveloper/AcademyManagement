package com.swj.academymanagement.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.swj.academymanagement.G
import com.swj.academymanagement.activities.CounselActivity
import com.swj.academymanagement.adapters.CounselRequestTeacherAdapter
import com.swj.academymanagement.databinding.FragmentCounselRequestBinding
import com.swj.academymanagement.model.CounselRequestTeacher
import com.swj.academymanagement.network.RetrofitCounselService
import com.swj.academymanagement.network.RetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 선생님 권한 학생 상담 신청 리스트 Fragment
class CounselRequestFragment : Fragment() {
    lateinit var binding:FragmentCounselRequestBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCounselRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 선생님 상담 Activity 객체
        val ca = requireActivity() as CounselActivity

        // 선생님 강좌에 수강 중인 학생의 상담 신청 현황 리스트 가져오기
        retrofitCounselRequestStudentList(ca)

        // 선생님이 학원 앱에 로그인한 상태에서 학생이 상담 신청을 할 수 있으므로 SwipeRefreshLayout이 필요하다.
        binding.swipeRefreshLayout.setOnRefreshListener {
            retrofitCounselRequestStudentList(ca, binding.swipeRefreshLayout)
        }
    }

    private fun retrofitCounselRequestStudentList(ca:CounselActivity, swipeRefreshLayout: SwipeRefreshLayout? = null) {
        RetrofitHelper.getRetrofitInstance().create(RetrofitCounselService::class.java)
            .counselRequestStudentList(
                G.member.id     // 선생님 아이디
            ).enqueue(object :Callback<MutableList<CounselRequestTeacher>> {
                override fun onResponse(
                    call: Call<MutableList<CounselRequestTeacher>>,
                    response: Response<MutableList<CounselRequestTeacher>>
                ) {
                    val counselRequestArr = response.body()
                    if(counselRequestArr != null) {
                        if(counselRequestArr.size > 0) {
                            binding.recycler.adapter = CounselRequestTeacherAdapter(ca, counselRequestArr)

                            if(swipeRefreshLayout != null) swipeRefreshLayout.isRefreshing = false

                        } else { // 상담 신청 리스트가 없다면..
                            // 상담 신청 RecyclerView 숨기기
                            binding.recycler.visibility = View.GONE
                            // 상담 신청 내역이 없다는 문구 출력
                            binding.tvNoCounsel.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onFailure(
                    call: Call<MutableList<CounselRequestTeacher>>,
                    t: Throwable
                ) {
                    Toast.makeText(context, "error : ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}