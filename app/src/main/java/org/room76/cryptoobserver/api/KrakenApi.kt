package org.room76.cryptoobserver.api

import com.google.gson.annotations.Since
import io.reactivex.Flowable
import okhttp3.ResponseBody
import org.room76.cryptoobserver.Model
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface KrakenApi {
    @GET("public/OHLC")
    fun getOlhc(@Query("pair") pair: String, @Query("interval") interval: String?,
                @Query("since") since: String?): Call<Model.OhlcKraken>
}