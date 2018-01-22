package org.room76.cryptoobserver

import com.google.gson.annotations.SerializedName
import retrofit2.Callback
import java.lang.IllegalArgumentException
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
        fun updateCurrencyCall(coin: String, call: Callback<Currency>): Double
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

        override fun updateCurrencyCall(coin: String, call: Callback<Currency>): Double {
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

        override fun updateCurrencyCall(coin: String, call: Callback<Currency>): Double {
            return 0.0
        }
    }

    class BittrexMarket : Market {
        override fun getName(): String {
            return "Bittrex"
        }

        override fun getToFee(): Double {
            return 0.25
        }

        override fun getFromFee(): Double {
            return 0.25
        }

        override fun updateCurrencyCall(coin: String,
                                        call: Callback<Currency>): Double {

            return 0.0
        }
    }

    class MarketFactory() {

        fun getMarketForName(name: String): Market {
            if (name.equals("Bittrix")) {
                return BittrexMarket()
            }
            if (name.equals("Yobit")) {
                return YobitMarket()
            }
            if (name.equals("Kraken")) {
                return KrakenMarket()
            }
            throw IllegalArgumentException("No market with name " + name)
        }
    }
}
