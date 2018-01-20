package org.room76.cryptoobserver

import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Alexey on 1/19/18.
 */

interface BittrixApi{

    @GET("public/getmarkets")
    fun getMarkets() : Flowable<ResponseBody>

    @GET("public/getcurrencies")
    fun getCurrencies() : Flowable<List<Model.Currency>>

    @GET("public/getticker")
    fun getTicker() : Flowable<ResponseBody>

    @GET("public/getmarketsummaries")
    fun getMarketSummaries() : Flowable<ResponseBody>

    @GET("public/getmarketsummary")
    fun getMarketSummaries(@Query("market") market:String) : Flowable<ResponseBody>

    @GET("public/getmarkethistory")
    fun getMarketHistory(@Query("market") market:String) : Flowable<ResponseBody>

}