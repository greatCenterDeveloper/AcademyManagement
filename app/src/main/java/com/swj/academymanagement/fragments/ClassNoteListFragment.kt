package com.swj.academymanagement.fragments

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.swj.academymanagement.G
import com.swj.academymanagement.activities.ClassNoteActivity
import com.swj.academymanagement.adapters.NoteAdapter
import com.swj.academymanagement.databinding.FragmentClassNoteListBinding
import com.swj.academymanagement.model.Member
import com.swj.academymanagement.model.Note

class ClassNoteListFragment : Fragment() {

    lateinit var binding:FragmentClassNoteListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClassNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val student: Member = (activity as ClassNoteActivity).student!!

        /*val noteArr:MutableList<Note> = mutableListOf()
        noteArr.add(Note("할일", "검색", "2023/03/24", "구글 국어 검색", student.authority))
        noteArr.add(Note("노트", "국어 동영상", "2023/03/25", "유튜브 국어 검색", student.authority))
        noteArr.add(Note("할일", "국어 문제집", "2023/03/26", "133P 풀기", student.authority))
        binding.recycler.adapter = NoteAdapter(requireActivity(), noteArr)*/

        val cna = activity as ClassNoteActivity

        val cursor: Cursor = cna.db.rawQuery("SELECT * FROM student_note", null)
        val cnt = cursor.count // 총 줄(row: 레코드) 수
        if(cnt == 0) {
            binding.recycler.visibility = View.GONE
            binding.tvNoList.visibility = View.VISIBLE
        } else {
            cursor.moveToFirst()
            val noteArr:MutableList<Note> = mutableListOf()
            for(i in 0 until cnt) {
                val num = cursor.getInt(0)
                val kind = cursor.getString(1)
                val title = cursor.getString(2)
                val content = cursor.getString(3)
                val registration = cursor.getString(4)
                val authority = cna.student?.authority ?: "teacher"
                noteArr.add(Note(num, kind, title, registration, content, authority))
                cursor.moveToNext()
            }

            noteArr.reverse()
            binding.recycler.adapter = NoteAdapter(cna, cna.db, noteArr)
        }
    }
}