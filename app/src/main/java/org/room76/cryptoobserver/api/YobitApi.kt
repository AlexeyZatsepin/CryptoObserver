package org.room76.cryptoobserver.api

import okhttp3.ResponseBody
import org.room76.cryptoobserver.Model
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YobitApi {

    @GET("public/getmarkets")
    fun getMarkets(): Call<ResponseBody>

    @GET("public/getticker")
    fun getTicker(): Call<ResponseBody>

    @GET("public/getmarketsummaries")
    fun getMarketSummaries(): Call<ResponseBody>

    @GET("public/getmarketsummary")
    fun getMarketSummaries(@Query("market") market: String): Call<ResponseBody>

    @GET("public/getmarkethistory")
    fun getMarketHistory(@Query("market") market: String): Call<ResponseBody>
}