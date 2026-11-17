package com.example.fastfood

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {

    private val vm: OrderViewModel by viewModels()
    lateinit var viewPager: ViewPager2
    private lateinit var tvFinalSummary: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)
        tvFinalSummary = findViewById(R.id.tvFinalSummary)

        viewPager.adapter = WizardPagerAdapter(this)
        viewPager.visibility = View.GONE

        findViewById<Button>(R.id.btnNavMain).setOnClickListener { openStep(0) }
        findViewById<Button>(R.id.btnNavSide).setOnClickListener { openStep(1) }
        findViewById<Button>(R.id.btnNavDrink).setOnClickListener { openStep(2) }
        findViewById<Button>(R.id.btnNavConfirm).setOnClickListener { openStep(3) }

        vm.isSubmitted.observe(this) { submitted ->
            if (submitted) {
                val order = vm.order.value
                if (order != null) {
                    tvFinalSummary.text = "Submitted Order:\n" +
                            "Main: ${order.mainMeal}\n" +
                            "Sides: ${order.sideDishes.joinToString(", ")}\n" +
                            "Drink: ${order.drink}"
                }
                viewPager.visibility = View.GONE
                vm.resetSubmissionState()
            }
        }
    }

    fun openStep(index: Int) {
        viewPager.visibility = View.VISIBLE
        viewPager.setCurrentItem(index, true)
    }

    fun closeWizard() {
        viewPager.visibility = View.GONE
    }
}