package com.example.memo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.memo.dao.ChatClearDao
import com.example.memo.entities.ChatClear

@Database(entities = [ChatClear::class], version = 1, exportSchema = false)
abstract class ChatClearDatabase : RoomDatabase() {

    abstract fun chatClearDao(): ChatClearDao

    companion object {
        @Volatile
        private var INSTANCE: ChatClearDatabase? = null

        fun getInstance(context: Context): ChatClearDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChatClearDatabase::class.java,
                    "chat_clear_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}