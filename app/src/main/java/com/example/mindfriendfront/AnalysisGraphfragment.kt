package com.example.mindfriendfront


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class AnalysisGraphfragment : Fragment() {

    companion object {
        fun newInstance(): AnalysisGraphfragment {
            return AnalysisGraphfragment()
        }
    }

    var v: View? = null
    var barChart: BarChart? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_analysis_info, container, false)
        barChart = v!!.findViewById<View>(R.id.chart) as BarChart // 레이아웃에서 바차트 뷰 아이디로 변경해야 합니다.

        val entries: MutableList<BarEntry> = ArrayList()
        entries.add(BarEntry(0f, 2f, "즐거움"))
        entries.add(BarEntry(1f, 3f, "우울함"))
        entries.add(BarEntry(2f, 2f, "편안함"))
        entries.add(BarEntry(3f, 4f, "두려움"))
        entries.add(BarEntry(4f, 1f, "무기력함"))
        val dataSet = BarDataSet(entries, null) // 색 샘플 부분을 삭제

        val colors = listOf(
            Color.parseColor("#FFF59E"),
            Color.parseColor("#FF9B7B"),
            Color.parseColor("#9CD0FF"),
            Color.parseColor("#C2EC67"),
            Color.parseColor("#E2CCFF")
        )
        dataSet.colors = colors

        val barData = BarData(dataSet)
        barChart!!.data = barData
        val labels = listOf("즐거움", "우울함", "편안함", "두려움", "무기력함")
        dataSet.valueFormatter = IndexAxisValueFormatter(labels)

        // Customize the chart appearance
        barChart!!.apply {
            // Disable grid lines
            xAxis.setDrawGridLines(false)
            axisLeft.setDrawGridLines(false)
            axisRight.setDrawGridLines(false)

            // Disable description label
            description.isEnabled = false
            legend.isEnabled = false
            // Disable x-axis and y-axis labels
            xAxis.isEnabled = false
            axisLeft.isEnabled = false
            axisRight.isEnabled = false
            barData.barWidth = 0.4f
            // Invalidate the chart to apply changes
            invalidate()
        }

        return v
    }


}