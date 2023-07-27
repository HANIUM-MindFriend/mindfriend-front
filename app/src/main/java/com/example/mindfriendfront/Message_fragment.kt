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

class Message_fragment : Fragment() {
    private lateinit var messageContainer: LinearLayout
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_chat, container, false)

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
