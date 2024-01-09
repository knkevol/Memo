package com.example.memo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.widget.AppCompatImageButton
import com.example.memo.dao.ChatClearDao
import com.example.memo.database.ChatClearDatabase
import com.example.memo.databinding.FragmentChatBinding
import com.example.memo.databinding.FragmentChatClearBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChatClearFragment : Fragment() {
    private lateinit var items: ArrayList<String>
    private lateinit var itemsAdapter: ArrayAdapter<String>
    private lateinit var listView: ListView
    private lateinit var chatClearDao: ChatClearDao
    private lateinit var chatClearDatabase: ChatClearDatabase

    companion object {
        private const val ARG_DELETED_CHATS = "arg_deleted_chats"

        fun newInstance(deletedChats: List<String>): ChatClearFragment {
            val fragment = ChatClearFragment()
            val args = Bundle()
            args.putStringArrayList(ARG_DELETED_CHATS, ArrayList(deletedChats))
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_chat_clear, container, false)
        listView = view.findViewById(R.id.listview)

        // 삭제된 채팅 데이터 가져오기
        items = arguments?.getStringArrayList(ARG_DELETED_CHATS) ?: arrayListOf()
        itemsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items)
        listView.adapter = itemsAdapter

        // 클릭 시 데이터 삭제
        listView.setOnItemClickListener { _, _, position, _ ->
            val deletedChat = items[position]
            items.removeAt(position)
            itemsAdapter.notifyDataSetChanged()
            GlobalScope.launch(Dispatchers.IO) {
                // ChatClearFragment에서 삭제된 데이터 삭제
                val chatClearToDelete = chatClearDao.getChatClearByText(deletedChat)
                chatClearToDelete?.let {
                    chatClearDao.delete(it)
                }
            }
        }
        // ChatFragment로 이동
        val backButton: AppCompatImageButton = view.findViewById(R.id.backbutton)
        backButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container, ChatFragment())
                .commit()
        }
        return view
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chatClearDatabase = ChatClearDatabase.getInstance(requireContext())
        chatClearDao = chatClearDatabase.chatClearDao()
    }
}