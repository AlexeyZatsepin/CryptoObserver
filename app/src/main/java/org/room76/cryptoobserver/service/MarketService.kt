package org.room76.cryptoobserver.service

import org.room76.cryptoobserver.Model

class MarketService {
    companion object {
        fun calculateProfitForMarket(from: Model.Market,
                                     amountOfCoins: Double,
                                     coin: String,
                                     to: Model.Market): Double {
            var usdFrom = from.getExchangeCurrencyFromCoin(coin) * amountOfCoins - from.getFromFee()
            var coinTo = to.getExchangeCurrencyToCoin(coin) * usdFrom - to.getToFee()
            return amountOfCoins - coinTo //profit
        }
    }
}