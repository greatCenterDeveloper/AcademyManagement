package com.swj.academymanagement.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.swj.academymanagement.activities.TeacherNoteActivity
import com.swj.academymanagement.databinding.FragmentTeacherNoteBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

// 선생님 권한 강의 노트 작성 Fragment
class TeacherNoteFragment : Fragment() {

    lateinit var binding:FragmentTeacherNoteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTeacherNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 선생님 노트 Activity 객체
        val tna = activity as TeacherNoteActivity

        binding.btnSave.setOnClickListener {
            // 노트 종류 ( 할일, 노트 )
            val kind:String = "노트"

            // 노트 제목
            val title:String = binding.tilTitle.editText!!.text.toString()

            // 노트 내용
            val content:String = binding.tilContent.editText!!.text.toString()

            // 현재 시간 가져오기
            val sdf = SimpleDateFormat("yyyy/MM/dd")
            sdf.timeZone = TimeZone.getTimeZone("Asia/Seoul")

            // 노트 작성 날짜
            val date = sdf.format(Date())

            // 노트 작성 내용 선생님 노트 테이블에 저장
            tna.db.execSQL("INSERT INTO teacher_note(kind, title, content, registration) " +
                            "VALUES (?,?,?,?)", arrayOf(kind, title, content, date))

            // 저장 완료 후 노트 리스트 Fragment 이동
            tna.changeFragmentList()
        }
    }
}