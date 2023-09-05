package com.example.mindfriendfront


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.Entry
import java.util.Calendar

class AnalysisGraph_fragment : Fragment() {

    companion object {
        fun newInstance(): AnalysisGraph_fragment {
            return AnalysisGraph_fragment()
        }
    }

    private lateinit var yymmTextView: TextView
    private lateinit var upButton: ImageButton
    private lateinit var downButton: ImageButton
    private var currentYear=0
    private var currentMonth=0
//    val (currentYear, currentMonth) = getCurrentYearAndMonth()

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
        yymmTextView = v!!.findViewById(R.id.yymm)
        upButton = v!!.findViewById(R.id.upButton)
        downButton = v!!.findViewById(R.id.downButton)
        getCurrentYearAndMonth()
        updateYymmText()

        // Up 버튼 클릭 시 처리
        upButton.setOnClickListener {
            // 월을 증가하고, 12월 이후에는 다음 연도로 이동
            currentMonth++
            if (currentMonth > 12) {
                currentMonth = 1
                currentYear++
            }
            updateYymmText()
        }

        // Down 버튼 클릭 시 처리
        downButton.setOnClickListener {
            // 월을 감소하고, 1월 이전에는 이전 연도로 이동
            currentMonth--
            if (currentMonth < 1) {
                currentMonth = 12
                currentYear--
            }
            updateYymmText()
        }

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
    private fun updateYymmText() {
        // TextView에 현재 연도와 월을 표시
        val yymmText = "${currentYear}년 ${currentMonth}월"
        yymmTextView.text = yymmText
    }
    // 현재 년도와 월을 가져오는 함수
    fun getCurrentYearAndMonth(): Pair<Int, Int> {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // 월은 0부터 시작하므로 +1
        currentYear=year
        currentMonth=month
        return Pair(year, month)
    }

    // 예제에서 사용하는 곳에서 호출하여 값을 얻을 수 있음

}