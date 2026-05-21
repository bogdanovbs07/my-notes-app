package com.example.mynotes.adapters

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.R
import com.example.mynotes.data.Note
import java.util.Date

class NoteAdapter(
    private val notes: List<Note>,
    private val onItemClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.tv_note_title)
        val contentPreviewTextView: TextView = itemView.findViewById(R.id.tv_note_preview)
        val dateTextView: TextView = itemView.findViewById(R.id.tv_note_date)
        val cardView: CardView = itemView.findViewById(R.id.cv_note_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.contentPreviewTextView.text = note.content.take(50) + if (note.content.length > 50) "..." else ""
        
        val formattedDate = DateFormat.format("dd.MM.yyyy HH:mm", Date(note.timestamp))
        holder.dateTextView.text = formattedDate
        
        holder.cardView.setCardBackgroundColor(note.cardColor)
        
        holder.itemView.setOnClickListener { onItemClick(note) }
    }

    override fun getItemCount(): Int = notes.size
}
