package com.example.memo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.memo.dao.FolderDao
import com.example.memo.entities.Folders

@Database(entities = [Folders::class], version = 2, exportSchema = false)
abstract class FoldersDatabase : RoomDatabase() {
    abstract fun folderDao():FolderDao
    companion object {
        private var foldersDatabase: FoldersDatabase? = null

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS Folders (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "title TEXT)"
                )
                // Notes 테이블 생성
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
        }

        fun getDatabase(context: Context): FoldersDatabase {
            if (foldersDatabase == null) {
                foldersDatabase = Room.databaseBuilder(
                    context, FoldersDatabase::class.java, "folders.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
            }
            return foldersDatabase!!
        }
    }
}
