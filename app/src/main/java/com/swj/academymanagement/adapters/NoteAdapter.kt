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
import com.swj.academymanagement.G
import com.swj.academymanagement.R
import com.swj.academymanagement.activities.ClassNoteActivity
import com.swj.academymanagement.activities.ClassNoteDetailActivity
import com.swj.academymanagement.activities.TeacherNoteActivity
import com.swj.academymanagement.activities.TeacherNoteDetailActivity
import com.swj.academymanagement.databinding.RecyclerItemNoteBinding
import com.swj.academymanagement.model.Note

class NoteAdapter(val context: Context, val db:SQLiteDatabase, val noteArr:MutableList<Note>)
    :Adapter<NoteAdapter.VH>() {
    inner class VH(val binding:RecyclerItemNoteBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemNoteBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = noteArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val note = noteArr[position]
        holder.binding.tvKind.text = note.kind
        holder.binding.tvTitle.text = note.title
        holder.binding.tvDate.text = note.date
        holder.binding.tvContent.text = note.content

        G.noteRemove = false

        holder.binding.root.setOnClickListener {
            val popMenu = PopupMenu(context, holder.binding.root)
            popMenu.menuInflater.inflate(R.menu.note_menu, popMenu.menu)
            popMenu.show()
            popMenu.setOnMenuItemClickListener {
                if(it.itemId == R.id.menu_note_update) {
                    // 수정 화면으로 이동
                    if(note.authority == "teacher") {
                        val intent = Intent(context, TeacherNoteDetailActivity::class.java)
                        intent.putExtra("note", Gson().toJson(note))
                        intent.putExtra("position", position)
                        val options: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                context as TeacherNoteActivity, Pair(holder.binding.tvContent, "note")
                            )
                        context.startActivity(intent, options.toBundle())
                    } else if(note.authority == "student") {
                        val intent = Intent(context, ClassNoteDetailActivity::class.java)
                        intent.putExtra("note", Gson().toJson(note))
                        intent.putExtra("position", position)
                        val options: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                context as ClassNoteActivity, Pair(holder.binding.tvContent, "note")
                            )
                        context.startActivity(intent, options.toBundle())
                    }
                }else if(it.itemId == R.id.menu_note_delete) {
                    AlertDialog.Builder(context)
                        .setMessage("삭제하시겠습니까?")
                        .setPositiveButton("OK") { dialogInterface, i ->
                            //Toast.makeText(context, "${note.authority} 권한에서 실행", Toast.LENGTH_SHORT).show()
                            // SQLite delete.. 권한에 따라 학생의 노트 테이블에서 삭제할지, 선생님의 노트 테이블에서 삭제할지.

                            if(note.authority == "teacher")
                                db.execSQL("DELETE FROM teacher_note WHERE num=?", arrayOf(note.num))
                            else if(note.authority == "student")
                                db.execSQL("DELETE FROM student_note WHERE num=?", arrayOf(note.num))

                            noteArr.remove(note)
                            G.noteRemove = true
                            G.position = position
                        }
                        .setNegativeButton("NO", null)
                        .show()
                }
                false
            }
        }
    }

}