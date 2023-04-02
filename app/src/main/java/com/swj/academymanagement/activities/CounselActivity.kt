package com.swj.academymanagement.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.swj.academymanagement.adapters.CounselAdapter
import com.swj.academymanagement.databinding.ActivityCounselBinding

class CounselActivity : AppCompatActivity() {

    val binding:ActivityCounselBinding by lazy { ActivityCounselBinding.inflate(layoutInflater) }
    private val tabTitle = arrayOf("상담 신청", "상담 현황")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.pager.adapter = CounselAdapter(this)

        val mediator = TabLayoutMediator(binding.tabLayout, binding.pager){
                tab, position -> tab.text = tabTitle[position]
        }
        mediator.attach()
    }
}