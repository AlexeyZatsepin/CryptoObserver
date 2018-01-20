package org.room76.cryptoobserver.util

import org.room76.cryptoobserver.Model
import java.sql.Date
import java.sql.Timestamp
import java.util.*


class ModelsConverter {

    companion object {
        fun ohlcKrakenToChart(values: Model.OhlcKraken): Model.Chart {
            var chartData = ArrayList<Model.ChartData>()
            for (p in values.body.pair) {
                val average = (p[2].toLong() + p[3].toLong()) / 2
                val date = Calendar.getInstance()
                date.time = Date(Timestamp(p[0].toLong()).time)
                chartData.add(Model.ChartData(date, average))
            }
            return Model.Chart("Kraken", "ETH/USD", chartData)
        }   
    }
}