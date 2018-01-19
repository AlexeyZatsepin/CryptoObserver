package org.room76.cryptoobserver

import android.app.Application
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Alexey on 1/19/18.
 */

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        bittrexApi = Retrofit.Builder()
                .baseUrl(BITTREX_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    companion object {
        lateinit var instance: App
            private set
        val BITTREX_URL = "https://bittrex.com/api/v1.1/"
        lateinit var bittrexApi: Retrofit
    }


}