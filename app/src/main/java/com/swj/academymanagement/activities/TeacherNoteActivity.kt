package com.swj.academymanagement.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.swj.academymanagement.R
import com.swj.academymanagement.databinding.ActivityTeacherNoteBinding
import com.swj.academymanagement.fragments.TeacherNoteFragment
import com.swj.academymanagement.fragments.TeacherNoteListFragment
import com.swj.academymanagement.fragments.TeacherNoteWorkFragment
import com.swj.academymanagement.model.Member

class TeacherNoteActivity : AppCompatActivity() {

    val binding:ActivityTeacherNoteBinding by lazy { ActivityTeacherNoteBinding.inflate(layoutInflater) }
    private val fragments = arrayOfNulls<Fragment>(3)
    var teacher:Member? = null
    var position = -1

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
        teacher = Gson().fromJson(intent.getStringExtra("teacher"), Member::class.java)
        position = intent.getIntExtra("position", -1)

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

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(InputMethodManager::class.java)
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        currentFocus?.clearFocus()
        return super.dispatchTouchEvent(ev)
    }
}