package com.example.fastfood

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class ConfirmFragment : Fragment(R.layout.fragment_confirm) {

    private val vm: OrderViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvConfirmSummary = view.findViewById<TextView>(R.id.tvConfirmSummary)
        val btnConfirmOrder = view.findViewById<Button>(R.id.btnConfirmOrder)

        vm.order.observe(viewLifecycleOwner) { order ->
            tvConfirmSummary.text = "Main: ${order.mainMeal ?: "Not selected"}\n" +
                    "Sides: ${if (order.sideDishes.isEmpty()) "Not selected" else order.sideDishes.joinToString(", ")}\n" +
                    "Drink: ${order.drink ?: "Not selected"}"
        }

        btnConfirmOrder.setOnClickListener {
            val order = vm.order.value

            if (order?.isComplete() != true) {
                Toast.makeText(
                    requireContext(),
                    "Please select a main meal, at least one side dish, and a drink.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            AlertDialog.Builder(requireContext())
                .setTitle("Submit Order")
                .setMessage(
                    "Main: ${order.mainMeal}\n" +
                            "Sides: ${order.sideDishes.joinToString(", ")}\n" +
                            "Drink: ${order.drink}\n\nSubmit this order?"
                )
                .setPositiveButton("Submit") { dialog, _ ->
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "Order submitted!", Toast.LENGTH_SHORT).show()
                    vm.submitOrder()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}