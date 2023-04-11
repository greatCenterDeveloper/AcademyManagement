package com.swj.academymanagement.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.swj.academymanagement.fragments.CounselCurrentFragment
import com.swj.academymanagement.fragments.CounselRequestFragment

// 선생님 권한 상담 신청 / 상담 현황 Fragment 어댑터
class CounselAdapter(fragmentActivity: FragmentActivity)
    : FragmentStateAdapter(fragmentActivity) {

    private val fragments = arrayOfNulls<Fragment>(2)

    init {
        // 상담 신청 Fragment
        fragments[0] = CounselRequestFragment()

        // 상담 현황 Fragment
        fragments[1] = CounselCurrentFragment()
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]!!
}