import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mindfriendfront.R

class message : AppCompatActivity() {
    private lateinit var messageContainer: LinearLayout
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        Toast.makeText(applicationContext, "홈 클릭", Toast.LENGTH_SHORT).show()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
//        setContentView(R.layout.activity_message_item)

        messageContainer = findViewById(R.id.messageContainer)
        messageEditText = findViewById(R.id.messageEditText)
        sendButton = findViewById(R.id.sendButton)
        sendButton.setBackgroundColor(Color.parseColor("#7ea9fd"))

        sendButton.setOnClickListener {

            val message = messageEditText.text.toString().trim()
            if (message.isNotEmpty()) {
                addMessage("Me", message)
                messageEditText.text.clear()
            }
        }

        // 가상의 메시지로 초기화
        addMessage("Other", "hi")
        addMessage("Me", "hello")
        addMessage("Other", "nice to meet you")
    }

    private fun addMessage(sender: String, message: String) {
        val messageView = LayoutInflater.from(this).inflate(R.layout.activity_message_item, null)

        val senderTextView = messageView.findViewById<TextView>(R.id.senderTextView)
        val messageTextView = messageView.findViewById<TextView>(R.id.messageTextView)

        senderTextView.text = sender
        messageTextView.text = message

        messageContainer.addView(messageView)
    }
}
