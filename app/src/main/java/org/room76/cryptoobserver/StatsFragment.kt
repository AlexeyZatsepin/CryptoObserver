package org.room76.cryptoobserver

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import kotlin.collections.ArrayList

class StatsFragment : Fragment() {

    private lateinit var mCurrencySelector: Spinner
    private lateinit var mExchangeSelector: Spinner
    private lateinit var mButton: Button
    private lateinit var mTextView: TextView

    val currencies = arrayOf("ETH", "BTC", "DASH", "RPL")
    val exchanges = arrayOf("Kraken", "Yobit", "Bittrix")

    private var lock = Any()
    private var responeses = ArrayList<MarketCurrency>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_stats, container)
        val adapter = ArrayAdapter<String>(context, R.layout.spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        mCurrencySelector = root.findViewById(R.id.currency)
        mCurrencySelector.adapter = adapter
        mCurrencySelector.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View,
                                        position: Int, id: Long) {
                Toast.makeText(context, currencies[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(arg0: AdapterView<*>) {}
        }

        val adapterEx = ArrayAdapter(context, R.layout.spinner_item, exchanges)
        adapterEx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        mExchangeSelector = root.findViewById(R.id.exchange)
        mExchangeSelector.adapter = adapterEx
        mExchangeSelector.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View,
                                        position: Int, id: Long) {
                Toast.makeText(context, exchanges[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(arg0: AdapterView<*>) {}
        }

        mButton = root.findViewById(R.id.button)
        mTextView = root.findViewById(R.id.result)

        mButton.setOnClickListener({
            Toast.makeText(context, "Calculate result and set it to result view",
                    Toast.LENGTH_SHORT).show()
            responeses = ArrayList()
            mTextView.text = "Result: "
            val selectedMarket = Model.MarketFactory()
                    .getMarketForName(mExchangeSelector.selectedItem.toString())
            val coin = mCurrencySelector.selectedItem.toString()
            for (ex in exchanges) {
                var market = Model.MarketFactory().getMarketForName(ex)
                market.updateCurrencyCall(coin, object : Callback<Model.Currency> {
                    override fun onFailure(call: Call<Model.Currency>?, t: Throwable?) {
                    }

                    override fun onResponse(call: Call<Model.Currency>?, response: Response<Model.Currency>?) {
                        val currency = response!!.body().getCurrency()
                        if (market.getName().equals(selectedMarket.getName())) {
                            updateView(MarketCurrency(market, currency, true))
                        } else {
                            updateView(MarketCurrency(market, currency, false))
                        }
                    }
                })
            }
        })


        return root
    }

    fun updateView(marketCurrency: MarketCurrency) {
        synchronized(lock) {
            responeses.add(marketCurrency)
            if (responeses.size == exchanges.size - 1) {
                for (fromMarket in responeses) {
                    if (fromMarket.from) {
                        for (market in responeses) {
                            if (!market.equals(fromMarket)) {
                                var profit = isProfit(fromMarket, market)
                                updateText(profit, fromMarket, market)
                            }
                        }
                        break
                    }
                }

            }
        }
    }

    fun isProfit(fromMarket: MarketCurrency, market: MarketCurrency): Boolean {
        var profit = (fromMarket.currency - fromMarket.currency * fromMarket.market.getFromFee())
        profit = profit / market.currency - market.market.getToFee() * profit
        return profit > 1
    }

    fun updateText(profit: Boolean, fromMarket: MarketCurrency, market: MarketCurrency) {
        if (profit) {
            val s = String
                    .format("You will make profit transferring %s from exchanger %s to exchanger %s",
                            mCurrencySelector.selectedItem.toString(),
                            fromMarket.market.getName(), market.market.getName())
            mTextView.text = mTextView.text.toString() + s
        } else {
            val s = String
                    .format("You will make no profit transferring %s from exchanger %s to exchanger %s",
                            mCurrencySelector.selectedItem.toString(),
                            fromMarket.market.getName(), market.market.getName())
            mTextView.text = mTextView.text.toString() + s
        }
    }

    class MarketCurrency(var market: Model.Market, var currency: Double, var from: Boolean) {
    }
}