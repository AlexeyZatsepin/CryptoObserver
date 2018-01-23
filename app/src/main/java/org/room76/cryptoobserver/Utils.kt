package org.room76.cryptoobserver

import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*


object Utils {

    val dateFormatWithYear = SimpleDateFormat("HH:mm dd/MM/yyyy")
    val dateFormatWithMonth = SimpleDateFormat("HH:mm dd/MM")
    val dateFormatWithDay = SimpleDateFormat("HH:mm dd")
    val dateFormat = SimpleDateFormat("HH:mm")

    interface Action<in T> {
        fun action(data: T)
    }

    fun ohlcKrakenToChart(values: Model.OhlcKraken): Model.Chart {
        val chartData: MutableList<Model.ChartData> = mutableListOf()
        if (values.error.isEmpty()) {
            for (p in values.body.pair) {
                val average = (p[2].toString().toFloat() + p[3].toString().toFloat()) / 2
                val date = Calendar.getInstance()
                date.time = Date(Timestamp((p[0] as Double).toLong()).time)
                chartData.add(Model.ChartData(date, average))
            }
        }
        return Model.Chart("Kraken", "ETH/USD", chartData)
    }

    fun bittrixCoins(): Map<String, String> {
        val coinsNames = HashMap<String, String>()
        coinsNames.put("BTC", "USDT-BTC")
        coinsNames.put("ETH", "USDT-ETH")
        coinsNames.put("BTC", "USDT-DASH")
        return coinsNames
    }


    fun yobitCoins(): Map<String, String> {
        val coinsNames = HashMap<String, String>()
        coinsNames.put("BTC", "USDT-BTC")
        coinsNames.put("ETH", "USDT-ETH")
        coinsNames.put("BTC", "USDT-DASH")
        return coinsNames
    }

    fun krakenCoins(): Map<String, String> {
        val coinsNames = HashMap<String, String>()
        coinsNames.put("ETH", "ETHUSD")
        return coinsNames
    }
}