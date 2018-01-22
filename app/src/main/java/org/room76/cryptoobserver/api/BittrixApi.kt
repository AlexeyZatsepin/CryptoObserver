package org.room76.cryptoobserver.api

import okhttp3.ResponseBody
import org.room76.cryptoobserver.Model
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BittrixApi {

    @GET("public/getmarketsummary")
    fun getCurrencies(@Query("market") market: String): Call<Model.BittrixMarketSummary>

}