package org.room76.cryptoobserver.api

import org.room76.cryptoobserver.Model
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface KrakenApi {
    @GET("public/OHLC")
    fun getOlhc(@Query("pair") pair: String, @Query("interval") interval: String?,
                @Query("since") since: String?): Call<Model.OhlcKraken>

    @GET("public/Depth")
    fun getCurrencies(@Query("pair") pair: String): Call<Model.KrakenEthMarketSummary>
}