package com.swj.academymanagement.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.swj.academymanagement.adapters.CounselRequestTeacherAdapter
import com.swj.academymanagement.databinding.FragmentCounselRequestBinding
import com.swj.academymanagement.model.CounselRequestTeacher

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

        val counselRequestArr:MutableList<CounselRequestTeacher> = mutableListOf()
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
        binding.recycler.adapter = CounselRequestTeacherAdapter(requireActivity(), counselRequestArr)
    }
}