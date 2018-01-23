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
import java.lang.IllegalArgumentException

class StatsFragment : Fragment() {

    private lateinit var mCurrencySelector: Spinner
    private lateinit var mExchangeSelector: Spinner
    private lateinit var mButton: Button
    private lateinit var mTextView: TextView

    val currencies = arrayOf("ETH")
    val exchanges = arrayOf("Kraken", "Bittrix")

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
            val selectedMarket = getMarketForName(mExchangeSelector.selectedItem.toString())
            val coin = mCurrencySelector.selectedItem.toString()
            for (ex in exchanges) {
                try {
                    var market = getMarketForName(ex)
                    setMarketCallback(coin, market, isSelectedMarket(market))
                } catch (e: IllegalArgumentException) {
                    mTextView.text = mTextView.text.toString() + e.message + "\n"
                }
            }
        })
        return root
    }

    fun updateView(marketCurrency: MarketCurrency) {
            responeses.add(marketCurrency)
            if (responeses.size == exchanges.size) {
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

    fun isProfit(fromMarket: MarketCurrency, market: MarketCurrency): Boolean {
        var profit = (fromMarket.currency - fromMarket.currency * fromMarket.market.getFromFee())
        profit = profit / market.currency - market.market.getToFee() * profit
        return profit > 1
    }

    fun updateText(profit: Boolean, fromMarket: MarketCurrency, market: MarketCurrency) {
        if (profit) {
            val s = String
                    .format("You will make profit transferring %s from exchanger %s to exchanger %s \n",
                            mCurrencySelector.selectedItem.toString(),
                            fromMarket.market.getName(), market.market.getName())
            mTextView.text = mTextView.text.toString() + s
        } else {
            val s = String
                    .format("You will make no profit transferring %s from exchanger %s to exchanger %s \n",
                            mCurrencySelector.selectedItem.toString(),
                            fromMarket.market.getName(), market.market.getName())
            mTextView.text = mTextView.text.toString() + s
        }
    }

    fun getMarketForName(name: String): Model.Market {
        if (name.equals("Bittrix")) {
            return Model.BittrexMarket()
        }
        if (name.equals("Yobit")) {
            return Model.YobitMarket()
        }
        if (name.equals("Kraken")) {
            return Model.KrakenMarket()
        }
        throw IllegalArgumentException("No market with name " + name)
    }

    fun setMarketCallback(coin: String, market: Model.Market, fromMarket: Boolean) {
        val name = market.getName()
        if (name.equals("Bittrix")) {
            market as Model.BittrexMarket
            market.updateCurrencyCall(coin, object : Callback<Model.BittrixMarketSummary> {
                override fun onFailure(call: Call<Model.BittrixMarketSummary>?, t: Throwable?) {

                }

                override fun onResponse(call: Call<Model.BittrixMarketSummary>?, response: Response<Model.BittrixMarketSummary>?) {
                    val currency = response!!.body().result.get(0).last

                    updateView(MarketCurrency(market, currency,
                            fromMarket))
                }
            })
            return
        }

        if (name.equals("Yobit")) {
            return
        }
        if (name.equals("Kraken")) {
            market as Model.KrakenMarket
            market.updateCurrencyCall(coin, object : Callback<Model.KrakenEthMarketSummary> {
                override fun onFailure(call: Call<Model.KrakenEthMarketSummary>?, t: Throwable?) {

                }

                override fun onResponse(call: Call<Model.KrakenEthMarketSummary>?, response: Response<Model.KrakenEthMarketSummary>?) {
                    val currency = response!!.body().result.xethzusd[0].asks[0].toDouble()

                    updateView(MarketCurrency(market, currency,
                            fromMarket))
                }
            })
            return
        }
        throw IllegalArgumentException("No market with name " + name)
    }

    fun isSelectedMarket(market: Model.Market): Boolean {
        return market.toString().equals(mExchangeSelector.selectedItem.toString())
    }
}

class MarketCurrency(var market: Model.Market, var currency: Double, var from: Boolean) {
}



