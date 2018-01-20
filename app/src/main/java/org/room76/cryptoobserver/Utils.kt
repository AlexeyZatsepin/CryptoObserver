package org.room76.cryptoobserver.util

import org.room76.cryptoobserver.Model
import java.sql.Date
import java.sql.Timestamp
import java.util.*


object Utils {

    fun ohlcKrakenToChart(values: Model.OhlcKraken): Model.Chart {
        val chartData = ArrayList<Model.ChartData>()
        for (p in values.body.pair) {
            val average = (p[2].toFloat() + p[3].toFloat()) / 2
            val date = Calendar.getInstance()
            date.time = Date(Timestamp(p[0].toLong()).time)
            chartData.add(Model.ChartData(date, average))
        }
        return Model.Chart("Kraken", "ETH/USD", chartData)
    }
}