package com.example.challengechapter4

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,

    @ColumnInfo(name = "judul")
    var judul: String,

    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "desc")
    var desc: String,
)

