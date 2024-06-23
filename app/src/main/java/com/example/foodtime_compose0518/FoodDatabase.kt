package com.example.foodtime_compose0518

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HolidayTable::class], version = 1, exportSchema = false)
abstract class FoodDatabase : RoomDatabase() {
    abstract val foodDao: FoodDao

    companion object {
        @Volatile
        private var INSTANCE: FoodDatabase? = null

        fun getInstance(context: Context): FoodDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FoodDatabase::class.java,
                        "foods_database"
                    )
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
