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
import com.example.foodtime_compose0518.StockTable
import kotlinx.coroutines.runBlocking

class NotificationWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    private val database = FoodDatabase.getInstance(appContext)
    private val repository = StockRepository(database.stockDao)
    override fun doWork(): Result {
        Log.d("NotificationWorker", "Sending notification...")
        val message = inputData.getString("message") ?: "Default message"
        val stockNotification = StockNotification(applicationContext)
        val (yellowItem, redItem, dangerItem) = runBlocking {
            repository.categorizeUnexpiredItems()
        }
        // 调用 NotificationUtils 中的 sendNotification 方法
        stockNotification.sendNotification("yellow")
        Log.d("StockNotification", "Sent notification for yellowItem: ${toMessage("yellowItem", yellowItem)}")

        return Result.success()
    }

    fun toMessage(title: String, items: List<StockTable>): String {
     //
        val messages = mutableListOf<String>()

        items.forEach { chunk ->
            val content = chunk.stockitemName // 假设 StockTable 有 stockitemName 属性
            val message = "$title: $content"
            messages.add(message)
        }

        return messages.joinToString(separator = "\n")
    }

}

