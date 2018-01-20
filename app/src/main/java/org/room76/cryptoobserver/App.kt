package org.room76.cryptoobserver

import android.app.Application
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import org.room76.cryptoobserver.api.BittrixApi
import org.room76.cryptoobserver.api.KrakenApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Alexey on 1/19/18.
 */

class App: Application() {
    companion object {
        lateinit var instance: App
            private set
        val BITTREX_URL = "https://bittrex.com/api/v1.1/"
        val KRAKEN_URL = "https://api.kraken.com/0/"
        lateinit var bittrexApi: BittrixApi
        lateinit var krakenApi: KrakenApi
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        bittrexApi = buildRetrofit(BITTREX_URL).create(BittrixApi::class.java)
        krakenApi = buildRetrofit(KRAKEN_URL).create(KrakenApi::class.java)
    }

    private fun buildRetrofit(url: String) : Retrofit {
        return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }
}