package com.saibal.logincontent.entity

import androidx.room.PrimaryKey
import java.time.LocalDateTime

data class UserHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val image_url:String,
    val timestamp:LocalDateTime
)
