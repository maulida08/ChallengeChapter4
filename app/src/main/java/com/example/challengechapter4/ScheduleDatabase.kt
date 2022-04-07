package com.example.challengechapter4

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Schedule::class],version = 1)
abstract class ScheduleDatabase: RoomDatabase() {
    abstract fun schedule(): Schedule

    companion object{
        private var INSTANCE: ScheduleDatabase? = null

        fun getInstance(context: Context): ScheduleDatabase? {
            if(INSTANCE == null){
                synchronized(ScheduleDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,ScheduleDatabase::class.java, "Schedule.db").build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }

}