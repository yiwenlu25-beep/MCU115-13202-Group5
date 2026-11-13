package com.example.fastfood

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class SideDishesFragment : Fragment(R.layout.fragment_side_dishes) {

    private val vm: OrderViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cbFries = view.findViewById<CheckBox>(R.id.cbFries)
        val cbSalad = view.findViewById<CheckBox>(R.id.cbSalad)
        val cbCornCup = view.findViewById<CheckBox>(R.id.cbCornCup)
        val btnDone = view.findViewById<Button>(R.id.btnSideDone)

        btnDone.setOnClickListener {
            val selectedSides = mutableListOf<String>()
            if (cbFries.isChecked) selectedSides.add(cbFries.text.toString())
            if (cbSalad.isChecked) selectedSides.add(cbSalad.text.toString())
            if (cbCornCup.isChecked) selectedSides.add(cbCornCup.text.toString())

            vm.setSideDishes(selectedSides)
            (activity as? MainActivity)?.closeWizard()
        }
    }
}