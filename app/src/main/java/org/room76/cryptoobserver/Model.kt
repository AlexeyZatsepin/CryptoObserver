package org.room76.cryptoobserver

import com.google.gson.annotations.SerializedName

/**
 * Created by Alexey on 1/19/18.
 */

object Model {
    data class Currency(@SerializedName("Currency") var currency:String,
                        @SerializedName("CurrencyLong") var currencyLong:String,
                        @SerializedName("MinConfirmation") var minConfirmation: Int,
                        @SerializedName("TxFee") var txFee: Float,
                        @SerializedName("IsActive") var isActive: Boolean,
                        @SerializedName("CoinType") var coinType: String,
                        @SerializedName("BaseAddress") var baseAddress: String)
}