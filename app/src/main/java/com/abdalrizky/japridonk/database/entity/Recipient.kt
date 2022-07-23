package com.abdalrizky.japridonk.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipient(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var number: String? = null,
    var message: String? = null
)