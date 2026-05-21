package com.example.mynotes.data

class NoteRepository(private val noteDao: NoteDao) {
    suspend fun getAllNotes(): List<Note> = noteDao.getAllNotes()
    suspend fun insert(note: Note): Long = noteDao.insert(note)
    suspend fun delete(note: Note) = noteDao.delete(note)
}
