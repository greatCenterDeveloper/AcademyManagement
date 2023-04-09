package com.swj.academymanagement.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.swj.academymanagement.activities.TeacherNoteActivity
import com.swj.academymanagement.databinding.FragmentTeacherNoteWorkBinding
import com.swj.academymanagement.model.Member
import com.swj.academymanagement.model.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class TeacherNoteWorkFragment : Fragment() {
    lateinit var binding:FragmentTeacherNoteWorkBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTeacherNoteWorkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val teacher: Member = (activity as TeacherNoteActivity).teacher!!
        val tna = activity as TeacherNoteActivity

        binding.btnSave.setOnClickListener {
            val kind:String = "할일"
            val title:String = binding.tilTitle.editText!!.text.toString()
            val content:String = binding.tilContent.editText!!.text.toString()
            val sdf = SimpleDateFormat("yyyy/MM/dd")
            sdf.timeZone = TimeZone.getTimeZone("Asia/Seoul")
            val date = sdf.format(Date())

            tna.db.execSQL("INSERT INTO teacher_note(kind, title, content, registration) " +
                    "VALUES (?,?,?,?)", arrayOf(kind, title, content, date))
            tna.changeFragmentList()
        }
    }
}