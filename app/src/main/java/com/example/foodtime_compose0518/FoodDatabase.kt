package com.example.foodtime_compose0518

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [HolidayTable::class, NormalTable::class, StockTable::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FoodDatabase : RoomDatabase(){
    abstract val foodDao: FoodDao
    abstract val normalDao: NormalDao
    abstract val stockDao: StockDao

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
