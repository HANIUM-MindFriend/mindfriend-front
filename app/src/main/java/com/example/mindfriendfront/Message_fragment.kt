package com.example.mindfriendfront
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import android.view.ViewTreeObserver
import android.widget.ScrollView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
class Message_fragment : Fragment() {
    private lateinit var messageContainer: LinearLayout
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button
    private lateinit var pdfButton: Button
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.activity_chat, container, false)
        pdfButton = rootView.findViewById(R.id.pdfButton)
        pdfButton.setOnClickListener { createPdf() }
        messageContainer = rootView.findViewById(R.id.messageContainer)
        messageEditText = rootView.findViewById(R.id.messageEditText)
        sendButton = rootView.findViewById(R.id.sendButton)
        sendButton.setBackgroundColor(Color.parseColor("#7ea9fd"))

        sendButton.setOnClickListener {
            val message = messageEditText.text.toString().trim()
            if (message.isNotEmpty()) {
                addMessage("나", message)
                messageEditText.text.clear()
            }
        }

        // 가상의 메시지로 초기화
        addMessage("챗봇", "hi")
        addMessage("나", "hello")
        addMessage("챗봇", "nice to meet you")

        return rootView
    }
    private fun createPdf() {
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
    private fun getFormattedDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyMMdd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
    private fun getFullContentHeight(): Int {
        val scrollView = rootView.findViewById<NestedScrollView>(R.id.pdfContainer)
        val contentView = scrollView.getChildAt(0)
        return contentView.height
    }
    private fun saveAsPdf() {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(rootView.width, rootView.height, 1).create()

        // 전체 콘텐츠 높이 측정
        val totalHeight = getFullContentHeight()
        var yOffset = 0

        while (yOffset < totalHeight) {
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            // 스크롤 뷰의 내용을 그림
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
        // 스크롤 뷰의 일부 영역을 캡처하여 그립니다.
        val bitmap = Bitmap.createBitmap(rootView.width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG)

        val scrollView = rootView.findViewById<NestedScrollView>(R.id.pdfContainer)
        val contentView = scrollView.getChildAt(0)

        val srcRect = Rect(0, yOffset, rootView.width, yOffset + height)
        contentView.draw(canvas)
        return bitmap
    }

    companion object {
        private const val REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 1
    }
    private fun addMessage(sender: String, message: String) {
        val messageView =
            LayoutInflater.from(requireContext()).inflate(R.layout.activity_message_item, null)

        val senderTextView = messageView.findViewById<TextView>(R.id.senderTextView)
        val messageTextView = messageView.findViewById<TextView>(R.id.messageTextView)

        senderTextView.text = sender
        messageTextView.text = message

        messageContainer.addView(messageView)
    }
}
