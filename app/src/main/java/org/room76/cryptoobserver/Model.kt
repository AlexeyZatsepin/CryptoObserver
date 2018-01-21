package org.room76.cryptoobserver

import com.google.gson.annotations.SerializedName
import java.sql.Date
import java.time.LocalDateTime
import java.util.*

object Model {
    data class Currency(@SerializedName("Currency") var currency: String,
                        @SerializedName("CurrencyLong") var currencyLong: String,
                        @SerializedName("MinConfirmation") var minConfirmation: Int,
                        @SerializedName("TxFee") var txFee: Float,
                        @SerializedName("IsActive") var isActive: Boolean,
                        @SerializedName("CoinType") var coinType: String,
                        @SerializedName("BaseAddress") var baseAddress: String)

    data class OhlcKraken(
            @SerializedName("error") var error: List<String>,
            @SerializedName("result") var body: OhlcResultEthUsd)

    data class OhlcResultEthUsd(
            @SerializedName("XETHZUSD") var pair: List<List<Any>>,
            @SerializedName("last") var lastTransaction: String
    )


    data class Chart(
            var exchangerName: String,
            var pair: String,
            var chartData: List<ChartData>
    )

    data class ChartData(
           var date: Calendar,
           var average: Float
    )
}