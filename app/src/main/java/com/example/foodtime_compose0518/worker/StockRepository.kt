package com.example.foodtime_compose0518.worker


import com.example.foodtime_compose0518.R
import com.example.foodtime_compose0518.StockDao
import com.example.foodtime_compose0518.StockTable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.pow

    class StockRepository(private val dao: StockDao) {
        private val freshrange=0.5
        private val unfreshrange=0.5
        var yellowItem = mutableListOf<StockTable>()
        var redItem = mutableListOf<StockTable>()
        var dangerItem = mutableListOf<StockTable>()
        val UnexpireItem=dao.getUnexpiredStockItems()

       suspend fun categorizeUnexpiredItems(): Triple<List<StockTable>, List<StockTable>, List<StockTable>> {
           yellowItem.clear()
           redItem.clear()
           dangerItem.clear()



           UnexpireItem.collect { stockList ->
               stockList.forEach { stock ->
                   when (freshness(stock)) {
                       in 0.0..30.0 -> yellowItem.add(stock) // 30天以内
                       in 31.0..180.0 -> redItem.add(stock) // 31到180天
                       else -> dangerItem.add(stock) // 超过180天
                   }
               }
           }
           return Triple(yellowItem, redItem, dangerItem)
    }
            // 计算每个项目的 `freshness` 并筛选符合范围的项目



    fun freshness(note: StockTable): Double {
        val loginDate = note.loginDate //登入日期
        val expiryDate = note.expiryDate //到期日期
        val currentDate = System.currentTimeMillis() // 現在日期

        val n = when {
            (expiryDate - loginDate) <= 30 * 24 * 60 * 60 * 1000 -> 2.75 // 30天以內
            (expiryDate - loginDate) in 30 * 24 * 60 * 60 * 1000..180 * 24 * 60 * 60 * 1000 -> 2.5 // 30到180天之間
            else -> 2.3 // 超過180天
        }

        val result = exp(ln(0.01) * ((currentDate-loginDate).toDouble() / expiryDate-loginDate).pow(n))
        return result
    }

}
