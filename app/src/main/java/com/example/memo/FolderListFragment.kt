package com.example.memo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.memo.adapter.FoldersAdapter
import com.example.memo.database.FoldersDatabase
import com.example.memo.databinding.FragmentFolderListBinding
import com.example.memo.entities.Folders
import com.example.memo.viewmodel.FolderListViewModel
import kotlinx.coroutines.launch
import java.util.Locale

class FolderListFragment : BaseFragment(), FolderFragmentListener {

    private var _binding: FragmentFolderListBinding? = null
    private val binding get() = _binding
    private var currentFolderId: Int = -1
    private var arrFolders = ArrayList<Folders>()
    private val viewModel: FolderListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFolderListBinding.inflate(inflater, container, false)
        return binding!!.root
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            FolderListFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setup
        binding?.folderRecyclerView?.setHasFixedSize(true)
        binding?.folderRecyclerView?.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding?.folderRecyclerView?.adapter = foldersAdapter
        observeFolderList()

        launch {
            context?.let {
                val folders = FoldersDatabase.getDatabase(it).folderDao().getAllFolders()
                arrFolders = folders as ArrayList<Folders>
                foldersAdapter.setData(folders)
                foldersAdapter.notifyDataSetChanged()
            }
        }
        foldersAdapter.setOnClickListener(onClicked)

        binding?.fab2?.setOnClickListener {
            replaceFragment(FolderFragment.newInstance(), false)
        }
        binding?.folderSearch?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                filterFolders(p0)
                return true
            }
        })
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun observeFolderList() {
        viewModel.folderList.observe(viewLifecycleOwner) { folders ->
            arrFolders = folders.toMutableList() as ArrayList<Folders>
            this.foldersAdapter.setData(folders)
            this.foldersAdapter.notifyDataSetChanged()
        }
        viewModel.getFolders()
    }

    private fun filterFolders(query: String?) {
        val tempArr = ArrayList<Folders>()

        if (query.isNullOrBlank()) {
            tempArr.addAll(arrFolders)
        } else {
            for (arr in arrFolders) {
                arr.title?.let {
                    if (it.lowercase(Locale.getDefault())
                            .contains(query.toString().lowercase(Locale.getDefault()))
                    ) {
                        val folder = Folders()
                        folder.id = arr.id
                        folder.title = arr.title
                        tempArr.add(folder)
                    }
                }
            }
        }
        foldersAdapter.setData(tempArr)
    }

    private val onClicked = object : FoldersAdapter.OnItemClickListener {
        override fun onClicked(folderId: Int) {
            currentFolderId = folderId
            // 함수 추가
            navigateToMemoListFragment(folderId)
        }
        //함수 추가
        private fun navigateToMemoListFragment(folderId: Int) {
            val memoListFragment = MemoListFragment.newInstance(folderId)
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container, memoListFragment)
                .addToBackStack(null)
                .commit()
        }

        override fun onDeleteButtonClicked(folderId: Int) {
            viewLifecycleOwner.lifecycleScope.launch {
                deleteFolder(folderId)
            }
        }
    }
    private val foldersAdapter: FoldersAdapter by lazy {
        FoldersAdapter(onClicked, childFragmentManager)
    }

    fun getCurrentFolderId(): Int {
        return currentFolderId
    }
    fun deleteFolder(folderId: Int) {
        viewModel.deleteFolder(folderId)
    }
    private fun replaceFragment(fragment:Fragment, istransition:Boolean){
        val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()

        if (istransition){
            fragmentTransition.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left)
        }
        fragmentTransition.replace(R.id.frame_container,fragment).addToBackStack(fragment.javaClass.simpleName).commit()
    }

    // 왜 이 설정을 해줘야 하는지??
    override fun onResume() {
        super.onResume()
        val mainAct = activity as MainActivity
        mainAct.HideBottomNavi(false)
    }

    override fun onDeleteButtonClicked(folderId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            deleteFolder(folderId)
        }
    }
}