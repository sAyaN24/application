package com.saibal.logincontent.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.saibal.logincontent.entity.UserHistory


@Dao
interface HistoryDao {


        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insert(history: UserHistory)


        @Delete
        suspend fun delete(history: UserHistory)


        @Query("SELECT * FROM user_table ORDER BY timestamp ASC")
        fun getAllUsers(): LiveData<List<UserHistory>>

}
