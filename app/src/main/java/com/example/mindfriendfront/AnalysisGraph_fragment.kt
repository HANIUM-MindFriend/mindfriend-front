package com.example.mindfriendfront


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mindfriendfront.data.DashboardResponse
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.util.Calendar
import com.example.mindfriendfront.network.ApiServiceFactory
import com.github.mikephil.charting.components.XAxis
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
//        그래프 표기 위한 수치
private var angryN = 0.0f;
    private var disgustN=0f;
    private var fearN=0f;
    private var happinessN=0f;
    private var neutralN=0f;
    private var  sadnessN=0f;
    private var surpriseN=0f;
    var v: View? = null
    var barChart: BarChart? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_analysis_graph_fragment, container, false)
        barChart = v!!.findViewById<View>(R.id.chart) as BarChart // 레이아웃에서 바차트 뷰 아이디로 변경해야 합니다.

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


        val yymm:String

        val apiService = ApiServiceFactory.apiService
        if (currentMonth<10){
            yymm = "${currentYear}-0${currentMonth}"
        }
        else{
            yymm = "${currentYear}-${currentMonth}"
        }



        apiService.getDashboard(yearMonth=yymm).enqueue(object : Callback<DashboardResponse> {

            override fun onResponse(call: Call<DashboardResponse>, response: Response<DashboardResponse>) {
                Log.e("Diarydate", yymm)

                if (response.isSuccessful) {
                    val emoGraphResponse = response.body()
                    val emoGraphData = emoGraphResponse?.data?.graphStatistics

                    emoGraphData?.let {
                        angryN = (it.angry).toFloat()
                        disgustN = (it.disgust).toFloat()
                        fearN = (it.fear).toFloat()
                        happinessN = (it.happiness).toFloat()
                        neutralN = (it.neutral).toFloat()
                        sadnessN = (it.sadness).toFloat()
                        surpriseN = (it.surprise).toFloat()
                          }
                    Log.e("API_RESPONSE", "성공")
                    val entries: MutableList<BarEntry> = ArrayList()
                    entries.add(BarEntry(0f, angryN, "angry"))
                    entries.add(BarEntry(1f, disgustN, "disgust"))
                    entries.add(BarEntry(2f, fearN, "fear"))
                    entries.add(BarEntry(3f, happinessN, "happiness"))
                    entries.add(BarEntry(4f, neutralN, "neutral"))
                    entries.add(BarEntry(4f, sadnessN, "sadness"))
                    entries.add(BarEntry(4f, surpriseN, "surprise"))
                    val dataSet = BarDataSet(entries, null) // 색 샘플 부분을 삭제

                    val colors = listOf(
                        Color.parseColor("#FFF59E"),
                        Color.parseColor("#FF9B7B"),
                        Color.parseColor("#9CD0FF"),
                        Color.parseColor("#C2EC67"),
                        Color.parseColor("#E2CCFF"),
                        Color.parseColor("#C2EC67"),
                        Color.parseColor("#FF9B7B")
                    )
                    dataSet.colors = colors

                    val barData = BarData(dataSet)
                    barChart!!.data = barData
                    val labels = listOf("화남", "역겨움", "공포", "행복","평온함", "슬픔","놀람")
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

                        // Set X-axis labels
                        val xAxisFormatter = IndexAxisValueFormatter(labels)
                        xAxis.valueFormatter = xAxisFormatter
                        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
                        xAxis.granularity = 1f

                        // Set bar label above the bars
                        dataSet.setDrawValues(true) // 막대 위에 레이블 표시 활성화
                        dataSet.valueTextColor = Color.BLACK // 레이블 텍스트 색상 설정
                        dataSet.valueTextSize = 12f // 레이블 텍스트 크기 설정

                        // Disable x-axis and y-axis labels
                        xAxis.isEnabled = true
                        axisLeft.isEnabled = false
                        axisRight.isEnabled = false
                        barData.barWidth = 0.4f

                        // Invalidate the chart to apply changes
                        invalidate()
                    }
                } else {
                    // 오류 처리
                    val errorBody = response.errorBody()?.string()
                    val message = "응답 코드: ${response.code()}, 메시지: ${response.message()}, 오류 내용: $errorBody"
                    Log.e("API_RESPONSE", message)
                    Toast.makeText(requireContext(), "오류가 발생했습니다. Error Code: "+response.code(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                // 네트워크 오류 처리
                Log.e("API_RESPONSE", "네트워크 실패: ${t.message}")
                Toast.makeText(requireContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        })






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