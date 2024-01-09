package com.example.memo

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.memo.database.FoldersDatabase
import com.example.memo.databinding.FragmentEditFolderBinding
import com.example.memo.entities.Folders
import kotlinx.coroutines.launch


class EditFolderFragment : BaseFragment() {

    private var _binding: FragmentEditFolderBinding? = null
    private val binding get() = _binding!!
    private lateinit var folderFragmentListener: FolderFragmentListener
    private var folderId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        folderId = requireArguments().getInt("folderId", -1)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEditFolderBinding.inflate(inflater, container, false)

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(folderId: Int) =
            EditFolderFragment().apply {
                arguments = Bundle().apply {
                    putInt("folderId", folderId)
                }
            }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (folderId != -1) {

            launch {
                context?.let {
                    val folders =
                        FoldersDatabase.getDatabase(it).folderDao().getSpecificFolder(folderId)
                    binding.etEditFolder.setText(folders.title)
                }
            }
        }

        binding.editFolderButton.setOnClickListener {
            if (folderId != -1){
                updateFolder()
            }else{
                saveFolder()
            }
        }

        binding.imgBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun updateFolder() {
        launch {
            context?.let {
                val folders =
                    FoldersDatabase.getDatabase(it).folderDao().getSpecificFolder(folderId)
                folders.title = binding.etEditFolder.text.toString()

                FoldersDatabase.getDatabase(it).folderDao().updateFolder(folders)
                binding.etEditFolder.setText("")
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun saveFolder() {
        if (binding.etEditFolder.text.isNullOrEmpty()) {
            Toast.makeText(context, "Folder Name is Required", Toast.LENGTH_SHORT).show()
        } else {
            launch {
                val folders = Folders()
                folders.title = binding.etEditFolder.text.toString()
                context?.let {
                    FoldersDatabase.getDatabase(it).folderDao().insertFolders(folders)
                    binding.etEditFolder.setText("")
                }
            }
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FolderFragmentListener) {
            folderFragmentListener = context
        } else {
            throw RuntimeException("$context must implement FolderFragmentListener")
        }
    }

//    private fun deleteFolder(folderId: Int) {
//        val folderListFragment = requireActivity().supportFragmentManager.fragments.find { it is FolderListFragment } as? FolderListFragment
//        folderListFragment?.deleteFolder(folderId)
//    }
}