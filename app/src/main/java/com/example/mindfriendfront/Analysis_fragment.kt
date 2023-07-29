package com.example.mindfriendfront

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment


class Analysis_fragment : Fragment() {
    private lateinit var btnBack: ImageButton
    private lateinit var colorView: View
    companion object {
        fun newInstance(): Analysis_fragment {
            return Analysis_fragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.analysis_info, container, false)

        btnBack = rootView.findViewById(R.id.btnBack)
        colorView = rootView.findViewById(R.id.colorView)

        btnBack.setOnClickListener {
            changeViewColor(colorView, "value1")
        }

        return rootView
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
