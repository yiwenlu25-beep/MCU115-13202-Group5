package com.example.fastfood

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OrderViewModel : ViewModel() {
    private val _order = MutableLiveData(Order())
    val order: LiveData<Order> = _order

    private val _isSubmitted = MutableLiveData(false)
    val isSubmitted: LiveData<Boolean> = _isSubmitted

    fun setMainMeal(meal: String) {
        val current = _order.value ?: Order()
        current.mainMeal = meal
        _order.value = current
    }

    fun setSideDishes(sides: List<String>) {
        val current = _order.value ?: Order()
        current.sideDishes = sides.toMutableList()
        _order.value = current
    }

    fun setDrink(drink: String) {
        val current = _order.value ?: Order()
        current.drink = drink
        _order.value = current
    }

    fun submitOrder() {
        _isSubmitted.value = true
    }

    fun resetSubmissionState() {
        _isSubmitted.value = false
    }
}