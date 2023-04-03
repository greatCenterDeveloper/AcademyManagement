package com.swj.academymanagement.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.swj.academymanagement.databinding.FragmentTeacherNoteWorkBinding
import com.swj.academymanagement.model.Note
import java.text.SimpleDateFormat
import java.util.Date

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

        binding.btnSave.setOnClickListener {
            val kind:String = "노트"
            val title:String = binding.tilWorkTitle.editText!!.text.toString()
            val content:String = binding.tilCounselContent.editText!!.text.toString()
            val sdf = SimpleDateFormat("yyyy/MM/dd")
            val date = sdf.parse(Date().toString()).toString()
            val note = Note(kind, title, date, content)
            Toast.makeText(requireActivity(), "${date}", Toast.LENGTH_SHORT).show()
        }
    }
}