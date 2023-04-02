package com.swj.academymanagement.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.swj.academymanagement.adapters.CounselCurrentAdapter
import com.swj.academymanagement.databinding.FragmentCounselCurrentBinding
import com.swj.academymanagement.model.CounselCurrent

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

        val counselCurrentArr:MutableList<CounselCurrent> = mutableListOf()
        counselCurrentArr.add(
            CounselCurrent(
            "가강사", "2023-02-18", "홍길동", "홍길동 학생 상담하였습니다.")
        )
        counselCurrentArr.add(CounselCurrent(
            "나강사", "2023-02-21", "김길동", "김길동 학생 상담 완료")
        )
        counselCurrentArr.add(CounselCurrent(
            "다강사", "2023-02-28", "최길동", "최길동 학생 상담 하였소.")
        )
        binding.recycler.adapter = CounselCurrentAdapter(requireActivity(), counselCurrentArr)
    }
}