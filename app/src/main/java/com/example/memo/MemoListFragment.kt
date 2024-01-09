package com.example.memo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.memo.databinding.FragmentMemoListBinding
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.memo.adapter.NotesAdapter
import com.example.memo.database.NotesDatabase
import com.example.memo.entities.Notes
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class MemoListFragment : BaseFragment() {

    private var _binding: FragmentMemoListBinding? = null
    private val binding get() = _binding!!
    var arrNotes = ArrayList<Notes>()
    var notesAdapter: NotesAdapter = NotesAdapter()
    private var folderId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            folderId = it.getInt("folderId", -1)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemoListBinding.inflate(inflater, container, false)
        return binding.root
    }
    companion object {
        @JvmStatic
        fun newInstance(folderId: Int): MemoListFragment {
            val fragment = MemoListFragment()
            val args = Bundle().apply {
                putInt("folderId", folderId)
            }
            fragment.arguments = args
            return fragment
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

        launch {
            context?.let {
                val notes = NotesDatabase.getDatabase(it).noteDao().getAllNotes()
                notesAdapter.setData(notes)
                arrNotes = notes as ArrayList<Notes>
                binding.recyclerView.adapter = notesAdapter
            }
        }
        notesAdapter.setOnClickListener(onClicked)

        binding.fab1.setOnClickListener {
            replaceFragment(MemoFragment.newInstance(),false)
        }
        binding.search.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }
            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(p0: String?): Boolean {

                val tempArr = ArrayList<Notes>()

                for (arr in arrNotes){
                    if (arr.title!!.lowercase(Locale.getDefault()).contains(p0.toString())){
                        tempArr.add(arr)
                    }
                }
                notesAdapter.setData(tempArr)
                notesAdapter.notifyDataSetChanged()
                return true
            }
        })
        binding.imgBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private val onClicked = object :NotesAdapter.OnItemClickListener{
        override fun onClicked(noteId: Int) {
            val fragment :Fragment
            val bundle = Bundle().apply {
                putInt("noteId", noteId)
            }
            fragment = MemoFragment.newInstance()
            fragment.arguments = bundle
            replaceFragment(fragment,false)
        }
    }
    fun replaceFragment(fragment:Fragment, istransition:Boolean){
        val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()

        if (istransition){
            fragmentTransition.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left)
        }
        fragmentTransition.replace(R.id.frame_container,fragment).addToBackStack(fragment.javaClass.simpleName).commit()
    }
    override fun onResume() {
        super.onResume()
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavi(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}