package com.example.foodtime_compose0518.worker

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.foodtime_compose0518.MainActivity.Companion.CHANNEL_ID
import com.example.foodtime_compose0518.R

class StockNotification(private val context: Context) {

    fun sendNotification(message: String) {
        val notificationManager = NotificationManagerCompat.from(context)

        // 创建通知通道（如果尚未创建）


        // 构建通知
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.apple)  // 替换为你实际的图标资源
            .setContentTitle("My App Notification")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        // 检查并请求通知权限
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 请求通知权限（如果未授予）
            ActivityCompat.requestPermissions(
                (context as Activity),
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        // 显示通知，使用唯一的通知ID
        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {

        const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1
    }
}
