package com.swj.academymanagement.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.swj.academymanagement.fragments.CounselCurrentFragment
import com.swj.academymanagement.fragments.CounselRequestFragment

class CounselAdapter(fragmentActivity: FragmentActivity)
    : FragmentStateAdapter(fragmentActivity) {

    private val fragments = arrayOfNulls<Fragment>(2)

    init {
        fragments[0] = CounselRequestFragment()
        fragments[1] = CounselCurrentFragment()
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]!!
}