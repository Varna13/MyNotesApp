package com.example.vnotes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.vnotes.databinding.ActivityAddNoteBinding
import com.example.vnotes.models.Note
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date

class AddNote : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding

    private lateinit var note: Note
    private lateinit var oldNote: Note
    var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            oldNote = intent.getSerializableExtra("current_note") as Note
            binding.etTitle.setText(oldNote.title)
            binding.etNote.setText(oldNote.note)
            isUpdate = true
        }catch (e:Exception){
            e.printStackTrace()
        }

        binding.ivCheck.setOnClickListener {
            val title =  binding.etTitle.text.toString()
            val noteDes =  binding.etNote.text.toString()

            if (title.isNotEmpty() || noteDes.isNotEmpty()){

                val formatter = SimpleDateFormat("EEE, d MMM yyy HH:mm a")

                if (isUpdate){
                    note = Note(oldNote.id, title, noteDes, formatter.format(Date()))

                } else {
                    note = Note(null, title, noteDes, formatter.format(Date()))
                }

                val intent = Intent()
                intent.putExtra("note", note)
                setResult(Activity.RESULT_OK, intent)
                finish()

            } else {
                Toast.makeText(this@AddNote, "Please Enter some data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }
}