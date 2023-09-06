package com.example.mindfriendfront

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ChatBot : AppCompatActivity() {
    lateinit var chat_text: TextView
    lateinit var chat_data: String
    private var running = true
    private lateinit var backgroundThread: Thread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)


        chat_text = findViewById(R.id.chatbotText)

        backgroundThread = Thread {
            while (running) {
                chat_text.setText(chat_data)

            }
        }
        //backgroundThread.start()
    }

    //화면 나가면 종료
    override fun onPause() {
        super.onPause()
        running = false
        try {
            backgroundThread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}
