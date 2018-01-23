package org.room76.cryptoobserver

import com.google.gson.annotations.SerializedName
import retrofit2.Callback
import java.lang.IllegalArgumentException
import java.util.*

object Model {
    data class KrakenEthMarketSummary(
            @SerializedName("erorr") var error: List<String>,
            @SerializedName("result") var result: KrakenEthResult
    )
    data class KrakenEthResult(
            @SerializedName("XETHZUSD") var xethzusd: List<KrakenXETHZUSD>
    )
    data class KrakenXETHZUSD(
            @SerializedName("asks")var asks: List<String>
    )
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

    interface Currency {
        fun getCurrency(): Double
    }

    class BittrixCurrency(result: BittrixMarketResult) : Currency {
        var result: BittrixMarketResult = result
        override fun getCurrency(): Double {
            return this.result.last
        }

    }

    interface Market {
        fun getName(): String
        fun getToFee(): Double
        fun getFromFee(): Double
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

        fun updateCurrencyCall(coin: String, call: Callback<Any>) {

        }
    }

    class KrakenMarket : Market {

        val coinsNames = Utils.krakenCoins()

        override fun getName(): String {
            return "Kraken"
        }

        override fun getToFee(): Double {
            return 0.1
        }

        override fun getFromFee(): Double {
            return 0.0
        }

        fun updateCurrencyCall(coin: String, call: Callback<OhlcKraken>) {
            val pair = coinsNames.get(coin)
            if (pair == null) {
                throw IllegalArgumentException("There are no" + coin + " Bittrex market")
            }
            App.krakenApi.getOlhc(pair, null, null).enqueue(call)
        }
    }

    class BittrexMarket : Market {
        val coinsNames = Utils.bittrixCoins()
        override fun getName(): String {
            return "Bittrix"
        }

        override fun getToFee(): Double {
            return 0.25
        }

        override fun getFromFee(): Double {
            return 0.25
        }

        fun updateCurrencyCall(coin: String, call: Callback<BittrixMarketSummary>) {
            val pair = coinsNames.get(coin)
            if (pair == null) {
                throw IllegalArgumentException("There are no" + coin + " Bittrex market")
            }
            App.bittrexApi.getCurrencies(pair).enqueue(call)
        }
    }
}
