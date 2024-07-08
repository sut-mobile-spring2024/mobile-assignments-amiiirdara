package com.amiiirdara.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private var operand1 = Double.NaN
    private var operand2: Double = 0.0
    private var currentOperation: String? = null

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
        }

        val numericButtons = intArrayOf(
            R.id.btn_0,
            R.id.btn_1,
            R.id.btn_2,
            R.id.btn_3,
            R.id.btn_4,
            R.id.btn_5,
            R.id.btn_6,
            R.id.btn_7,
            R.id.btn_8,
            R.id.btn_9,
            R.id.btn_dot
        )
        numericButtons.forEach { findViewById<Button>(it).setOnClickListener(listener) }
    }

    private fun setOperatorOnClickListener() {
        findViewById<Button>(R.id.btn_plus).setOnClickListener {
            compute()
            currentOperation = "+"
            display.text = null
        }

        findViewById<Button>(R.id.btn_minus).setOnClickListener {
            compute()
            currentOperation = "-"
            display.text = null
        }

        findViewById<Button>(R.id.btn_multiply).setOnClickListener {
            compute()
            currentOperation = "*"
            display.text = null
        }

        findViewById<Button>(R.id.btn_divide).setOnClickListener {
            compute()
            currentOperation = "/"
            display.text = null
        }

        findViewById<Button>(R.id.btn_remainder).setOnClickListener {
            compute()
            currentOperation = "%"
            display.text = null
        }

        findViewById<Button>(R.id.btn_radical).setOnClickListener {
            try {
                val number = display.text.toString().toDouble()
                val result = if (number >= 0) {
                    val sqrtResult = Math.sqrt(number)
                    String.format("%.3f", sqrtResult).toDouble()
                } else {
                    "Invalid input"
                }
                display.text = result.toString()
            } catch (e: NumberFormatException) {
                display.text = "Invalid input"
            }
        }

        findViewById<Button>(R.id.btn_equal).setOnClickListener {
            compute()
            currentOperation = "="
            display.text = operand1.toString()
            operand1 = Double.NaN
        }

        findViewById<Button>(R.id.btn_ac).setOnClickListener {
            display.text = ""
            operand1 = Double.NaN
            operand2 = Double.NaN
        }

        findViewById<Button>(R.id.btn_c).setOnClickListener {
            val currentText = display.text.toString()
            if (currentText.isNotEmpty()) {
                val newText = currentText.substring(0, currentText.length - 1)
                display.text = newText
            }
        }

    }

    private fun compute() {
        try {
            if (!operand1.isNaN()) {
                operand2 = display.text.toString().toDouble()
                when (currentOperation) {
                    "+" -> operand1 += operand2
                    "-" -> operand1 -= operand2
                    "*" -> operand1 *= operand2
                    "/" -> operand1 /= operand2
                    "%" -> operand1 %= operand2
                    else -> throw IllegalArgumentException("Invalid operation")
                }
                operand1 = String.format("%.3f", operand1).toDouble()
            } else {
                operand1 = display.text.toString().toDouble()
            }
            display.text = operand1.toString()
        } catch (e: NumberFormatException) {
            display.text = "Invalid input"
        } catch (e: ArithmeticException) {
            display.text = "Division by zero"
        } catch (e: IllegalArgumentException) {
            display.text = "Invalid operation"
        }
    }
}
