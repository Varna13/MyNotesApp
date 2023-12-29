package com.example.vnotes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.vnotes.adapter.NotesAdapter
import com.example.vnotes.database.NoteDatabase
import com.example.vnotes.databinding.ActivityMainBinding
import com.example.vnotes.databinding.ListItemBinding
import com.example.vnotes.models.Note
import com.example.vnotes.models.NotesViewModel

class MainActivity : AppCompatActivity(),
    NotesAdapter.NotesClickListener,
    PopupMenu.OnMenuItemClickListener{

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NoteDatabase
    private lateinit var viewModel: NotesViewModel
    private lateinit var adapter: NotesAdapter
    private lateinit var selectedNote: Note

    private val updateResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK){
            val note = result?.data?.getSerializableExtra("note") as? Note
            if(note != null){
                viewModel.update(note)

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize the Ui
        initUI()

        viewModel = ViewModelProvider(this,
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NotesViewModel::class.java)

        viewModel.allNotes.observe(this) { list ->
            list?.let {

                adapter.updateList(list)
            }

        }

        database = NoteDatabase.getDatabase(this)
    }

    private fun initUI() {
        binding.rvNotes.setHasFixedSize(true)
        binding.rvNotes.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter = NotesAdapter(this, this)
        binding.rvNotes.adapter = adapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

            //when click on the floating action button and every thing is fine then we need to add the notes inside the database

            if (result.resultCode == Activity.RESULT_OK){
                val note = result?.data?.getSerializableExtra("note") as? Note

                if (note != null){
                    viewModel.insert(note)
                }
            }

        }

        binding.fabAddNote.setOnClickListener {
            val intent = Intent(this, AddNote::class.java)
            getContent.launch(intent)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null){
                    adapter.filterList(newText)
                }
                return true
            }

        })
    }

    override fun onItemClicked(note: Note) {
        val intent = Intent(this@MainActivity, AddNote::class.java)
        intent.putExtra("current_note", note)
        updateResult.launch(intent)

    }

    override fun onLongItemClicked(note: Note, cardView: CardView) {
        selectedNote = note
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {
        val popup = PopupMenu(this, cardView)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.deleteNote){
            viewModel.delete(selectedNote)
            return true
        }

        return false
    }


}