package com.example.mindfriendfront


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.Entry

class AnalysisGraph_fragment : Fragment() {

    companion object {
        fun newInstance(): AnalysisGraph_fragment {
            return AnalysisGraph_fragment()
        }
    }


    var v: View? = null
    var lineChart: LineChart? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_analysis_graph_fragment, container, false)
        lineChart = v!!.findViewById<View>(R.id.chart) as LineChart
        val entries: MutableList<Entry> = ArrayList()
        entries.add(Entry(1f, 1f))
        entries.add(Entry(2f, 2f))
        entries.add(Entry(3f, 3f))
        entries.add(Entry(4f, 4f))
        entries.add(Entry(5f, 5f))
        val dataSet = LineDataSet(entries, "Label")
        dataSet.lineWidth = 4f //라인 두께
        dataSet.circleRadius = 6f // 점 크기
        dataSet.setCircleColor(Color.parseColor("#FFA1B4DC")) // 점 색깔
        dataSet.setDrawCircleHole(true) // 원의 겉 부분 칠할거?
        dataSet.color = Color.parseColor("#FFA1B4DC") // 라인 색깔
        val lineData = LineData(dataSet)
        lineChart!!.data = lineData
        lineChart!!.invalidate()
        return v
    }
}