package com.swj.academymanagement.activities

import android.database.sqlite.SQLiteDatabase
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.swj.academymanagement.R
import com.swj.academymanagement.databinding.ActivityClassNoteBinding
import com.swj.academymanagement.fragments.ClassNoteFragment
import com.swj.academymanagement.fragments.ClassNoteListFragment
import com.swj.academymanagement.fragments.ClassNoteWorkFragment

// 학생 권한 학생 수업 노트 화면
class ClassNoteActivity : AppCompatActivity() {

    val binding:ActivityClassNoteBinding by lazy { ActivityClassNoteBinding.inflate(layoutInflater) }

    // BottomNavigationView 로 이동할 Fragment
    private val fragments = arrayOfNulls<Fragment>(3)

    // 노트 디비 객체
    val db:SQLiteDatabase by lazy { openOrCreateDatabase("note.db", MODE_PRIVATE, null) }

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

        // 학생 수업 노트 테이블 생성
        db.execSQL("CREATE TABLE IF NOT EXISTS student_note(num INTEGER PRIMARY KEY AUTOINCREMENT," +
                "kind VARCHAR(20) NOT NULL, " +
                "title VARCHAR(200) NOT NULL, " +
                "content TEXT NOT NULL, " +
                "registration DATE NOT NULL)")

        // 수업 노트 리스트
        fragments[0] = ClassNoteListFragment()

        // 노트 할일 작성
        fragments[1] = ClassNoteWorkFragment()

        // 공부 노트 작성
        fragments[2] = ClassNoteFragment()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.container_fragment, fragments[0]!!)
            .commit()

        binding.bnv.setOnItemSelectedListener {
            if(it.itemId == R.id.bnv_list) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container_fragment, fragments[0]!!)
                    .commit()
            } else if(it.itemId == R.id.bnv_work) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container_fragment, fragments[1]!!)
                    .commit()
            } else if(it.itemId == R.id.bnv_note) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container_fragment, fragments[2]!!)
                    .commit()
            }
            true
        }
    }

    // 할일 / 공부 노트 작성 후 리스트 화면으로 이동
    fun changeFragmentList() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_fragment, fragments[0]!!)
            .commit()
        binding.bnv.selectedItemId = R.id.bnv_list
    }

    // 바깥 화면 터치 시 소프트 키보드 숨기기
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(InputMethodManager::class.java)
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        currentFocus?.clearFocus()
        return super.dispatchTouchEvent(ev)
    }
}