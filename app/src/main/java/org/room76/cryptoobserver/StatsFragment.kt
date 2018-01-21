package org.room76.cryptoobserver

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener


/**
 * Created by Alexey on 1/21/18.
 */
class StatsFragment  : Fragment() {

    private lateinit var mCurrencySelector: Spinner
    private lateinit var mExchangeSelector: Spinner
    private lateinit var mButton: Button
    private lateinit var mTextView: TextView

    val currencies = arrayOf("ETH", "BTC", "DASH", "RPL")
    val exchanges = arrayOf("Kraken", "Yobit", "Bittrix")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_stats,container)
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
            Toast.makeText(context, "Calculate result and set it to result view", Toast.LENGTH_SHORT).show()
            mTextView.text = "Result"
        })

        return root
    }

}