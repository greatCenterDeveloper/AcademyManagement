package com.swj.academymanagement.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
        val position = (activity as ClassNoteActivity).position

        val noteArr:MutableList<Note> = mutableListOf()
        noteArr.add(Note("할일", "검색", "2023/03/24", "구글 국어 검색", student.authority))
        noteArr.add(Note("노트", "국어 동영상", "2023/03/25", "유튜브 국어 검색", student.authority))
        noteArr.add(Note("할일", "국어 문제집", "2023/03/26", "133P 풀기", student.authority))
        binding.recycler.adapter = NoteAdapter(requireActivity(), noteArr)

        // 포지션이 0보다 크다면 ClassNoteDetailActivity에서 해당 position번째 노트 내용이 수정된 것이므로.
        //binding.recycler.adapter?.notifyItemChanged(position)
    }
}