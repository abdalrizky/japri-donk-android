package com.abdalrizky.japridonk.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.abdalrizky.japridonk.database.entity.Recipient

@Dao
interface HistoryDao {
    @Insert
    fun insert(recipient: Recipient)

    @Delete
    fun delete(recipient: Recipient)

    @Query("SELECT * FROM Recipient ORDER BY id DESC")
    fun getAllHistory(): LiveData<List<Recipient>>

    @Query("SELECT * FROM Recipient ORDER BY id DESC LIMIT 5")
    fun getFiveLastHistory(): LiveData<List<Recipient>>

    @Query("DELETE FROM Recipient")
    fun deleteAllHistory()
}