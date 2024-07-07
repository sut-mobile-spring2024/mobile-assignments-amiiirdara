package com.amiiirdara.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException


class HistoryActivity : AppCompatActivity() {
    private var historyContainer: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        historyContainer = findViewById(R.id.history_container)
        val clearHistoryButton = findViewById<Button>(R.id.btn_clear_history)

        loadHistory()

        clearHistoryButton.setOnClickListener { v: View? -> clearHistory() }
    }

    @SuppressLint("SetTextI18n")
    private fun loadHistory() {
        val historyFile = File(filesDir, "history.txt")
        if (historyFile.exists()) {
            try {
                BufferedReader(FileReader(historyFile)).use { reader ->
                    var line: String?
                    while ((reader.readLine().also { line = it }) != null) {
                        val textView = TextView(this)
                        textView.text = line
                        textView.setTextColor(resources.getColor(android.R.color.black))
                        textView.textSize = 18f
                        historyContainer!!.addView(textView)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            val textView = TextView(this)
            textView.text = "error"
            historyContainer!!.addView(textView)
        }
    }

    private fun clearHistory() {
        val historyFile = File(filesDir, "history.txt")
        if (historyFile.exists()) {
            historyFile.delete()
            historyContainer!!.removeAllViews()
        }
    }
}
