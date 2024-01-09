package com.example.memo.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_clear")
data class ChatClear(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String
)