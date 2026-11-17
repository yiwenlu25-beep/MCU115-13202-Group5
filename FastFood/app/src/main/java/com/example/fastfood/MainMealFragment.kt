package com.example.fastfood

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class MainMealFragment : Fragment(R.layout.fragment_main_meal) {

    private val vm: OrderViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rgMainMeal = view.findViewById<RadioGroup>(R.id.rgMainMeal)
        val btnDone = view.findViewById<Button>(R.id.btnMainDone)

        btnDone.setOnClickListener {
            val selectedId = rgMainMeal.checkedRadioButtonId
            if (selectedId != -1) {
                val rb = view.findViewById<RadioButton>(selectedId)
                vm.setMainMeal(rb.text.toString())
            }
            (activity as? MainActivity)?.closeWizard()
        }
    }
}