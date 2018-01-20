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
//        val stream = App.krakenApi.getOlhc("ETHUSD", "5", null)
//        stream.enqueue(object : Callback<Model.OhlcKraken> {
//            override fun onFailure(call: Call<Model.OhlcKraken>?, t: Throwable?) {
//                Log.v("App", t.toString())
//            }
//
//            override fun onResponse(call: Call<Model.OhlcKraken>?, response: Response<Model.OhlcKraken>?) {
//                Log.v("App", response?.body().toString())
//                Utils.ohlcKrakenToChart(response!!.body())
////                Log.v("App", "Hello world")
//            }
//        })

    }
}
