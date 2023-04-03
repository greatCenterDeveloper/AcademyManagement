package com.swj.academymanagement.activities

import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.swj.academymanagement.adapters.SmsImageAdapter
import com.swj.academymanagement.databinding.ActivitySmsSendBinding


class SmsSendActivity : AppCompatActivity() {

    val binding:ActivitySmsSendBinding by lazy { ActivitySmsSendBinding.inflate(layoutInflater) }
    val images:MutableList<Uri> = mutableListOf()
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

        binding.ivBackspace.setOnClickListener { finish() }
        binding.pager.adapter = SmsImageAdapter(this, images)
        binding.dotsIndicator.attachTo(binding.pager)
        binding.btnSelectImage.setOnClickListener { selectImage() }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            .setType("image/*")
            .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        imagePickMultipleLauncher.launch(intent)
    }

    private val imagePickMultipleLauncher:ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
                if(it.resultCode != RESULT_CANCELED) {
                    val intent = it.data
                    val clipData:ClipData = intent?.clipData!!
                    images.clear()
                    for(i in 0 until clipData.itemCount)
                        images.add(clipData.getItemAt(i).uri)
                    binding.pager.adapter!!.notifyDataSetChanged()
                }
            }
}