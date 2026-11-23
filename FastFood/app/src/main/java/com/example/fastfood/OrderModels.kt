package com.example.fastfood

data class Order(
    var mainMeal: String? = null,
    var sideDishes: MutableList<String> = mutableListOf(),
    var drink: String? = null
) {
    fun isComplete(): Boolean {
        return !mainMeal.isNullOrEmpty() && sideDishes.isNotEmpty() && !drink.isNullOrEmpty()
    }
}