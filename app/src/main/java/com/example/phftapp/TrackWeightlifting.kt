package com.example.phftapp

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate

class TrackWeightlifting : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var barList:ArrayList<BarEntry>
        lateinit var barDataSet: BarDataSet
        lateinit var barData: BarData


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tracking)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val weight = intent.getFloatExtra("weight", 0.0f) // default to 0.0 if nothing is passed
        val barChar = findViewById<com.github.mikephil.charting.charts.BarChart>(R.id.barChar)
        barChar.xAxis.textColor = Color.WHITE
        barChar.axisLeft.textColor = Color.WHITE
        barChar.axisRight.textColor = Color.WHITE


        barList = arrayListOf(
            BarEntry(1f, 100f),
            BarEntry(2f, 150f),
            BarEntry(3f, 150f),
            BarEntry(4f, 170f),
            BarEntry(5f, 200f),
            BarEntry(6f, 250f),
            BarEntry(7f, weight)

        )

        barDataSet = BarDataSet(barList,"Pounds of Weight")
        barData = BarData(barDataSet)
        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS, 250)
        barChar.data = barData
        barDataSet.valueTextColor = Color.WHITE
        barDataSet.valueTextSize= 15f

    }
}