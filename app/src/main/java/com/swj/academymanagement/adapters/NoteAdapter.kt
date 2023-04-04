package com.swj.academymanagement.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.swj.academymanagement.R
import com.swj.academymanagement.activities.CounselActivity
import com.swj.academymanagement.activities.TeacherNoteActivity
import com.swj.academymanagement.databinding.RecyclerItemTeacherNoteBinding
import com.swj.academymanagement.model.Note

class NoteAdapter(val context: Context, val noteArr:MutableList<Note>)
    :Adapter<NoteAdapter.VH>() {
    inner class VH(val binding:RecyclerItemTeacherNoteBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemTeacherNoteBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = noteArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val note = noteArr[position]
        holder.binding.tvKind.text = note.kind
        holder.binding.tvTitle.text = note.title
        holder.binding.tvDate.text = note.date
        holder.binding.tvContent.text = note.content

        holder.binding.root.setOnClickListener {
            val popMenu = PopupMenu(context, holder.binding.root)
            popMenu.menuInflater.inflate(R.menu.note_menu, popMenu.menu)
            popMenu.show()
            popMenu.setOnMenuItemClickListener {
                if(it.itemId == R.id.menu_note_update) {
                    // 수정 화면으로 이동
                    if(note.authority == "선생님") {
                        //val intent = Intent(context, ::class.java)
                        val options: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                context as CounselActivity, Pair(holder.binding.tvContent, "teacherNote")
                            )
                        //context.startActivity(intent, options.toBundle())
                    } else if(note.authority == "학생") {

                    }
                }else if(it.itemId == R.id.menu_note_delete) {
                    AlertDialog.Builder(context)
                        .setMessage("삭제하시겠습니까?")
                        .setPositiveButton("OK") { dialogInterface, i ->
                            Toast.makeText(context, "${note.authority} 권한에서 실행", Toast.LENGTH_SHORT).show()
                            // SQLite delete.. 권한에 따라 학생의 노트 테이블에서 삭제할지, 선생님의 노트 테이블에서 삭제할지.
                            //noteArr.remove(note)
                            //this.notifyItemRemoved(position)
                        }
                        .setNegativeButton("NO", null)
                        .show()
                }
                false
            }
        }
    }

}