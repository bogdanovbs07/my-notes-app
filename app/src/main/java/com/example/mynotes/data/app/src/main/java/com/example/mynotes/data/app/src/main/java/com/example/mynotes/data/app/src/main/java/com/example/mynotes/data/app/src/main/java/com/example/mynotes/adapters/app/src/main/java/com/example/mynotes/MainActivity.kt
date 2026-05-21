package com.example.mynotes

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.adapters.NoteAdapter
import com.example.mynotes.data.AppDatabase
import com.example.mynotes.data.NoteRepository
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var repository: NoteRepository
    private lateinit var mainLayout: LinearLayout
    private lateinit var btnChangeBgColor: Button
    private lateinit var btnChangeButtonColor: Button
    private lateinit var etInputText: EditText
    private lateinit var btnPrintToConsole: Button
    private lateinit var btnAddNote: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = AppDatabase.getDatabase(this)
        repository = NoteRepository(database.noteDao())

        recyclerView = findViewById(R.id.rv_notes)
        mainLayout = findViewById(R.id.main_layout)
        btnChangeBgColor = findViewById(R.id.btn_change_bg)
        btnChangeButtonColor = findViewById(R.id.btn_change_btn_color)
        etInputText = findViewById(R.id.et_input_text)
        btnPrintToConsole = findViewById(R.id.btn_print_console)
        btnAddNote = findViewById(R.id.btn_add_note)

        recyclerView.layoutManager = LinearLayoutManager(this)

        loadNotes()

        btnChangeBgColor.setOnClickListener {
            val randomColor = Color.rgb(
                Random.nextInt(256),
                Random.nextInt(256),
                Random.nextInt(256)
            )
            mainLayout.setBackgroundColor(randomColor)
        }

        btnChangeButtonColor.setOnClickListener {
            val newColor = Color.rgb(
                Random.nextInt(256),
                Random.nextInt(256),
                Random.nextInt(256)
            )
            btnChangeButtonColor.setBackgroundColor(newColor)
            btnChangeBgColor.setBackgroundColor(newColor)
            btnPrintToConsole.setBackgroundColor(newColor)
            btnAddNote.setBackgroundColor(newColor)
        }

        btnPrintToConsole.setOnClickListener {
            val inputText = etInputText.text.toString()
            if (inputText.isNotEmpty()) {
                println("Введённый текст: $inputText")
                android.util.Log.d("MyNotes", "Введённый текст: $inputText")
                Toast.makeText(this, "Текст выведен в консоль", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Введите текст", Toast.LENGTH_SHORT).show()
            }
        }

        btnAddNote.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadNotes()
    }

    private fun loadNotes() {
        lifecycleScope.launch {
            val notes = repository.getAllNotes()
            val adapter = NoteAdapter(notes) { note ->
                val intent = Intent(this@MainActivity, NoteDetailActivity::class.java)
                intent.putExtra("note_id", note.id)
                intent.putExtra("note_title", note.title)
                intent.putExtra("note_content", note.content)
                intent.putExtra("note_timestamp", note.timestamp)
                startActivity(intent)
            }
            recyclerView.adapter = adapter
        }
    }
}
