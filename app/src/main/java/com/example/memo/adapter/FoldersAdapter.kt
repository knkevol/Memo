package com.example.memo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memo.EditFolderFragment
import com.example.memo.MemoListFragment
import com.example.memo.R
import com.example.memo.databinding.ItemFolderBinding
import com.example.memo.entities.Folders

class FoldersAdapter(private val folderFragmentListener: OnItemClickListener,
                     private val fragmentManager: FragmentManager
) : RecyclerView.Adapter<FoldersAdapter.FoldersViewHolder>() {
    private var listener: OnItemClickListener? = null
    private var arrList = ArrayList<Folders>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoldersViewHolder {
        val binding = ItemFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoldersViewHolder(binding, parent.context)
    }
    override fun getItemCount(): Int {
        return arrList.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setData(arrFoldersList: List<Folders>) {
        arrList.clear()
        arrList.addAll(arrFoldersList)
        notifyDataSetChanged()
    }
    fun setOnClickListener(listener1: OnItemClickListener) {
        listener = listener1
    }

    override fun onBindViewHolder(holder: FoldersViewHolder, position: Int) {
        val currentItem = arrList[position]

        holder.binding.filename.text = currentItem.title

        holder.binding.folder1.setOnClickListener {
            listener?.onClicked(currentItem.id!!)
        }

        holder.binding.folderMore.setOnClickListener {
            val folderId = arrList[position].id!!
            holder.showPopupMenu(folderId)
        }
    }
    inner class FoldersViewHolder(val binding: ItemFolderBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {
        private val folderMore: ImageView = binding.folderMore

        fun showPopupMenu(folderId: Int) {
            val popupMenu = PopupMenu(context, folderMore)
            popupMenu.inflate(R.menu.show_menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.editTitle -> {
                        val editFolderFragment = EditFolderFragment.newInstance(folderId)
                        (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                            .replace(R.id.frame_container, editFolderFragment)
                            .addToBackStack(null)
                            .commit()
                        true
                    }
                    R.id.delete -> {
                        folderFragmentListener.onDeleteButtonClicked(folderId)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }
    interface OnItemClickListener {
        fun onClicked(folderId: Int)
        fun onDeleteButtonClicked(folderId: Int)
    }
}
