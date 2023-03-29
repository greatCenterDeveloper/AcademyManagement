package com.swj.academymanagement.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.swj.academymanagement.R
import com.swj.academymanagement.databinding.ActivityStudentManagementBinding

class StudentManagementActivity : AppCompatActivity() {
    val binding:ActivityStudentManagementBinding by lazy { ActivityStudentManagementBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



    }
}