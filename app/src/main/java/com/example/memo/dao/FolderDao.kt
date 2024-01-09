package com.example.memo.dao

import androidx.room.*
import com.example.memo.entities.Folders

@Dao
interface FolderDao {
    @Query("SELECT * FROM Folders ORDER BY id DESC")
    suspend fun getAllFolders() : List<Folders>

    @Query("SELECT * FROM folders WHERE id =:id")
    suspend fun getSpecificFolder(id:Int) : Folders

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolders(folder:Folders)

    @Delete
    suspend fun deleteFolder(folder:Folders)

    // delete 수정
    @Query("DELETE FROM folders WHERE id =:id")
    suspend fun deleteSpecificFolder(id:Int)

    @Update
    suspend fun updateFolder(folder:Folders)

    @Query("SELECT * FROM folders WHERE title = :title")
    fun getFolderByTitle(title: String): Folders
}