package com.example.challengechapter4

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "judul")
    val judul: String,

    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "desc")
    val desc: String,
)

