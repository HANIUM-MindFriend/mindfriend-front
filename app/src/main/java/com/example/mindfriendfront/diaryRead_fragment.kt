package com.example.mindfriendfront

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.mindfriendfront.R
import java.text.SimpleDateFormat
import java.util.Date


class diaryRead_fragment : Fragment() {
    companion object {
        fun newInstance(): diaryRead_fragment {
            return diaryRead_fragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diary_read_fragment, container, false)

        val dateTextView: TextView = view.findViewById(R.id.date)

        val currentDate = Date()

        val dateFormat = SimpleDateFormat("MMdd")
        val formattedDate = dateFormat.format(currentDate)

        dateTextView.text = formattedDate

        return view
    }
}
