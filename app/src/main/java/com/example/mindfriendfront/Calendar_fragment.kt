package com.example.mindfriendfront
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mindfriendfront.data.CalendarResponse
import com.example.mindfriendfront.data.DiaryGetResponse
import com.example.mindfriendfront.network.ApiServiceFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar

class Calendar_fragment : Fragment() {

    private lateinit var calendarView: CalendarView
    private lateinit var writeBtn: ImageButton
    private lateinit var readBtn: Button
    private var selectedDate: Long = 0

    override fun onCreateView(


        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_calendar, container, false)

        calendarView = rootView.findViewById(R.id.calendarView)
        writeBtn = rootView.findViewById(R.id.writeBtn)
        readBtn = rootView.findViewById(R.id.readBtn)

        // 배경색 동적 변경
        val desiredColor = Color.parseColor("#0C2A64") // 원하는 색상으로 변경
        readBtn.setBackgroundColor(desiredColor)

        // 이미지 버튼 크기를 이미지의 크기에 맞게 조정
        val writeDrawable = resources.getDrawable(R.drawable.write)
        writeBtn.layoutParams.width = writeDrawable.intrinsicWidth
        writeBtn.layoutParams.height = writeDrawable.intrinsicHeight






        // ApiServiceFactory를 통해 ApiService 인스턴스 생성
        val apiService = ApiServiceFactory.apiService

        val todayCalendar = Calendar.getInstance()
        val currentYear = todayCalendar.get(Calendar.YEAR)
        val currentMonth = todayCalendar.get(Calendar.MONTH) + 1 // 월은 0부터 시작하므로 1을 더함

        val todayYearMonth = "%04d-%02d".format(currentYear, currentMonth)

        apiService.getCalendar(yearMonth = todayYearMonth).enqueue(object : Callback<CalendarResponse> {
            override fun onResponse(call: Call<CalendarResponse>, response: Response<CalendarResponse>) {
                if (response.isSuccessful) {
                    val calendarResponse = response.body()
                    var emptyToday = true
                    val calendarData = calendarResponse?.data
                    calendarData!!.forEach {
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                        val calendarDateStr = dateFormat.format(it.date)
                        val selectedDateStr = dateFormat.format(selectedDate)
                        if (calendarDateStr == selectedDateStr) {
                            emptyToday = false // 선택한 날짜에 데이터가 있는 경우
                        }
                    }
                    if (!emptyToday) {
                        // 해당 날짜에 데이터가 있는 경우
                        readBtn.visibility = View.VISIBLE
                        writeBtn.visibility = View.GONE
                    } else {
                        writeBtn.visibility = View.VISIBLE
                        readBtn.visibility = View.GONE
                    }



                } else {
                    // 오류 처리
                    val errorBody = response.errorBody()?.string()
                    val message = "응답 코드: ${response.code()}, 메시지: ${response.message()}, 오류 내용: $errorBody"
                    Log.e("API_RESPONSE", message)
                    Toast.makeText(requireContext(), "오류가 발생했습니다. Error Code: "+response.code(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CalendarResponse>, t: Throwable) {
                // 네트워크 오류 처리
                Log.e("API_RESPONSE", "네트워크 실패: ${t.message}")
                Toast.makeText(requireContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        })


                    // CalendarView의 날짜 선택 이벤트 리스너 설정
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, dayOfMonth)
            selectedDate = selectedCalendar.timeInMillis // 선택한 날짜를 저장

            val selectedYear = year // 선택한 년
            val selectedMonth = month + 1 // 선택한 월 (월은 0부터 시작하므로 1을 더함)

            val yearMonth = "%04d-%02d".format(selectedYear, selectedMonth)

            apiService.getCalendar(yearMonth = yearMonth).enqueue(object : Callback<CalendarResponse> {
                override fun onResponse(call: Call<CalendarResponse>, response: Response<CalendarResponse>) {
                    if (response.isSuccessful) {
                        val calendarResponse = response.body()
                        var emptyDay = true
                        val calendarData = calendarResponse?.data
                        calendarData!!.forEach {
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                            val calendarDateStr = dateFormat.format(it.date)
                            val selectedDateStr = dateFormat.format(selectedDate)
                            if (calendarDateStr == selectedDateStr) {
                                emptyDay = false // 선택한 날짜에 데이터가 있는 경우
                            }
                        }
                        if (!emptyDay) {
                            // 해당 날짜에 데이터가 있는 경우
                            readBtn.visibility = View.VISIBLE
                            writeBtn.visibility = View.GONE
                        } else {
                            // 해당 날짜에 데이터가 없는 경우
                            val todayCalendar = Calendar.getInstance()

                            if (selectedCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR) &&
                                selectedCalendar.get(Calendar.MONTH) == todayCalendar.get(Calendar.MONTH) &&
                                selectedCalendar.get(Calendar.DAY_OF_MONTH) == todayCalendar.get(Calendar.DAY_OF_MONTH)
                            ) {
                                writeBtn.visibility = View.VISIBLE
                                readBtn.visibility = View.GONE
                            }else{
                                writeBtn.visibility = View.GONE
                                readBtn.visibility = View.GONE
                            }
                        }



                    } else {
                        // 오류 처리
                        val errorBody = response.errorBody()?.string()
                        val message = "응답 코드: ${response.code()}, 메시지: ${response.message()}, 오류 내용: $errorBody"
                        Log.e("API_RESPONSE", message)
                        Toast.makeText(requireContext(), "오류가 발생했습니다. Error Code: "+response.code(), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CalendarResponse>, t: Throwable) {
                    // 네트워크 오류 처리
                    Log.e("API_RESPONSE", "네트워크 실패: ${t.message}")
                    Toast.makeText(requireContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
            })



        }

        // writeBtn 클릭 이벤트 설정
        writeBtn.setOnClickListener {
            val uploadFragment= Upload_fragment.newInstance()

            parentFragmentManager.beginTransaction()
                .replace(R.id.mainNaviFragmentContainer, uploadFragment)
                .addToBackStack(null)
                .commit()
        }




        // readBtn 클릭 이벤트 설정
        readBtn.setOnClickListener {
            // readBtn 클릭 시 동작 수행
            val diaryReadFragment  = DiaryReadFragment.newInstance(selectedDate)


            parentFragmentManager.beginTransaction()
                .replace(R.id.mainNaviFragmentContainer, diaryReadFragment )
                .addToBackStack(null)
                .commit()

            Toast.makeText(requireContext(), "일기 조회", Toast.LENGTH_SHORT).show()
        }

        return rootView
    }

}
