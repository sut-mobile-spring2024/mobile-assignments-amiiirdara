package com.amiiirdara.calculator

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileWriter
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private lateinit var display: TextView
    private var operand1 = Double.NaN
    private var operand2 = 0.0
    private var currentOperation = 0.toChar()
    private var expression = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.display)

        setNumericOnClickListener()
        setOperatorOnClickListener()
    }

    private fun setNumericOnClickListener() {
        val listener = View.OnClickListener { v ->
            val button = v as Button
            display.append(button.text.toString())
            expression += button.text.toString()
        }

        findViewById<View>(R.id.btn_0).setOnClickListener(listener)
        findViewById<View>(R.id.btn_1).setOnClickListener(listener)
        findViewById<View>(R.id.btn_2).setOnClickListener(listener)
        findViewById<View>(R.id.btn_3).setOnClickListener(listener)
        findViewById<View>(R.id.btn_4).setOnClickListener(listener)
        findViewById<View>(R.id.btn_5).setOnClickListener(listener)
        findViewById<View>(R.id.btn_6).setOnClickListener(listener)
        findViewById<View>(R.id.btn_7).setOnClickListener(listener)
        findViewById<View>(R.id.btn_8).setOnClickListener(listener)
        findViewById<View>(R.id.btn_9).setOnClickListener(listener)
        findViewById<View>(R.id.btn_dot).setOnClickListener(listener)
    }

    @SuppressLint("SetTextI18n")
    private fun setOperatorOnClickListener() {
        findViewById<View>(R.id.btn_plus).setOnClickListener {
            compute()
            currentOperation = '+'
            expression += " + "
            display.text = null
        }

        findViewById<View>(R.id.btn_minus).setOnClickListener {
            compute()
            currentOperation = '-'
            expression += " - "
            display.text = null
        }

        findViewById<View>(R.id.btn_multiply).setOnClickListener {
            compute()
            currentOperation = '*'
            expression += " * "
            display.text = null
        }

        findViewById<View>(R.id.btn_divide).setOnClickListener {
            compute()
            currentOperation = '/'
            expression += " / "
            display.text = null
        }

        findViewById<View>(R.id.btn_remainder).setOnClickListener {
            compute()
            currentOperation = '%'
            expression += " % "
            display.text = null
        }

        findViewById<View>(R.id.btn_equal).setOnClickListener {
            compute()
            currentOperation = '='
            val result = operand1.toString()
            display.text = result
            writeHistory("$expression = $result")
            expression = result
            operand1 = Double.NaN
        }

        // Clear (AC) button
        findViewById<View>(R.id.btn_ac).setOnClickListener {
            display.text = ""
            operand1 = Double.NaN
            operand2 = Double.NaN
            expression = ""
        }

        findViewById<View>(R.id.btn_history).setOnClickListener {
            val intent = Intent(
                this@MainActivity,
                HistoryActivity::class.java
            )
            startActivity(intent)
        }
    }

    private fun compute() {
        if (!java.lang.Double.isNaN(operand1)) {
            operand2 = display.text.toString().toDouble()
            when (currentOperation) {
                '+' -> operand1 += operand2
                '-' -> operand1 -= operand2
                '*' -> operand1 *= operand2
                '/' -> if (operand2 != 0.0) operand1 /= operand2
                '%' -> operand1 %= operand2
            }
        } else {
            try {
                operand1 = display.text.toString().toDouble()
            } catch (e: Exception) {
            }
        }
    }

    private fun writeHistory(operation: String) {
        val historyFile = File(filesDir, "history.txt")
        try {
            FileWriter(historyFile, true).use { writer ->
                writer.write(operation + "\n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
