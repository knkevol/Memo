package com.example.memo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.memo.database.FoldersDatabase
import com.example.memo.databinding.FragmentFolderBinding
import com.example.memo.entities.Folders
import kotlinx.coroutines.launch


class FolderFragment : BaseFragment(), FolderFragmentListener {
    private lateinit var _binding: FragmentFolderBinding
    private val binding get() = _binding
    private var folderId = -1
    private lateinit var folderFragmentListener: FolderFragmentListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        folderId = requireArguments().getInt("folderId",-1)
        if (parentFragment is FolderFragmentListener) {
            folderFragmentListener = parentFragment as FolderFragmentListener
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FolderFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (folderId != -1){
            launch {
                context?.let {
                    val folders = FoldersDatabase.getDatabase(it).folderDao().getSpecificFolder(folderId)
                    binding.etAddFolder.setText(folders.title)
                }
            }
        }
        binding.addFolderButton.setOnClickListener {
            if (folderId != -1){
                updateFolder()
            }else{
                saveFolder()
            }
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.imgBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
    private fun updateFolder() {
        launch {
            context?.let {
                val folders = FoldersDatabase.getDatabase(it).folderDao().getSpecificFolder(folderId)
                folders.title = binding.etAddFolder.text.toString()

                FoldersDatabase.getDatabase(it).folderDao().updateFolder(folders)
                binding.etAddFolder.setText("")
                folderFragmentListener.onDeleteButtonClicked(folderId)
            }
        }
    }
    private fun saveFolder(){
        if (binding.etAddFolder.text.isNullOrEmpty()){
            Toast.makeText(context,"Folder Name is Required", Toast.LENGTH_SHORT).show()
        }
        else{
            launch {
                val folders = Folders()
                folders.title = binding.etAddFolder.text.toString()
                context?.let {
                    FoldersDatabase.getDatabase(it).folderDao().insertFolders(folders)
                    binding.etAddFolder.setText("")
                }
            }
        }
    }
    override fun onDeleteButtonClicked(folderId: Int) {
        val folderListFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.frame_container) as? FolderListFragment
        val currentFolderId = folderListFragment?.getCurrentFolderId()
        currentFolderId?.let(folderListFragment::deleteFolder)
    }
}