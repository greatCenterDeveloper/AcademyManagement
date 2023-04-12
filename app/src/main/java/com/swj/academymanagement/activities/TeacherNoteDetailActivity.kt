package com.swj.academymanagement.activities

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import com.swj.academymanagement.databinding.ActivityTeacherNoteDetailBinding
import com.swj.academymanagement.model.Note

// 선생님 권한 노트 내용 수정 화면
class TeacherNoteDetailActivity : AppCompatActivity() {

    val binding:ActivityTeacherNoteDetailBinding by lazy {
        ActivityTeacherNoteDetailBinding.inflate(layoutInflater)
    }

    // 노트 디비 객체
    val db: SQLiteDatabase by lazy { openOrCreateDatabase("note.db", MODE_PRIVATE, null) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 화면 전체 다 먹기
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        // 뒤로 가기
        binding.ivBackspace.setOnClickListener { finish() }

        // 노트 리스트에서 노트 수정 화면으로 오기 위해 기존에 노트에 입력된 내용 가져오기
        val note = Gson().fromJson(intent.getStringExtra("note"), Note::class.java)

        // 선생님 노트 리스트에서 노트 수정 화면으로 이동시 화면 전환 효과
        binding.tilContent.editText?.transitionName = "teacherNote"

        // 노트 종류 ( 할일 / 노트 )
        binding.tvKind.text = "${note.kind} 내용 작성"

        // 노트 제목
        binding.tilTitle.editText?.setText(note.title)

        // 노트 내용
        binding.tilContent.editText?.setText(note.content)

        // 수정한 내용 저장 버튼
        binding.btnSave.setOnClickListener {
            // 선생님 노트 테이블의 num ( auto_increment -> primary key )
            val num:Int = note.num

            // 노트 종류 ( 할일 / 노트 )
            val kind:String = note.kind

            // 노트 제목
            val title:String = binding.tilTitle.editText?.text.toString()

            // 노트 내용
            val content:String = binding.tilContent.editText?.text.toString()

            // 수정된 내용 선생님 노트 테이블에 적용
            db.execSQL("UPDATE teacher_note SET title=?, content=? WHERE num=?", arrayOf(title, content, num))
            startActivity(Intent(this, TeacherNoteActivity::class.java))
            finish()
        }
    }

    // 바깥 화면 터치 시 소프트 키보드 숨기기
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(InputMethodManager::class.java)
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        currentFocus?.clearFocus()
        return super.dispatchTouchEvent(ev)
    }
}