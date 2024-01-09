package com.example.memo.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.memo.entities.ChatClear

@Dao
interface ChatClearDao {
    @Insert
    suspend fun insert(chatClear: ChatClear)

    @Query("SELECT * FROM chat_clear")
    fun getAllChatClears(): LiveData<List<ChatClear>>

    @Delete
    suspend fun delete(chatClear: ChatClear)

    @Query("DELETE FROM chat_clear")
    suspend fun deleteAllChatClears()

    @Query("SELECT * FROM chat_clear WHERE text = :text")
    suspend fun getChatClearByText(text: String): ChatClear?
}