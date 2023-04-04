package com.swj.academymanagement.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.swj.academymanagement.activities.ClassNoteActivity
import com.swj.academymanagement.databinding.FragmentClassNoteWorkBinding
import com.swj.academymanagement.model.Member
import com.swj.academymanagement.model.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class ClassNoteWorkFragment : Fragment() {
    lateinit var binding:FragmentClassNoteWorkBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClassNoteWorkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val student: Member = (activity as ClassNoteActivity).student!!

        binding.btnSave.setOnClickListener {
            val kind:String = "노트"
            val title:String = binding.tilTitle.editText!!.text.toString()
            val content:String = binding.tilContent.editText!!.text.toString()
            val sdf = SimpleDateFormat("yyyy/MM/dd")
            sdf.timeZone = TimeZone.getTimeZone("Asia/Seoul")

            val date = sdf.format(Date())
            val note = Note(kind, title, date, content, student.authority)
            Toast.makeText(requireActivity(), "${date}", Toast.LENGTH_SHORT).show()
        }
    }
}