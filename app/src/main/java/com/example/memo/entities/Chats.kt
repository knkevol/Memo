package com.example.memo.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class Chats(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String
)
