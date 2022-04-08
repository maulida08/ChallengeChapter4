package com.example.challengechapter4

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "pass")
    val pass: String,

    )
