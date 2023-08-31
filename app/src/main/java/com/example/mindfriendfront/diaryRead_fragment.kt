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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar
import java.util.Locale
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import android.widget.ImageButton
import androidx.core.content.ContentProviderCompat.requireContext


class diaryRead_fragment : Fragment(){
    private lateinit var pdfButton: ImageButton
    private lateinit var moreButton: ImageButton
    private lateinit var rootView: View
    companion object {
        private const val REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 1
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
        // 기존 코드와 동일
        val view = inflater.inflate(R.layout.fragment_diary_read_fragment, container, false)
        val selectedDateMillis = arguments?.getLong(ARG_SELECTED_DATE) ?: 0
        rootView = view  // rootView를 초기화

        pdfButton = rootView.findViewById(R.id.pdfButton)
        val selectedDate = Date(selectedDateMillis)
        Log.e("Diarydate", selectedDate.toString())
        pdfButton = rootView.findViewById(R.id.pdfButton)
        moreButton = rootView.findViewById(R.id.moreButton)
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


        pdfButton.setOnClickListener {

            checkAndRequestPermission() // 권한 확인 및 요청
        }

        moreButton.setOnClickListener {
            val dayInfo_fragment = DayInfo_fragment.newInstance()
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainNaviFragmentContainer, dayInfo_fragment)
                .addToBackStack(null)
                .commit()
        }
        return view
    }

    private fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE
            )
        } else {

            saveAsPdf()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveAsPdf() // 권한이 허용된 경우에만 PDF 저장 실행
            } else {
                Toast.makeText(
                    requireContext(),
                    "권한이 거부되었습니다. PDF를 생성할 수 없습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun getFullContentHeight(scrollView: NestedScrollView): Int {
        val contentView = scrollView.getChildAt(0)
        return contentView.height
    }
    private fun saveAsPdf() {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(rootView.width, rootView.height, 1).create()

        val scrollView = rootView.findViewById<NestedScrollView>(R.id.pdfContainer)
        val totalHeight = getFullContentHeight(scrollView)
        var yOffset = 0

        while (yOffset < totalHeight) {
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            val dstRect = Rect(0, 0, rootView.width, pageInfo.pageHeight)

            val bitmap = getBitmap(yOffset, dstRect.height())
            canvas.drawBitmap(bitmap, 0f, 0f, null)

            pdfDocument.finishPage(page)
            yOffset += dstRect.height()
        }

    val fileName = "example.pdf"
    val file = File(requireContext().getExternalFilesDir(null), fileName)
    try {

        val outputStream = FileOutputStream(file)
        pdfDocument.writeTo(outputStream)
        pdfDocument.close()

        Toast.makeText(requireContext(), "PDF 저장 완료: ${file.absolutePath}", Toast.LENGTH_LONG).show()
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(requireContext(), "PDF 저장 실패", Toast.LENGTH_SHORT).show()
    }
}
    private fun getBitmap(yOffset: Int, height: Int): Bitmap {
        val scrollView = rootView.findViewById<NestedScrollView>(R.id.pdfContainer)
        val contentView = scrollView.getChildAt(0)

        val srcRect = Rect(0, yOffset, rootView.width, yOffset + height)

        val bitmap = Bitmap.createBitmap(rootView.width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        contentView.draw(canvas)  // 수정: contentView.draw(canvas)로 변경

        return Bitmap.createBitmap(bitmap, 0, 0, srcRect.width(), height)
    }



}