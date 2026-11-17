package com.example.mycalculator

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvExpression: TextView
    private lateinit var tvCurrentOperator: TextView
    private lateinit var tvResult: TextView

    private var firstOperand: Double? = null
    private var pendingOperator: String? = null
    private var isNewInput = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvExpression = findViewById(R.id.tvExpression)
        tvCurrentOperator = findViewById(R.id.tvCurrentOperator)
        tvResult = findViewById(R.id.tvResult)

        // 綁定數字 0 ~ 9
        val digitButtons = mapOf(
            R.id.btn0 to "0", R.id.btn1 to "1", R.id.btn2 to "2",
            R.id.btn3 to "3", R.id.btn4 to "4", R.id.btn5 to "5",
            R.id.btn6 to "6", R.id.btn7 to "7", R.id.btn8 to "8",
            R.id.btn9 to "9"
        )

        for ((id, digit) in digitButtons) {
            findViewById<Button>(id).setOnClickListener { appendDigit(digit) }
        }

        // 小數點
        findViewById<Button>(R.id.btnDot).setOnClickListener { appendDot() }

        // 運算符按鈕 (÷, ×, -, +)[cite: 6, 7]
        val operatorButtons = mapOf(
            R.id.btnPlus to "+",
            R.id.btnMinus to "-",
            R.id.btnMultiply to "×",
            R.id.btnDivide to "÷"
        )

        for ((id, op) in operatorButtons) {
            findViewById<Button>(id).setOnClickListener { setOperator(op) }
        }

        // 功能鍵[cite: 6, 7]
        findViewById<Button>(R.id.btnEqual).setOnClickListener { calculateResult() }
        findViewById<Button>(R.id.btnAC).setOnClickListener { clearAll() }
        findViewById<Button>(R.id.btnC).setOnClickListener { clearEntry() }
        findViewById<ImageButton>(R.id.btnBackspace).setOnClickListener { backspace() }
    }

    private fun appendDigit(digit: String) {
        if (isNewInput || tvResult.text.toString() == "0") {
            tvResult.text = digit
            isNewInput = false
        } else {
            tvResult.text = "${tvResult.text}$digit"
        }
    }

    private fun appendDot() {
        if (isNewInput) {
            tvResult.text = "0."
            isNewInput = false
        } else if (!tvResult.text.contains(".")) {
            tvResult.text = "${tvResult.text}."
        }
    }

    private fun setOperator(op: String) {
        val currentValue = tvResult.text.toString().toDoubleOrNull() ?: return

        if (firstOperand != null && pendingOperator != null && !isNewInput) {
            val result = performOperation(firstOperand!!, currentValue, pendingOperator!!)
            firstOperand = result
            tvResult.text = formatResult(result)
        } else {
            firstOperand = currentValue
        }

        pendingOperator = op
        tvCurrentOperator.text = op
        tvExpression.text = "${formatResult(firstOperand!!)}$op"
        isNewInput = true
    }

    private fun calculateResult() {
        val secondOperand = tvResult.text.toString().toDoubleOrNull() ?: return
        val op = pendingOperator ?: return
        val first = firstOperand ?: return

        tvExpression.text = "${formatResult(first)}$op${formatResult(secondOperand)}"
        tvCurrentOperator.text = ""

        val result = performOperation(first, secondOperand, op)
        tvResult.text = if (result.isNaN()) "Error" else formatResult(result)

        firstOperand = null
        pendingOperator = null
        isNewInput = true
    }

    private fun performOperation(num1: Double, num2: Double, op: String): Double {
        return when (op) {
            "+" -> num1 + num2
            "-" -> num1 - num2
            "×" -> num1 * num2
            "÷" -> if (num2 != 0.0) num1 / num2 else Double.NaN
            else -> num2
        }
    }

    private fun clearAll() {
        firstOperand = null
        pendingOperator = null
        isNewInput = true
        tvExpression.text = ""
        tvCurrentOperator.text = ""
        tvResult.text = "0"
    }

    private fun clearEntry() {
        tvResult.text = "0"
        isNewInput = true
    }

    private fun backspace() {
        if (isNewInput) return
        val text = tvResult.text.toString()
        if (text.length > 1) {
            tvResult.text = text.substring(0, text.length - 1)
        } else {
            tvResult.text = "0"
            isNewInput = true
        }
    }

    private fun formatResult(value: Double): String {
        return if (value % 1.0 == 0.0) {
            value.toLong().toString()
        } else {
            value.toString()
        }
    }
}