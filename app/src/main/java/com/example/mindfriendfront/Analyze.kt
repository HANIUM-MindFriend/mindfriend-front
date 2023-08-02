package com.example.mindfriendfront
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Analyze : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analyze)

        // R.layout.activity_analyze 레이아웃의 GridView를 찾아옴
        val gridView: GridView = findViewById(R.id.gridView)

        // 1부터 30까지의 숫자를 가지는 배열 생성
        val numbers = Array(30) { i -> (i + 1).toString() }

        // ArrayAdapter를 사용하여 GridView에 데이터 설정
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, numbers)
        gridView.adapter = adapter

        //각 칸마다 색깔 설정하기
//        val adapter = object : ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, numbers) {
//            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//                val view = super.getView(position, convertView, parent) as TextView
//                val bgColor = if (position % 2 == 0) Color.RED else Color.BLUE
//                view.setBackgroundColor(bgColor)
//                return view
//            }
//        }

        gridView.adapter = adapter
    }
}




