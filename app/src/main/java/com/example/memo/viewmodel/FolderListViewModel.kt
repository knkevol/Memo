package com.example.memo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.memo.database.FoldersDatabase
import com.example.memo.entities.Folders
import kotlinx.coroutines.launch

class FolderListViewModel(application: Application) : AndroidViewModel(application) {

    private val _folderList = MutableLiveData<List<Folders>>()
    val folderList: LiveData<List<Folders>> get() = _folderList

    init {
        _folderList.value = emptyList()
    }

    fun getFolders() {
        viewModelScope.launch {
            val folders = FoldersDatabase.getDatabase(getApplication()).folderDao().getAllFolders()
            _folderList.postValue(folders)
        }
    }

    fun deleteFolder(folderId: Int) {
        viewModelScope.launch {
            FoldersDatabase.getDatabase(getApplication()).folderDao().deleteSpecificFolder(folderId)
            getFolders()
        }
    }
}
