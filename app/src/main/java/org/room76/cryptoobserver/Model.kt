package org.room76.cryptoobserver

import com.google.gson.annotations.SerializedName
import java.util.*

object Model {
    data class BittrixMarketSummary(
            @SerializedName("success") var success: Boolean,
            @SerializedName("message") var message: String,
            @SerializedName("result") var result: List<BittrixMarketResult>
    )

    data class BittrixMarketResult(
            @SerializedName("Last") var last: Double
    )

    data class OhlcKraken(
            @SerializedName("error") var error: List<String>,
            @SerializedName("result") var body: OhlcResultEthUsd)

    data class OhlcResultEthUsd(
            @SerializedName("XETHZUSD") var pair: List<List<String>>,
            @SerializedName("last") var lastTransaction: Long
    )

    data class Chart(
            var exchangerName: String,
            var pair: String,
            var chartData: List<ChartData>
    )

    data class ChartData(
            var date: Calendar,
            var average: Long
    )

    interface Market {
        fun getName(): String
        fun getToFee(): Double
        fun getFromFee(): Double
        fun getExchangeCurrencyFromCoin(coin: String): Double
        fun getExchangeCurrencyToCoin(coin: String): Double
    }

    interface MarketCallback {
        fun returnDouble() : Double
    }

    class YobitMarket : Market {
        override fun getName(): String {
            return "Yobit"
        }

        override fun getToFee(): Double {
            return 0.2
        }

        override fun getFromFee(): Double {
            return 0.2
        }

        override fun getExchangeCurrencyFromCoin(coin: String): Double {
            return 0.0
        }

        override fun getExchangeCurrencyToCoin(coin: String): Double {
            return 0.0
        }
    }

    class KrakenMarket : Market {

        override fun getName(): String {
            return "Kraken"
        }

        override fun getToFee(): Double {
            return 0.1
        }

        override fun getFromFee(): Double {
            return 0.0
        }

        override fun  getExchangeCurrencyFromCoin(coin: String): Double {
          return 0.0
        }

        override fun getExchangeCurrencyToCoin(coin: String): Double {
            return 0.0
        }
    }

    class BittrexMarket: Market {
        override fun getName(): String {
            return "Bittrex"
        }

        override fun getToFee(): Double {
            return 0.25
        }

        override fun getFromFee(): Double {
            return 0.25
        }

        override fun  getExchangeCurrencyFromCoin(coin: String): Double {

            return 0.0
        }

        override fun getExchangeCurrencyToCoin(coin: String): Double {
            return 0.0
        }
    }
}