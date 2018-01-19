package org.room76.cryptoobserver

/**
 * Created by Alexey on 1/19/18.
 */

object Model {
    data class Currency(var currency:String,
                        var currencyLong:String,
                        var minConfirmation: Int,
                        var txFee: Float,
                        var isActive: Boolean,
                        var coinType: String,
                        var baseAddress: String)
}