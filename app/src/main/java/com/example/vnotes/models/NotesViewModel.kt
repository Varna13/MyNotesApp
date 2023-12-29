package com.example.vnotes.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.vnotes.database.NoteDatabase
import com.example.vnotes.database.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(application: Application):AndroidViewModel(application) {

    val allNotes: LiveData<List<Note>>
    private val repository: NotesRepository

    init {
        val dao = NoteDatabase.getDatabase(application).getNoteDao()
        repository = NotesRepository(dao)
        allNotes = repository.allNotes
    }

    fun insert(note: Note){
        viewModelScope.launch(Dispatchers.IO){
            repository.insert(note)
        }
    }

    fun delete(note: Note){
        viewModelScope.launch(Dispatchers.IO){
            repository.insert(note)
        }
    }

    fun update(note: Note){
        viewModelScope.launch(Dispatchers.IO){
            repository.insert(note)
        }
    }
}