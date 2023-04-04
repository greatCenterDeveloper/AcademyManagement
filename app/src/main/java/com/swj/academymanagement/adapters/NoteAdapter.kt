package com.swj.academymanagement.adapters

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.swj.academymanagement.R
import com.swj.academymanagement.databinding.RecyclerItemTeacherNoteBinding
import com.swj.academymanagement.model.Note

class NoteAdapter(val context: Context, val noteArr:MutableList<Note>)
    :Adapter<NoteAdapter.VH>() {
    inner class VH(val binding:RecyclerItemTeacherNoteBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
        = VH(RecyclerItemTeacherNoteBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = noteArr.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val teacherNote = noteArr[position]
        holder.binding.tvKind.text = teacherNote.kind
        holder.binding.tvTitle.text = teacherNote.title
        holder.binding.tvDate.text = teacherNote.date
        holder.binding.tvContent.text = teacherNote.content

        holder.binding.root.setOnClickListener {
            val popMenu = PopupMenu(context, holder.binding.root)
            popMenu.menuInflater.inflate(R.menu.note_menu, popMenu.menu)
            popMenu.show()
            popMenu.setOnMenuItemClickListener {
                if(it.itemId == R.id.menu_note_update) {

                }else if(it.itemId == R.id.menu_note_delete) {
                    AlertDialog.Builder(context)
                        .setMessage("삭제하시겠습니까?")
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                            noteArr.remove(teacherNote)
                            this.notifyItemRemoved(position)
                            // SQLite delete..
                        })
                        .setNegativeButton("NO", null)
                        .show()
                }
                false
            }
        }
    }

}