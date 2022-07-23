package com.abdalrizky.japridonk.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.abdalrizky.japridonk.database.HistoryDao
import com.abdalrizky.japridonk.database.HistoryDatabase
import com.abdalrizky.japridonk.database.entity.Recipient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val historyDao: HistoryDao = HistoryDatabase.getDatabase(application).historyDao()

    fun insert(recipient: Recipient) {
        CoroutineScope(Dispatchers.IO).launch {
            historyDao.insert(recipient)
        }
    }

    fun delete(recipient: Recipient) {
        CoroutineScope(Dispatchers.IO).launch {
            historyDao.delete(recipient)
        }
    }

    fun getFiveLastHistory(): LiveData<List<Recipient>> {
        return historyDao.getFiveLastHistory()
    }
}