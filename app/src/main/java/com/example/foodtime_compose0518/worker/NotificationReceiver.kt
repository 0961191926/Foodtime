package com.example.foodtime_compose0518.worker
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.foodtime_compose0518.FoodDatabase
import com.example.foodtime_compose0518.R
import com.example.foodtime_compose0518.StockTable
import com.example.foodtime_compose0518.convertLongToDateString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.pow
import com.example.foodtime_compose0518.MainActivity
import android.app.PendingIntent

data class StockItemWithFreshness(
    val stockItem: StockTable,
    val freshness: Double,
    val lightSignal: Int
)

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // 在 IO 调度器中启动协程
        CoroutineScope(Dispatchers.IO).launch {
            val database = FoodDatabase.getInstance(context.applicationContext) // 使用 ApplicationContext
            val data = database.stockDao.getUnExpiredStockItemsList()
            val enabledYellowLight=database.settingDao.getSettingByName("YellowLightEnabled")
            val enabledRedLight=database.settingDao.getSettingByName("RedLightEnabled")
            val enableNotifiction=database.settingDao.getSettingByName("NotificationEnabled")

            if (enableNotifiction != null) {
                if (enableNotifiction.settingNotify) {
                    val stockItemsWithFreshness = data.map { stockItem ->
                        val freshnessValue = freshness(stockItem)
                        val lightSignalValue = lightSignal(freshnessValue)
                        StockItemWithFreshness(stockItem, freshnessValue, lightSignalValue)
                    }
                    val groupedStockItems = stockItemsWithFreshness.groupBy { it.lightSignal }
                    val freshItems = groupedStockItems[R.drawable.greenlight]
                    val expiringItems = groupedStockItems[R.drawable.yellowlight]
                    val expiredItems = groupedStockItems[R.drawable.redlight]
                    val skulltems = groupedStockItems[R.drawable.skull]

                    // 构建通知内容
                    val messageContent = StringBuilder("過期了\n" + freshItemsToMessageContent(skulltems)) // Assuming skulltems should be expiredItems

                    if (enabledYellowLight != null && enabledYellowLight.settingNotify) {
                        messageContent.append("黃色警戒\n").append(freshItemsToMessageContent(expiringItems))
                    }

                    if (enabledRedLight != null && enabledRedLight.settingNotify) {
                        messageContent.append("紅色警戒\n").append(freshItemsToMessageContent(expiredItems))
                    }

                    // 切换到主线程以显示通知

                    withContext(Dispatchers.Main) {
                        val intent = Intent(context, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            putExtra("notification_type", "food_expired")
                        }
                        val requestCode = System.currentTimeMillis().toInt()
                        val pendingIntent = PendingIntent.getActivity(
                            context,
                            requestCode,
                            intent,
                            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                        )

                        val notificationManager =
                            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        val notification = NotificationCompat.Builder(context, "default_channel_id")
                            .setContentTitle("這是即將到期的食物通知")
                            .setContentText("")
                            .setSmallIcon(R.drawable.ingredients_apple)
                            .setStyle(NotificationCompat.BigTextStyle().bigText(messageContent))
                            .setContentIntent(pendingIntent) // 設置點擊通知時的行為
                            .setAutoCancel(true) // 點擊後自動移除通知
                            .build()

                        notificationManager.notify(1, notification)
                    }
                }
            }
        }
    }
    fun freshness(note: StockTable): Double {
        var loginDate = note.loginDate //登入日期
        var expiryDate = note.expiryDate //到期日期
        var currentDate = System.currentTimeMillis() // 現在日期
        val daysDifference = (expiryDate - loginDate) / (1000 * 60 * 60 * 24)
        var n = when {
            daysDifference <= 30 -> 2.75 // 30天以內
            daysDifference in 30..180  -> 2.5 // 30到180天之間
            else -> 2.3 // 超過180天
        }
        var passedtime = (currentDate-loginDate).toDouble()
        var totaltime = (expiryDate-loginDate).toDouble()
        return exp(ln(0.01) * (passedtime/totaltime).pow(n))
    }
    fun lightSignal(freshness: Double): Int {
        return when {
            freshness in 0.5..1.0 -> R.drawable.greenlight // Fresh
            freshness in 0.2..0.5 -> R.drawable.yellowlight
            freshness in 0.000001..0.2 -> R.drawable.redlight
            else -> R.drawable.skull // Not fresh
        }
    }
    fun freshItemsToMessageContent(freshItems: List<StockItemWithFreshness>?): String {
        if (freshItems.isNullOrEmpty()) {
            return ""
        }

        val messageBuilder = StringBuilder()
        messageBuilder.append("注意!!：\n")

        freshItems.forEach { item ->
            messageBuilder.append("- ${item.stockItem.stockitemName}：過期時間 ${convertLongToDateString(item.stockItem.expiryDate) }\n")
            // 可以根據需要添加其他信息，例如到期日期等
        }

        return messageBuilder.toString()
    }
}