package com.example.mynotes

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mynotes.data.AppDatabase
import com.example.mynotes.data.Note
import com.example.mynotes.data.NoteRepository
import kotlinx.coroutines.launch
import kotlin.random.Random

class AddNoteActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var btnSave: Button
    private lateinit var repository: NoteRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        etTitle = findViewById(R.id.et_note_title)
        etContent = findViewById(R.id.et_note_content)
        btnSave = findViewById(R.id.btn_save_note)

        val database = AppDatabase.getDatabase(this)
        repository = NoteRepository(database.noteDao())

        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val content = etContent.text.toString().trim()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val randomColor = Color.rgb(
                    128 + Random.nextInt(128),
                    128 + Random.nextInt(128),
                    128 + Random.nextInt(128)
                )
                
                val note = Note(
                    title = title,
                    content = content,
                    timestamp = System.currentTimeMillis(),
                    cardColor = randomColor
                )
                
                lifecycleScope.launch {
                    repository.insert(note)
                    Toast.makeText(this@AddNoteActivity, "Заметка сохранена", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
