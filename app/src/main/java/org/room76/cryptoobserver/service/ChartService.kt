package org.room76.cryptoobserver.service

import android.util.Log
import org.room76.cryptoobserver.App
import org.room76.cryptoobserver.Model
import org.room76.cryptoobserver.util.ModelsConverter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChartService {

    fun getChartData(pair: String, interval: String, lastCall: String?) {
        var response = App.krakenApi.getOlhc(pair, interval, lastCall)

        response.enqueue(object : Callback<Model.OhlcKraken> {
            override fun onFailure(call: Call<Model.OhlcKraken>?, t: Throwable?) {
                Log.e("App Error", t.toString())
            }

            override fun onResponse(call: Call<Model.OhlcKraken>?,
                                    response: Response<Model.OhlcKraken>?) {
                var chart = ModelsConverter.ohlcKrakenToChart(response!!.body())
                //todo show chart on phone.
            }
        })
    }
}