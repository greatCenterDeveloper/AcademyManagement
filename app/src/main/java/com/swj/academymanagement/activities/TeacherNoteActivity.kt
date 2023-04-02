package com.swj.academymanagement.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import com.swj.academymanagement.R
import com.swj.academymanagement.databinding.ActivityTeacherNoteBinding
import com.swj.academymanagement.fragments.TeacherNoteFragment
import com.swj.academymanagement.fragments.TeacherNoteListFragment
import com.swj.academymanagement.fragments.TeacherNoteWorkFragment

class TeacherNoteActivity : AppCompatActivity() {

    val binding:ActivityTeacherNoteBinding by lazy { ActivityTeacherNoteBinding.inflate(layoutInflater) }
    private val fragments = arrayOfNulls<Fragment>(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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
}