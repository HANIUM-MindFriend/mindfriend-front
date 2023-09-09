import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mindfriendfront.DayInfo
import com.example.mindfriendfront.R
import com.example.mindfriendfront.data.DayInfoResponse
import com.example.mindfriendfront.data.DiaryGetResponse
import com.example.mindfriendfront.network.ApiServiceFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DayInfo_fragment : Fragment() {
    private lateinit var colorView: View
    companion object {
        private const val REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 1
        fun newInstance(): DayInfo_fragment {
            return DayInfo_fragment()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_day_info_fragment, container, false)

        colorView = view.findViewById(R.id.colorView)

        val dateText : TextView = view.findViewById(R.id.textView)
        val btnBack: ImageButton = view.findViewById(R.id.btnBack)
        val musicText : TextView = view.findViewById(R.id.textView3)
        val colorText : TextView = view.findViewById(R.id.textView5)
        val emoText : TextView = view.findViewById(R.id.textView7)
        val missionText : TextView = view.findViewById(R.id.textView9)
        val colorView : View = view.findViewById(R.id.colorView)


        btnBack.setOnClickListener {
            // 백 스택에서 이전 Fragment로 돌아가기
            activity?.supportFragmentManager?.popBackStack()
        }
        val formattedDate: String = arguments?.getString("formattedDate") ?: ""
        Log.e("date",formattedDate)
        dateText.text = formattedDate.substring(5, 7) + formattedDate.substring(8, 10)

        // ApiServiceFactory를 통해 ApiService 인스턴스 생성
        val apiService = ApiServiceFactory.apiService

        apiService.getDayInfo(date = formattedDate).enqueue(object : Callback<DayInfoResponse> {
            override fun onResponse(call: Call<DayInfoResponse>, response: Response<DayInfoResponse>) {
                if (response.isSuccessful) {
                    val emoResponse = response.body()
                    val emoData = emoResponse?.data
                    Log.e("data",emoData.toString())
                    if (emoData != null) {
                        when (emoData.mainEmo) {
                            "분노" -> {
                                // 분노
                                musicText.text = "분노 음악"
                                colorText.text = "#D4F0F0"
                                emoText.text = "분노"
                                missionText.text = "산책을 해보는 것은 어떨까요?"
                                changeViewColor(colorView, "분노")
                            }
                            "혐오"  -> {
                                // 혐오
                                musicText.text = "혐오 음악"
                                colorText.text = "#CCE2CB"
                                emoText.text = "햠오"
                                missionText.text = "오늘은 휴식을 취해보는 것도 추천드려요"
                                changeViewColor(colorView, "혐오")
                            }
                            "두려움"  -> {
                                // 두려움
                                musicText.text ="두려움 음악"
                                colorText.text ="#B6C1A9"
                                emoText.text = "두려움"
                                missionText.text = Editable.Factory.getInstance().newEditable("햇빛을 받으며 명상을 해보세요!")
                                changeViewColor(colorView, "두려움")
                            }
                            "행복"  -> {
                                //행복
                                musicText.text = "Shake It Off - Taylor Swift"
                                colorText.text = "#FF968A"
                                emoText.text = "행복"
                                missionText.text = "오늘을 기록해두는 건 어떨까요?"
                                changeViewColor(colorView, "행복")
                            }
                            "중립"  -> {
                                //중립
                                musicText.text = "중립 음악"
                                colorText.text = "#FFFFFF"
                                emoText.text = "중립"
                                missionText.text = "자신이 좋아하는 취미를 하면서 즐거운 시간을 보내는 것은 어떨까요?"
                                changeViewColor(colorView, "중립")
                            }
                            "슬픔"  -> {
                                //슬픔
                                musicText.text = "수고했어, 오늘도 - 옥상달빛"
                                colorText.text = "#FFD8BE"
                                emoText.text = "슬픔"
                                missionText.text = "신나는 노래를 들어보세요"
                                changeViewColor(colorView, "슬픔")
                            }
                            else -> {
                                // 놀람
                                musicText.text = "놀람 음악"
                                colorText.text = "#F6EAC2"
                                emoText.text = "놀람"
                                missionText.text ="천천히 심호흡한 후 명상을 해보세요!"
                                changeViewColor(colorView, "놀람")
                            }
                        }
                    }
                } else {
                    // 오류 처리
                    val errorBody = response.errorBody()?.string()
                    val message = "응답 코드: ${response.code()}, 메시지: ${response.message()}, 오류 내용: $errorBody"
                    Log.e("API_RESPONSE", message)
                    Toast.makeText(requireContext(), "오류가 발생했습니다. Error Code: " + response.code(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DayInfoResponse>, t: Throwable) {
                // 네트워크 오류 처리
                Log.e("API_RESPONSE", "네트워크 실패: ${t.message}")
                Toast.makeText(requireContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        })


        return view
    }

    private fun changeViewColor(view: View, data: String) {
        val color: Int = when (data) {
            "분노" -> Color.parseColor("#D4F0F0")
            "혐오" -> Color.parseColor("#CCE2CB")
            "두려움" -> Color.parseColor("#B6C1A9")
            "행복" -> Color.parseColor("#FF968A")
            "중립" -> Color.parseColor("#FFFFFF")
            "슬픔" -> Color.parseColor("#FFD8BE")
            else -> Color.parseColor("#F6EAC2")
        }
        view.setBackgroundColor(color)
    }
}