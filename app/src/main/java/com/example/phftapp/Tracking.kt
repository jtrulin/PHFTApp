package com.example.phftapp

// FOR RUNNING ACTIVITY

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate

class Tracking : AppCompatActivity() {
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


        val calories = intent.getFloatExtra("calories", 0.0f) // default to 0.0 if nothing is passed
        val barChar = findViewById<com.github.mikephil.charting.charts.BarChart>(R.id.barChar)
        barChar.xAxis.textColor = Color.WHITE
        barChar.axisLeft.textColor = Color.WHITE
        barChar.axisRight.textColor = Color.WHITE


        barList = arrayListOf(
            BarEntry(1f, 500f),
            BarEntry(2f, 150f),
            BarEntry(3f, 300f),
            BarEntry(4f, 200f),
            BarEntry(5f, 200f),
            BarEntry(6f, 20f),
            BarEntry(7f, calories)

        )

        barDataSet = BarDataSet(barList,"Calories")
        barData = BarData(barDataSet)
        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS, 250)
        barChar.data = barData
        barDataSet.valueTextColor = Color.WHITE
        barDataSet.valueTextSize= 15f

    }
}