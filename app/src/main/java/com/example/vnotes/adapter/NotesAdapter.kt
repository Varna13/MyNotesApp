package com.example.vnotes.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.vnotes.R
import com.example.vnotes.databinding.ListItemBinding
import com.example.vnotes.models.Note
import kotlin.random.Random

class NotesAdapter(
    private val context: Context,
    private val listener: NotesClickListener
    ): RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    private val noteList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    fun updateList(newList: List<Note>){
        fullList.clear()
        fullList.addAll(newList)

        noteList.clear()
        noteList.addAll(fullList)
        notifyDataSetChanged()
    }

    //whenever user types anything in the search bar
    fun filterList(search: String){

        noteList.clear()

        for (item in fullList){
            if (item.title?.lowercase()?.contains(search.lowercase()) == true ||
                    item.note?.lowercase()?.contains(search.lowercase()) == true) {

                noteList.add(item)
            }
        }

        notifyDataSetChanged()

    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val currentNote = noteList[position]
        holder.tvTitle.text = currentNote.title
        holder.tvTitle.isSelected = true

        holder.tvNotes.text = currentNote.note
        holder.tvDate.text = currentNote.date
        holder.tvDate.isSelected = true

        holder.notes_layout.setCardBackgroundColor(holder.itemView.resources.getColor(randomColors(), null))

        holder.notes_layout.setOnClickListener {
            listener.onItemClicked(noteList[holder.adapterPosition])
        }

        holder.notes_layout.setOnLongClickListener {
            listener.onLongItemClicked(noteList[holder.adapterPosition], holder.notes_layout)
            true
        }
    }

    fun randomColors(): Int{
        val list = ArrayList<Int>()
        list.add(R.color.NoteColor1)
        list.add(R.color.NoteColor2)
        list.add(R.color.NoteColor3)
        list.add(R.color.NoteColor4)
        list.add(R.color.NoteColor5)
        list.add(R.color.NoteColor6)

        val seed = System.currentTimeMillis()
        val randomIndex = Random(seed).nextInt(list.size)
        return list[randomIndex]
    }
    inner class NotesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val notes_layout = itemView.findViewById<CardView>(R.id.cardLayout)
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val tvNotes = itemView.findViewById<TextView>(R.id.tvNote)
        val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
    }

    interface NotesClickListener{

        fun onItemClicked(note: Note)
        fun onLongItemClicked(note: Note, cardView: CardView)

    }
}