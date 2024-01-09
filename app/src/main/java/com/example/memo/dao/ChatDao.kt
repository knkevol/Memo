package com.example.memo.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.memo.entities.Chats

@Dao
interface ChatDao {
    @Insert
    suspend fun insert(chat: Chats)

    @Query("SELECT * FROM chats")
    fun getAllChats(): LiveData<List<Chats>>

    @Delete
    suspend fun delete(chat: Chats)

    @Query("SELECT * FROM chats WHERE text = :text")
    suspend fun getChatByText(text: String): Chats?
}
