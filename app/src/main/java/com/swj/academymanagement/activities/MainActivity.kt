package com.swj.academymanagement.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.swj.academymanagement.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding:ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}