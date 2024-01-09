package com.example.memo.dao

import androidx.room.*
import com.example.memo.entities.Notes

@Dao
interface NoteDao {

    @Query("SELECT * FROM Notes ORDER BY id DESC")
    suspend fun getAllNotes() : List<Notes>

    @Query("SELECT * FROM notes WHERE id =:id")
    suspend fun getSpecificNote(id:Int) : Notes

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(note:Notes)

    @Delete
    suspend fun deleteNote(note:Notes)

    @Query("DELETE FROM notes WHERE id =:id")
    suspend fun deleteSpecificNote(id:Int)

    @Update
    suspend fun updateNote(note:Notes)

    @Query("SELECT * FROM notes WHERE folderId = :folderId ORDER BY id DESC")
    suspend fun getNotesForFolder(folderId: Int): List<Notes>
}