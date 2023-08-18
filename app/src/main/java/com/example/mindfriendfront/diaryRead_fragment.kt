package com.example.mindfriendfront

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.mindfriendfront.R
import com.example.mindfriendfront.data.DiaryGetResponse
import com.example.mindfriendfront.network.ApiServiceFactory
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date


class diaryRead_fragment : Fragment() {
    companion object {
        private const val ARG_SELECTED_DATE = "selectedDate"

        fun newInstance(selectedDate: Long): diaryRead_fragment {
            val fragment = diaryRead_fragment()
            val args = Bundle()
            args.putLong(ARG_SELECTED_DATE, selectedDate)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diary_read_fragment, container, false)
        val selectedDateMillis = arguments?.getLong(ARG_SELECTED_DATE) ?: 0

        val selectedDate = Date(selectedDateMillis)
        Log.e("Diarydate", selectedDate.toString())

        val dateTextView: TextView = view.findViewById(R.id.date) // 'view' 추가
        val diaryText: TextView = view.findViewById(R.id.uploadText) // 'view' 추가
        val profile : CircleImageView = view.findViewById(R.id.circle_profile) // 'view' 추가

        val dateFormat = SimpleDateFormat("MMdd")
        val formattedDate = dateFormat.format(selectedDate)

        val queryDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val queryFormattedDate = queryDateFormat.format(selectedDate)
        Log.e("QueryDiarydate", queryFormattedDate)
        dateTextView.text = formattedDate

        // ApiServiceFactory를 통해 ApiService 인스턴스 생성
        val apiService = ApiServiceFactory.apiService

        apiService.getDiary(date = queryFormattedDate).enqueue(object : Callback<DiaryGetResponse> {
            override fun onResponse(call: Call<DiaryGetResponse>, response: Response<DiaryGetResponse>) {
                if (response.isSuccessful) {
                    val diaryResponse = response.body()
                    val diaryData = diaryResponse?.data

                    diaryData?.let {
                        diaryText.text = it.content // 일기 내용 설정
                        // 이미지 라이브러리를 사용하여 profile 이미지 설정
                        Glide.with(requireContext()) // 'requireContext()'로 수정
                            .load(diaryData.profileImg)
                            .into(profile)
                    }
                } else {
                    // 오류 처리
                    val errorBody = response.errorBody()?.string()
                    val message = "응답 코드: ${response.code()}, 메시지: ${response.message()}, 오류 내용: $errorBody"
                    Log.e("API_RESPONSE", message)
                    Toast.makeText(requireContext(), "오류가 발생했습니다. Error Code: "+response.code(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DiaryGetResponse>, t: Throwable) {
                // 네트워크 오류 처리
                Log.e("API_RESPONSE", "네트워크 실패: ${t.message}")
                Toast.makeText(requireContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        })

        return view
    }

}
