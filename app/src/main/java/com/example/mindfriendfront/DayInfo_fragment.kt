import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.mindfriendfront.DayInfo
import com.example.mindfriendfront.R

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

        val btnBack: ImageButton = view.findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            // changeViewColor 함수 호출하여 배경색 변경
            changeViewColor(colorView, "value1")
        }

        return view
    }

    private fun changeViewColor(view: View, data: String) {
        val color: Int = when (data) {
            "value1" -> Color.RED
            "value2" -> Color.GREEN
            "value3" -> Color.BLUE
            else -> Color.BLACK
        }
        view.setBackgroundColor(color)
    }
}