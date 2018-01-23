package org.room76.cryptoobserver

import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.*
import com.db.chart.animation.Animation
import com.db.chart.model.LineSet
import com.db.chart.renderer.AxisRenderer
import com.db.chart.tooltip.Tooltip
import com.db.chart.util.Tools
import com.db.chart.view.LineChartView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by Alexey on 1/20/18.
 */
class ChartFragment : Fragment() {

    private lateinit var mChart: LineChartView
    private lateinit var mTip: Tooltip
    private lateinit var mUpdateBtn: ImageButton
    private lateinit var mCurrencySelector: Spinner
    private lateinit var mIntervalSelector: Spinner
    private lateinit var mFrom: TextView
    private lateinit var mTo: TextView
    private val TAG = ChartFragment::class.java.name

    private val currencies = arrayOf("ETH/USD", "DASH/USD", "BCH/USD", "XRP/USD", "LTC/USD")
    private val intervals = arrayOf("1", "5", "15", "30", "60", "240", "1440", "10080", "21600") // minutes

    private val start : Calendar = Calendar.getInstance()
    private val end : Calendar = Calendar.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_chart,container)

        start.time = Date(System.currentTimeMillis())
        start.set(start.time.year,start.time.month,start.time.day-7)
        end.time = Date(System.currentTimeMillis())

        mChart = root.findViewById(R.id.chart)
        mUpdateBtn = root.findViewById(R.id.update)
        mUpdateBtn.setOnClickListener {
//            populateChart(currencies[mCurrencySelector.selectedItemPosition]
//                    ,intervals[mIntervalSelector.selectedItemPosition])
        }
        mFrom = root.findViewById(R.id.from)
        mTo = root.findViewById(R.id.to)
        mFrom.text = Utils.dateFormatWithYear.format(start.time)
        mTo.text = Utils.dateFormatWithYear.format(end.time)

        populateChart(currencies[0],intervals[6])

        val adapter = ArrayAdapter<String>(context, R.layout.spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        mCurrencySelector = root.findViewById(R.id.currency)
        mCurrencySelector.adapter = adapter
        mCurrencySelector.setSelection(0)
        mCurrencySelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View,
                                        position: Int, id: Long) {
                if (mIntervalSelector.selectedItemPosition > 0){
//                    dismiss()
//                    updateChart(currencies[position],intervals[mIntervalSelector.selectedItemPosition])
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>) {}
        }

        val adapterEx = ArrayAdapter(context, R.layout.spinner_item, intervals)
        adapterEx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        mIntervalSelector = root.findViewById(R.id.interval)
        mIntervalSelector.adapter = adapterEx
        mIntervalSelector.setSelection(6)
        mIntervalSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View,
                                        position: Int, id: Long) {
                if (mCurrencySelector.selectedItemPosition > 0){
//                    dismiss()
//                    updateChart(currencies[mCurrencySelector.selectedItemPosition],intervals[position])
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>) {}
        }
        return root
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    private fun populateChart(pair: String, interval:String){
        requestChartData(pair.replace("/",""), interval, object : Utils.Action<Model.Chart> {
            override fun action(data: Model.Chart) {
                val labels: MutableList<String> = mutableListOf()
                val values: MutableList<Float> = mutableListOf()
                data.chartData.filter { it.date.timeInMillis > start.timeInMillis }
                        .filter { it.date.timeInMillis < end.timeInMillis }
                        .forEach { it->
                            run {
                                labels.add(Utils.dateFormat.format(it.date.time))
                                values.add(it.average)
                            }
                }
                show(labels.toTypedArray(), values.toFloatArray())
            }
        })
    }

    private fun show(labels: Array<String>, values: FloatArray ) {
        Log.i(TAG,"showing " + values.size + "elements")
        // Tooltip
        mTip = Tooltip(context, R.layout.tooltip, R.id.value)

        mTip.setVerticalAlignment(Tooltip.Alignment.BOTTOM_TOP)
        mTip.setDimensions(Tools.fromDpToPx(80f).toInt(), Tools.fromDpToPx(25f).toInt())

        mTip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f),
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)).duration = 200

        mTip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f),
                PropertyValuesHolder.ofFloat(View.SCALE_X, 0f)).duration = 200

        mTip.pivotX = Tools.fromDpToPx(65f) / 2
        mTip.pivotY = Tools.fromDpToPx(25f)

        // Data
        var dataset = LineSet(labels, values)
        dataset.setColor(ContextCompat.getColor(context, R.color.chartColorNewItem))
                .setFill(ContextCompat.getColor(context, R.color.chartBackground))
                .setDotsColor(ContextCompat.getColor(context, R.color.chartColorNewItem))
                .setThickness(4f)
                .setDashed(floatArrayOf(10f, 10f))
                .beginAt(labels.count()-2)
        mChart.addData(dataset)

        dataset = LineSet(labels, values)
        dataset.setColor(ContextCompat.getColor(context, R.color.chartLineColor))
                .setFill(ContextCompat.getColor(context, R.color.chartBackground))
                .setDotsColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setThickness(4f)
                .endAt(labels.count()-1)
        mChart.addData(dataset)

        val chartAction = Runnable {
            mTip.prepare(mChart.getEntriesArea(0)[3], values[3])
            mChart.showTooltip(mTip, true)
        }

        val maxY: Float = values.max()!!
        mChart.setAxisBorderValues(0f, maxY + maxY * 0.1f)
                .setYLabels(AxisRenderer.LabelPosition.NONE)
                .setTooltips(mTip)
                .show(Animation().setInterpolator(BounceInterpolator())
                        .fromAlpha(0)
                        .withEndAction(chartAction))
    }

    private fun updateChart(pair: String, interval: String) {
        requestChartData(pair.replace("/",""), interval, object : Utils.Action<Model.Chart> {
            override fun action(data: Model.Chart) {
                val values: MutableList<Float> = mutableListOf()
                data.chartData.filter { it.date.timeInMillis > start.timeInMillis }
                        .filter { it.date.timeInMillis < end.timeInMillis }
                        .forEach { it -> values.add(it.average) }
                mChart.dismissAllTooltips()
                mChart.updateValues(0, values.toFloatArray())
                mChart.updateValues(1, values.toFloatArray())
                mChart.chartAnimation.withEndAction({ Log.v(TAG,"chart updated")})
                mChart.notifyDataUpdate()
            }
        })
    }


    private fun dismiss() {
        if (mChart.childCount > 0) {
            mChart.dismissAllTooltips()
            mChart.dismiss(Animation().setInterpolator(BounceInterpolator())
                    .withEndAction({Log.v(TAG,"animation dissmised")}))
            mChart.removeAllViewsInLayout()
        }
    }

    private fun requestChartData(pair: String, interval:String, action: Utils.Action<Model.Chart>) {
        val response = App.krakenApi.getOlhc(pair, interval, null)

        response.enqueue(object : Callback<Model.OhlcKraken> {
            override fun onFailure(call: Call<Model.OhlcKraken>?, t: Throwable?) {
                Log.e(TAG, t!!.message)
                Toast.makeText(context,"No network",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Model.OhlcKraken>,
                                    response: Response<Model.OhlcKraken>) {
                if (response.isSuccessful){
                    val chart: Model.Chart = Utils.ohlcKrakenToChart(response.body())
                    if (chart.chartData.isEmpty()) {
                        Toast.makeText(context,"No data",Toast.LENGTH_LONG).show()
                    }else {
                        Log.i(TAG, "Response objects: " + chart.chartData.size)
                        action.action(chart)
                    }
                } else {
                    Log.e(TAG, response.errorBody().string())
                    Toast.makeText(context,"Network error",Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}