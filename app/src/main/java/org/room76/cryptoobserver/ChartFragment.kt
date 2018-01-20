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

/**
 * Created by Alexey on 1/20/18.
 */
class ChartFragment : Fragment() {

    private lateinit var mChart: LineChartView
    private lateinit var mTip: Tooltip
    private lateinit var mUpdateBtn: ImageButton
    private var firstStage: Boolean = false
    private val TAG = ChartFragment::class.java.name

    private val mLabels = arrayOf("Jan", "Fev", "Mar", "Apr", "Jun", "May", "Jul", "Aug", "Sep")

    private val mValues = arrayOf(
            floatArrayOf(3.5f, 4.7f, 4.3f, 8f, 6.5f, 9.9f, 7f, 8.3f, 7.0f),
            floatArrayOf(4.5f, 2.5f, 2.5f, 20f, 4.5f, 9.5f, 5f, 8.3f, 30.8f))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_graphs,container)
        mChart = root.findViewById(R.id.chart)
        mUpdateBtn = root.findViewById(R.id.update)
        mUpdateBtn.setOnClickListener {
            update(if (firstStage) mValues[0] else mValues[1])
            firstStage = !firstStage
        }
        show(mLabels, mValues[0])
        return root
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    private fun show(labels: Array<String>, values: FloatArray ) {
        firstStage = false
        // Tooltip
        mTip = Tooltip(context, R.layout.tooltip, R.id.value)

        mTip.setVerticalAlignment(Tooltip.Alignment.BOTTOM_TOP)
        mTip.setDimensions(Tools.fromDpToPx(58f).toInt(), Tools.fromDpToPx(25f).toInt())

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
                .beginAt(mLabels.count()-2)
        mChart.addData(dataset)

        dataset = LineSet(labels, values)
        dataset.setColor(ContextCompat.getColor(context, R.color.chartLineColor))
                .setFill(ContextCompat.getColor(context, R.color.chartBackground))
                .setDotsColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setThickness(4f)
                .endAt(mLabels.count()-1)
        mChart.addData(dataset)

        val chartAction = Runnable {
            mTip.prepare(mChart.getEntriesArea(0)[3], mValues[0][3])
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

    private fun update(values: FloatArray) {
        mChart.dismissAllTooltips()
        mChart.updateValues(0, values)
        mChart.updateValues(1, values)
        mChart.chartAnimation.withEndAction({ Log.v(TAG,"chart updated")})
        mChart.notifyDataUpdate()
    }


    private fun dismiss() {
        mChart.dismissAllTooltips()
        mChart.dismiss(Animation().setInterpolator(BounceInterpolator())
                .withEndAction({Log.v(TAG,"animation dissmised")}))
    }
}