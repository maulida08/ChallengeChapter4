package com.example.challengechapter4

import androidx.room.*

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM User WHERE username = :username AND pass = :password")
    fun login(username: String, password: String):Boolean

    @Query("SELECT * FROM Schedule")
    fun getAllSchedule(): List<Schedule>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSchedule(schedule: Schedule):Long

    @Update
    fun updateSchedule(schedule: Schedule):Int

    @Delete
    fun deleteSchedule(schedule: Schedule):Int

}