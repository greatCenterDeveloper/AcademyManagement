package com.swj.academymanagement.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
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
    }

}