package com.example.foodtime_compose0518.worker
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.foodtime_compose0518.FoodDatabase
import com.example.foodtime_compose0518.MainActivity
import com.example.foodtime_compose0518.R

class NotificationWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    private val database = FoodDatabase.getInstance(appContext)
    private val repository = StockRepository(database.stockDao)
    override fun doWork(): Result {

        val message = inputData.getString("message") ?: "Default message"
        val stockNotification = StockNotification(applicationContext)
        // 调用 NotificationUtils 中的 sendNotification 方法
        stockNotification.sendNotification(message)
        return Result.success()
    }


}

