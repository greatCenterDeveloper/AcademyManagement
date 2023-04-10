package com.swj.academymanagement.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
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

        /*val counselRequestArr:MutableList<CounselRequestTeacher> = mutableListOf()
        counselRequestArr.add(
            CounselRequestTeacher(
            "aaa", "홍길동", "2023-02-11", "2023-02-13",
            "15:00", "15:30", "상담을 원합니다.")
        )
        counselRequestArr.add(CounselRequestTeacher(
            "bbb", "김길동", "2023-02-12", "2023-02-14",
            "15:00", "15:30", "상담을 원하오. 안되겠소?")
        )
        counselRequestArr.add(CounselRequestTeacher(
            "ccc", "최길동", "2023-02-13", "2023-02-15",
            "15:00", "15:30", "상담을 원하옵니다. 상담좀 해주시옵소서.")
        )
        binding.recycler.adapter = CounselRequestTeacherAdapter(requireActivity(), counselRequestArr)*/

        val ca = requireActivity() as CounselActivity

        RetrofitHelper.getRetrofitInstance().create(RetrofitCounselService::class.java)
            .counselRequestStudentList(G.member.id)
            .enqueue(object :Callback<MutableList<CounselRequestTeacher>> {
                override fun onResponse(
                    call: Call<MutableList<CounselRequestTeacher>>,
                    response: Response<MutableList<CounselRequestTeacher>>
                ) {
                    val counselRequestArr = response.body()
                    if(counselRequestArr != null) {
                        if(counselRequestArr.size > 0) {
                            binding.recycler.adapter = CounselRequestTeacherAdapter(ca, counselRequestArr)
                        } else {
                            binding.recycler.visibility = View.GONE
                            binding.tvNoCounsel.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onFailure(
                    call: Call<MutableList<CounselRequestTeacher>>,
                    t: Throwable
                ) {
                    AlertDialog.Builder(ca)
                        .setMessage("error : ${t.message}")
                        .setPositiveButton("OK", null).show()
                }
            })
    }
}