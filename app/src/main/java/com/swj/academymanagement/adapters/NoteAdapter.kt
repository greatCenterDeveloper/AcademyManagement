package com.swj.academymanagement.adapters

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.gson.Gson
import com.swj.academymanagement.R
import com.swj.academymanagement.activities.ClassNoteActivity
import com.swj.academymanagement.activities.ClassNoteDetailActivity
import com.swj.academymanagement.activities.TeacherNoteActivity
import com.swj.academymanagement.activities.TeacherNoteDetailActivity
import com.swj.academymanagement.databinding.RecyclerItemNoteBinding
import com.swj.academymanagement.model.Note

// 선생님 / 학생 권한  선생님 노트 / 학생 수업 노트 RecyclerView 어댑터
class NoteAdapter(val context: Context, val db:SQLiteDatabase, val noteArr:MutableList<Note>)
    :Adapter<NoteAdapter.VH>() {
    inner class VH(val binding:RecyclerItemNoteBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemNoteBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = noteArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val note = noteArr[position]

        // 종류 : ( 할일 / 노트 )
        holder.binding.tvKind.text = note.kind

        // 노트 제목
        holder.binding.tvTitle.text = note.title

        // 노트 작성일
        holder.binding.tvDate.text = note.date

        // 노트 작성 내용
        holder.binding.tvContent.text = note.content

        // 노트 요소 ( 수정 / 삭제 ) PopupMenu
        holder.binding.root.setOnClickListener {
            val popMenu = PopupMenu(context, holder.binding.root)
            popMenu.menuInflater.inflate(R.menu.menu_options, popMenu.menu)
            popMenu.show()
            popMenu.setOnMenuItemClickListener {
                if(it.itemId == R.id.menu_update) {  // 노트 수정
                    if(note.authority == "teacher") { // 권한이 선생님이라면 선생님 노트 수정 화면으로 이동

                        val intent = Intent(context, TeacherNoteDetailActivity::class.java)

                        // 노트 수정 화면에 가져갈 노트 정보
                        intent.putExtra("note", Gson().toJson(note))

                        // 노트 수정 화면으로 화면 전환 효과
                        val options: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                context as TeacherNoteActivity, Pair(holder.binding.tvContent, "teacherNote")
                            )
                        context.startActivity(intent, options.toBundle())

                    } else if(note.authority == "student") { // 권한이 학생이라면 학생 수업 노트 수정 화면으로 이동

                        val intent = Intent(context, ClassNoteDetailActivity::class.java)

                        // 노트 수정 화면에 가져갈 노트 정보
                        intent.putExtra("note", Gson().toJson(note))

                        // 노트 수정 화면으로 화면 전환 효과
                        val options: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                context as ClassNoteActivity, Pair(holder.binding.tvContent, "studentNote")
                            )
                        context.startActivity(intent, options.toBundle())

                    }
                }else if(it.itemId == R.id.menu_delete) {   // 노트 삭제
                    AlertDialog.Builder(context)
                        .setMessage("삭제하시겠습니까?")
                        .setNegativeButton("NO", null)
                        .setPositiveButton("OK") { dialogInterface, i ->
                            //Toast.makeText(context, "${note.authority} 권한에서 실행", Toast.LENGTH_SHORT).show()
                            // SQLite delete.. 권한에 따라 학생의 노트 테이블에서 삭제할지, 선생님의 노트 테이블에서 삭제할지.

                            if(note.authority == "teacher") // 권한이 선생님이라면 선생님 노트 테이블에서 삭제
                                db.execSQL("DELETE FROM teacher_note WHERE num=?", arrayOf(note.num))
                            else if(note.authority == "student") // 권한이 학생이라면 학생 수업 노트 테이블에서 삭제
                                db.execSQL("DELETE FROM student_note WHERE num=?", arrayOf(note.num))

                            noteArr.remove(note)
                            this.notifyItemRemoved(position)

                            Toast.makeText(context, "삭제 완료", Toast.LENGTH_SHORT).show()
                        }.show()
                }
                false
            }
        }
    }

}