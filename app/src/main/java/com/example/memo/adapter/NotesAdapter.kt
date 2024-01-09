package com.example.memo.adapter

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memo.entities.Notes
import java.util.*
import com.example.memo.databinding.ItemRvMemoBinding

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    private var listener: OnItemClickListener? = null
    private var arrList = ArrayList<Notes>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = ItemRvMemoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrList.size
    }

    fun setData(arrNotesList: List<Notes>) {
        arrList = arrNotesList as ArrayList<Notes>
    }

    fun setOnClickListener(listener1: OnItemClickListener) {
        listener = listener1
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val currentItem = arrList[position]

        holder.binding.itemMemoTitle.text = currentItem.title
        holder.binding.itemMemoDesc.text = currentItem.noteText
        holder.binding.itemMemoTime.text = currentItem.dateTime

        if (arrList[position].color != null) {
            holder.binding.cardView.setCardBackgroundColor(Color.parseColor(arrList[position].color))
        } else {
            holder.binding.cardView.setCardBackgroundColor(Color.BLACK)
        }

        if (arrList[position].imgPath != null) {
            holder.binding.imgNote.setImageBitmap(BitmapFactory.decodeFile(arrList[position].imgPath))
            holder.binding.imgNote.visibility = View.VISIBLE
        } else {
            holder.binding.imgNote.visibility = View.GONE
        }

        if (arrList[position].webLink != "") {
            holder.binding.WebLink.text = arrList[position].webLink
            holder.binding.WebLink.visibility = View.VISIBLE
        } else {
            holder.binding.WebLink.visibility = View.GONE
        }

        holder.binding.cardView.setOnClickListener {
            listener!!.onClicked(arrList[position].id!!)
        }
    }

    class NotesViewHolder(val binding: ItemRvMemoBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onClicked(noteId: Int)
    }
}
