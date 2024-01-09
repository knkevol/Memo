package com.example.memo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.memo.dao.NoteDao
import com.example.memo.entities.Folders
import com.example.memo.entities.Notes

@Database(entities = [Folders::class, Notes::class], version = 1, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NotesDatabase? = null

        fun getDatabase(context: Context): NotesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java,
                    "notes_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            db.execSQL(
                                "CREATE TABLE IF NOT EXISTS Folders (" +
                                        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                                        "title TEXT)"
                            )

                            db.execSQL(
                                "CREATE TABLE IF NOT EXISTS Notes (" +
                                        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                                        "title TEXT, " +
                                        "sub_title TEXT, " +
                                        "date_time TEXT, " +
                                        "note_text TEXT, " +
                                        "img_path TEXT, " +
                                        "web_link TEXT, " +
                                        "color TEXT, " +
                                        "folderId INTEGER NOT NULL, " +
                                        "FOREIGN KEY(folderId) REFERENCES Folders(id) ON DELETE CASCADE" +
                                        ")"
                            )
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}