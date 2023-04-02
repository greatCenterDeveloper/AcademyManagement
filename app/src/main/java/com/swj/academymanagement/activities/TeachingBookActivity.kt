package com.swj.academymanagement.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.swj.academymanagement.R
import com.swj.academymanagement.adapters.TeachingBookAdapter
import com.swj.academymanagement.databinding.ActivityTeachingBookBinding
import com.swj.academymanagement.model.ShoppingItem

class TeachingBookActivity : AppCompatActivity() {

    val binding:ActivityTeachingBookBinding by lazy { ActivityTeachingBookBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val teachingBookArr:MutableList<ShoppingItem> = mutableListOf()

        teachingBookArr.add(
            ShoppingItem(
            "안드로이드", "", "", "10000", "30000", "교보문고")
        )
        teachingBookArr.add(ShoppingItem(
            "아이폰", "", "", "15000", "29000", "중앙문고")
        )
        teachingBookArr.add(ShoppingItem(
            "하이브리드웹앱", "", "", "11000", "25000", "서울문고")
        )
        binding.recycler.adapter = TeachingBookAdapter(this, teachingBookArr)
    }
}