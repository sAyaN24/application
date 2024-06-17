package com.saibal.logincontent.util

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.saibal.logincontent.db.HistoryDatabase
import com.saibal.logincontent.entity.UserHistory
import com.saibal.logincontent.repository.HistoryRepository

class HistoryViewModel(application: Application): AndroidViewModel(application) {
    private val repository:HistoryRepository
    val allHistory:LiveData<List<UserHistory>>

    init{
        val historyDao = HistoryDatabase.getDatabase(application).historyDao()
        repository = HistoryRepository(historyDao)
        allHistory = repository.allHistory
    }

    fun insert(history: UserHistory) =  viewModelScope.launch { // Now inside the ViewModel
        repository.insert(history)
    }



    class UserViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HistoryViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}