package com.example.mynotes

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mynotes.data.AppDatabase
import com.example.mynotes.data.NoteRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvContent: TextView
    private lateinit var tvTimestamp: TextView
    private lateinit var btnDelete: Button
    private lateinit var repository: NoteRepository
    
    private var noteId: Long = -1
    private var currentNote: com.example.mynotes.data.Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        tvTitle = findViewById(R.id.tv_detail_title)
        tvContent = findViewById(R.id.tv_detail_content)
        tvTimestamp = findViewById(R.id.tv_detail_timestamp)
        btnDelete = findViewById(R.id.btn_delete_note)

        val database = AppDatabase.getDatabase(this)
        repository = NoteRepository(database.noteDao())

        noteId = intent.getLongExtra("note_id", -1)
        val title = intent.getStringExtra("note_title") ?: ""
        val content = intent.getStringExtra("note_content") ?: ""
        val timestamp = intent.getLongExtra("note_timestamp", 0)

        // ВЫВОД ID ЗАМЕТКИ В КОНСОЛЬ
        Log.d("NoteDetail", "=== ВТОРОЙ ЭКРАН ===")
        Log.d("NoteDetail", "ID заметки: $noteId")
        Log.d("NoteDetail", "Заголовок: $title")
        println("ID заметки на втором экране: $noteId")

        tvTitle.text = title
        tvContent.text = content
        
        if (timestamp > 0) {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
            tvTimestamp.text = "Создано: ${dateFormat.format(Date(timestamp))}"
        }

        lifecycleScope.launch {
            val allNotes = repository.getAllNotes()
            currentNote = allNotes.find { it.id == noteId }
        }

        btnDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Удаление заметки")
            .setMessage("Вы уверены, что хотите удалить заметку \"${tvTitle.text}\"?")
            .setPositiveButton("Удалить") { _, _ ->
                deleteNote()
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteNote() {
        lifecycleScope.launch {
            currentNote?.let { note ->
                repository.delete(note)
                Toast.makeText(this@NoteDetailActivity, "Заметка удалена", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
