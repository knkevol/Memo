package com.example.memo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.memo.dao.ChatClearDao
import com.example.memo.dao.ChatDao
import com.example.memo.database.ChatClearDatabase
import com.example.memo.database.ChatsDatabase
import com.example.memo.databinding.FragmentChatBinding
import com.example.memo.entities.ChatClear
import com.example.memo.entities.Chats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var items: ArrayList<String>
    private lateinit var itemsAdapter: ArrayAdapter<String>
    private lateinit var listView: ListView
    private lateinit var button: AppCompatImageButton
    private lateinit var editText: EditText
    private lateinit var chatDao: ChatDao
    private lateinit var chatClearDao: ChatClearDao
    private lateinit var chatDatabase: ChatsDatabase
    private lateinit var chatClearDatabase: ChatClearDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        val view = binding.root

        listView = view.findViewById(R.id.listview)
        button = view.findViewById(R.id.chatbutton)
        editText = view.findViewById(R.id.edittext)

        chatDatabase = ChatsDatabase.getInstance(requireContext())
        chatDao = chatDatabase.chatDao()

        chatClearDatabase = ChatClearDatabase.getInstance(requireContext())
        chatClearDao = chatClearDatabase.chatClearDao()

        binding.clearbutton.setOnClickListener {
            // 현재 리스트의 사본을 저장
            val deletedChats = ArrayList(items)
            items.clear()
            itemsAdapter.notifyDataSetChanged()

            // ChatClearFragment로 이동하면서 삭제된 채팅 데이터 전달
            val clearFragment = ChatClearFragment.newInstance(deletedChats)
            clearFragment.setTargetFragment(this, 1)
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container, clearFragment)
                .addToBackStack(null)
                .commit()
        }

        items = ArrayList()
        itemsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items)
        listView.adapter = itemsAdapter
        button.setOnClickListener { addItem() }
        setUpListViewListener()

        chatDao.getAllChats().observe(viewLifecycleOwner, Observer {
            items.clear()
            items.addAll(it.map { chat -> chat.text })
            itemsAdapter.notifyDataSetChanged()
        })
        return view
    }

    private fun setUpListViewListener() {
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedChat = items[position]

            GlobalScope.launch(Dispatchers.IO) {
                val chatToDelete = chatDao.getChatByText(selectedChat)
                chatToDelete?.let {
                    chatDao.delete(it)

                    chatClearDao.insert(ChatClear(text = selectedChat))
                }
            }
        }
    }

    private fun addItem() {
        val itemText = editText.text.toString()
        if (itemText.isNotEmpty()) {
            GlobalScope.launch(Dispatchers.IO) {
                chatDao.insert(Chats(text = itemText))
            }
            editText.text.clear()
        } else {
            Toast.makeText(requireContext(), "Enter Text", Toast.LENGTH_LONG).show()
        }
    }
}