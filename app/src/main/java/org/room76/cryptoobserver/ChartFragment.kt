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
import com.db.chart.animation.Animation
import com.db.chart.model.LineSet
import com.db.chart.renderer.AxisRenderer
import com.db.chart.tooltip.Tooltip
import com.db.chart.util.Tools
import com.db.chart.view.LineChartView
import android.widget.ImageButton
import android.widget.Toast
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
    private val TAG = ChartFragment::class.java.name

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_chart,container)
        mChart = root.findViewById(R.id.chart)
        mUpdateBtn = root.findViewById(R.id.update)
        mUpdateBtn.setOnClickListener {
            updateChart()
        }
        populateChart()
        return root
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    private fun populateChart(){
        requestChartData("ETHUSD", "1440", object : Utils.Action<Model.Chart> {
            override fun action(data: Model.Chart) {
                val labels: MutableList<String> = mutableListOf()
                val values: MutableList<Float> = mutableListOf()
                data.chartData.forEach { it->
                    run {
                        labels.add(it.date.get(Calendar.DATE).toString())
                        values.add(it.average)
                    }
                }
                show(labels.toTypedArray(), values.toFloatArray())
            }
        })
    }

    private fun show(labels: Array<String>, values: FloatArray ) {
        // Tooltip
        mTip = Tooltip(context, R.layout.tooltip, R.id.value)

        mTip.setVerticalAlignment(Tooltip.Alignment.BOTTOM_TOP)
        mTip.setDimensions(Tools.fromDpToPx(70f).toInt(), Tools.fromDpToPx(25f).toInt())

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

    private fun updateChart() {
        requestChartData("ETHUSD", "1440", object : Utils.Action<Model.Chart> {
            override fun action(data: Model.Chart) {
                val values: MutableList<Float> = mutableListOf()
                data.chartData.forEach { it -> values.add(it.average) }
                mChart.dismissAllTooltips()
                mChart.updateValues(0, values.toFloatArray())
                mChart.updateValues(1, values.toFloatArray())
                mChart.chartAnimation.withEndAction({ Log.v(TAG,"chart updated")})
                mChart.notifyDataUpdate()
            }
        })
    }


    private fun dismiss() {
        mChart.dismissAllTooltips()
        mChart.dismiss(Animation().setInterpolator(BounceInterpolator())
                .withEndAction({Log.v(TAG,"animation dissmised")}))
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
                    val chart = Utils.ohlcKrakenToChart(response.body())
                    Log.i(TAG, "Response objects: " + chart.chartData.size)
                    action.action(chart)
                } else {
                    Log.e(TAG, response.errorBody().string())
                    Toast.makeText(context,"Network error",Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}