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
import com.swj.academymanagement.databinding.ActivityTeacherNoteBinding
import com.swj.academymanagement.fragments.TeacherNoteFragment
import com.swj.academymanagement.fragments.TeacherNoteListFragment
import com.swj.academymanagement.fragments.TeacherNoteWorkFragment

class TeacherNoteActivity : AppCompatActivity() {

    val binding:ActivityTeacherNoteBinding by lazy { ActivityTeacherNoteBinding.inflate(layoutInflater) }
    private val fragments = arrayOfNulls<Fragment>(3)
    //var teacher:Member? = null
    val db:SQLiteDatabase by lazy { openOrCreateDatabase("note.db", MODE_PRIVATE, null) }

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
        //teacher = Gson().fromJson(intent.getStringExtra("teacher"), Member::class.java)

        db.execSQL("CREATE TABLE IF NOT EXISTS teacher_note(num INTEGER PRIMARY KEY AUTOINCREMENT," +
                  "kind VARCHAR(20) NOT NULL, " +
                  "title VARCHAR(200) NOT NULL, " +
                  "content TEXT NOT NULL, " +
                  "registration DATE NOT NULL)")

        fragments[0] = TeacherNoteListFragment()
        fragments[1] = TeacherNoteWorkFragment()
        fragments[2] = TeacherNoteFragment()

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

    fun changeFragmentList() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_fragment, fragments[0]!!)
            .commit()
        binding.bnv.selectedItemId = R.id.bnv_list
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(InputMethodManager::class.java)
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        currentFocus?.clearFocus()
        return super.dispatchTouchEvent(ev)
    }
}