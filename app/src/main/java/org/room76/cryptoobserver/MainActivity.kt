package org.room76.cryptoobserver

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var stream = App.bittrexApi.getCurrencies("usdt-btc")
        stream.enqueue(object : Callback<Model.BittrixMarketSummary> {
            override fun onFailure(call: Call<Model.BittrixMarketSummary>?, t: Throwable?) {
                Log.v("App", t.toString())
            }

            override fun onResponse(call: Call<Model.BittrixMarketSummary>?, response: Response<Model.BittrixMarketSummary>?) {
                Log.v("App", response?.body().toString())
            }
        })

    }
}
