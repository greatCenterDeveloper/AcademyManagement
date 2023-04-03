package com.swj.academymanagement.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.swj.academymanagement.R
import com.swj.academymanagement.adapters.TeachingBookAdapter
import com.swj.academymanagement.databinding.ActivityTeachingBookBinding
import com.swj.academymanagement.model.ShoppingItem

class TeachingBookActivity : AppCompatActivity() {

    val binding:ActivityTeachingBookBinding by lazy { ActivityTeachingBookBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

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