package com.saibal.logincontent.repository

import androidx.lifecycle.LiveData
import com.saibal.logincontent.dao.HistoryDao
import com.saibal.logincontent.entity.UserHistory

class HistoryRepository(private  val historyDao: HistoryDao) {

    val allHistory: LiveData<List<UserHistory>> = historyDao.getAllUsers()

    suspend fun insert(history: UserHistory){
        historyDao.insert(history)
    }
}