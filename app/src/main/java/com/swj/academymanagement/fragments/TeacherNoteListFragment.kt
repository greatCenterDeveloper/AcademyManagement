package com.swj.academymanagement.fragments

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.swj.academymanagement.G
import com.swj.academymanagement.activities.TeacherNoteActivity
import com.swj.academymanagement.adapters.NoteAdapter
import com.swj.academymanagement.databinding.FragmentTeacherNoteListBinding
import com.swj.academymanagement.model.Note

// 선생님 권한 노트 리스트 Fragment
class TeacherNoteListFragment : Fragment() {

    lateinit var binding:FragmentTeacherNoteListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTeacherNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 선생님 노트 Activity 객체
        val tna = activity as TeacherNoteActivity

        // 노트 제목 검색 (제목 포함)
        binding.btnSearch.setOnClickListener {
            // 검색하려는 노트 제목
            val searchTitle = binding.tilTitle.editText?.text.toString()

            // 번거롭게 입력한 제목을 지울 필요 없이.. 여기서 지우기
            binding.tilTitle.editText?.setText("")

            // 제목으로 검색하여 선생님 강의 노트 리스트 가져오기
            // 가장 최근에 작성된 내용을 가장 위에 보이게 하기 위해서 작성일 기준 내림차순
            val cursor:Cursor = tna.db.rawQuery(
                "SELECT * FROM teacher_note WHERE title LIKE ? ORDER BY registration DESC",
                arrayOf("%${searchTitle}%"))

            val cnt = cursor.count // 총 줄(row: 레코드) 수
            if(cnt == 0) { // 검색 결과가 없다면..
                Toast.makeText(tna, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()

                // 검색 결과가 없으므로 화면에 아무 것도 안 보여주기 위해서..
                val noteArr:MutableList<Note> = mutableListOf()
                binding.recycler.adapter = NoteAdapter(tna, tna.db, noteArr)
            } else {
                cursor.moveToFirst()
                val noteArr:MutableList<Note> = mutableListOf()
                for(i in 0 until cnt) {
                    val num = cursor.getInt(0)                  // 노트 테이블의 num ( auto_increment -> primary key ) -> 수정, 삭제 시 필요
                    val kind = cursor.getString(1)              // 노트 종류 ( 할일, 노트 )
                    val title = cursor.getString(2)             // 노트 제목
                    val content = cursor.getString(3)           // 노트 내용
                    val registration = cursor.getString(4)      // 작성일

                    // 노트 어댑터에서 노트 수정 / 삭제 시 권한을 보고 선생님 노트 테이블에서 작업할 것인지, 학생 수업 노트 테이블에서 작업할 것인지 결정
                    val authority = G.member.authority

                    // 노트 리스트에 추가
                    noteArr.add(Note(num, kind, title, registration, content, authority))
                    cursor.moveToNext()
                }
                binding.recycler.adapter = NoteAdapter(tna, tna.db, noteArr)
            }
        }

        // 선생님 강의 노트 리스트 가져오기
        // 가장 최근에 작성된 내용을 가장 위에 보이게 하기 위해서 작성일 기준 내림차순
        val cursor:Cursor = tna.db.rawQuery("SELECT * FROM teacher_note ORDER BY registration DESC", null)
        val cnt = cursor.count // 총 줄(row: 레코드) 수

        if(cnt == 0) { // 작성된 노트가 없다면..
            // 노트 RecyclerView 숨기기
            binding.recycler.visibility = View.GONE

            // 작성된 노트가 없다는 문구 보이기
            binding.tvNoList.visibility = View.VISIBLE
        } else {
            cursor.moveToFirst()
            val noteArr:MutableList<Note> = mutableListOf()
            for(i in 0 until cnt) {
                val num = cursor.getInt(0)                  // 노트 테이블의 num ( auto_increment -> primary key ) -> 수정, 삭제 시 필요
                val kind = cursor.getString(1)              // 노트 종류 ( 할일, 노트 )
                val title = cursor.getString(2)             // 노트 제목
                val content = cursor.getString(3)           // 노트 내용
                val registration = cursor.getString(4)      // 작성일

                // 노트 어댑터에서 노트 수정 / 삭제 시 권한을 보고 선생님 노트 테이블에서 작업할 것인지, 학생 수업 노트 테이블에서 작업할 것인지 결정
                val authority = G.member.authority

                // 노트 리스트에 추가
                noteArr.add(Note(num, kind, title, registration, content, authority))
                cursor.moveToNext()
            }
            binding.recycler.adapter = NoteAdapter(tna, tna.db, noteArr)
        }
    }
}