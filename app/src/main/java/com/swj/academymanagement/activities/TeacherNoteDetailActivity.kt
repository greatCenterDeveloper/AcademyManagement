package com.swj.academymanagement.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.google.gson.Gson
import com.swj.academymanagement.databinding.ActivityTeacherNoteDetailBinding
import com.swj.academymanagement.model.Note

class TeacherNoteDetailActivity : AppCompatActivity() {

    val binding:ActivityTeacherNoteDetailBinding by lazy {
        ActivityTeacherNoteDetailBinding.inflate(layoutInflater)
    }

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

        val note = Gson().fromJson(intent.getStringExtra("note"), Note::class.java)
        val position = intent.getIntExtra("position", 0)
        binding.tilContent.editText?.transitionName = "note"

        binding.tvKind.text = "${note.kind} 내용 작성"
        binding.tilTitle.editText?.setText(note.title)
        binding.tilContent.editText?.setText(note.content)

        binding.btnSave.setOnClickListener {
            val kind:String = note.kind
            val title:String = binding.tilTitle.editText?.text.toString()
            val content:String = binding.tilContent.editText?.text.toString()
            // SQLite 수정 작업 후 성공적으로 수정되면..
            //val intent = Intent(this, TeacherNoteActivity::class.java)
            //intent.putExtra("position", position)
            //startActivity(intent)
            //finish()
        }
    }
}