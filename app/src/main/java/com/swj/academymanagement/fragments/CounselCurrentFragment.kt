package com.swj.academymanagement.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.swj.academymanagement.G
import com.swj.academymanagement.activities.CounselActivity
import com.swj.academymanagement.adapters.CounselCurrentAdapter
import com.swj.academymanagement.databinding.FragmentCounselCurrentBinding
import com.swj.academymanagement.model.CounselCurrent
import com.swj.academymanagement.network.RetrofitCounselService
import com.swj.academymanagement.network.RetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 선생님 권한 상담 현황 리스트 Fragment
class CounselCurrentFragment : Fragment() {

    lateinit var binding:FragmentCounselCurrentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCounselCurrentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 선생님 상담 Activity 객체
        val ca = requireActivity() as CounselActivity

        // 선생님 강좌에 수강 중인 학생과 상담한 상담 현황에서 학생의 이름으로 검색
        // ( 입력한 이름이 포함된 모든 학생 리스트 )
        binding.btnSearch.setOnClickListener {
            // 검색하기 위해 입력한 학생 이름
            val name = binding.tilName.editText?.text.toString()

            RetrofitHelper.getRetrofitInstance().create(RetrofitCounselService::class.java)
                .counselNameSearch(
                    G.member.id,    // 선생님 아이디
                    name            // 학생 이름
                ).enqueue(object :Callback<MutableList<CounselCurrent>> {
                    override fun onResponse(
                        call: Call<MutableList<CounselCurrent>>,
                        response: Response<MutableList<CounselCurrent>>
                    ) {
                        val counselCurrentArr = response.body()
                        if(counselCurrentArr != null) {
                            binding.recycler.adapter = CounselCurrentAdapter(ca, counselCurrentArr)
                            if(counselCurrentArr.size == 0)
                                Toast.makeText(ca, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<MutableList<CounselCurrent>>, t: Throwable) {
                        AlertDialog.Builder(ca)
                            .setMessage("error : ${t.message}")
                            .setPositiveButton("OK", null).show()
                    }
                })
        }

        // 선생님 강좌에 수강 중인 학생과 상담한 상담 현황 리스트 가져오기
        RetrofitHelper.getRetrofitInstance().create(RetrofitCounselService::class.java)
            .counselList(
                G.member.id     // 선생님 아이디
            ).enqueue(object : Callback<MutableList<CounselCurrent>> {
                override fun onResponse(
                    call: Call<MutableList<CounselCurrent>>,
                    response: Response<MutableList<CounselCurrent>>
                ) {
                    val counselCurrentArr = response.body()
                    if(counselCurrentArr != null) {
                        if(counselCurrentArr.size > 0) { // 상담 현황 리스트가 존재한다면..
                            binding.recycler.adapter = CounselCurrentAdapter(ca, counselCurrentArr)
                        } else { // 상담 현황 리스트가 없다면..
                            // 상담 현황 RecyclerView 숨기기
                            binding.recycler.visibility = View.GONE
                            // 상담 내역이 없다는 문구 출력
                            binding.tvNoCounsel.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onFailure(call: Call<MutableList<CounselCurrent>>, t: Throwable) {
                    AlertDialog.Builder(ca)
                        .setMessage("error : ${t.message}")
                        .setPositiveButton("OK", null).show()
                }
            })
    }
}