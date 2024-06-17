package com.saibal.logincontent.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.saibal.logincontent.dao.HistoryDao
import com.saibal.logincontent.entity.UserHistory

@Database(entities = [UserHistory::class], version = 1, exportSchema = false)
abstract class HistoryDatabase:RoomDatabase() {

    abstract fun historyDao(): HistoryDao

    companion object{

        @Volatile
        private var INSTANCE:HistoryDatabase? = null
        fun getDatabase(context: Context): HistoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HistoryDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}


