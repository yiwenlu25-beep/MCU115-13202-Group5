package com.example.fastfood

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class DrinkFragment : Fragment(R.layout.fragment_drink) {

    private val vm: OrderViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rgDrink = view.findViewById<RadioGroup>(R.id.rgDrink)
        val btnDone = view.findViewById<Button>(R.id.btnDrinkDone)

        btnDone.setOnClickListener {
            val selectedId = rgDrink.checkedRadioButtonId
            if (selectedId != -1) {
                val rb = view.findViewById<RadioButton>(selectedId)
                vm.setDrink(rb.text.toString())
            }
            (activity as? MainActivity)?.closeWizard()
        }
    }
}