package com.example.fastfoodorderingwizard

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var selectedMainMeal: String? = null
    private val selectedSideDishes = mutableListOf<String>()
    private var selectedDrink: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnMainMeal = findViewById<Button>(R.id.btnMainMeal)
        val btnSideDishes = findViewById<Button>(R.id.btnSideDishes)
        val btnDrink = findViewById<Button>(R.id.btnDrink)
        val btnConfirm = findViewById<Button>(R.id.btnConfirm)

        // 1. 主餐選擇（單選 AlertDialog）
        btnMainMeal.setOnClickListener {
            val meals = arrayOf("漢堡 (Burger)", "炸雞 (Fried Chicken)", "牛肉麵 (Beef Noodles)")
            var checkedIndex = -1
            if (selectedMainMeal != null) {
                checkedIndex = meals.indexOf(selectedMainMeal)
            }

            AlertDialog.Builder(this)
                .setTitle("請選擇主餐")
                .setSingleChoiceItems(meals, checkedIndex) { _, which ->
                    checkedIndex = which
                }
                .setPositiveButton("確定") { _, _ ->
                    if (checkedIndex != -1) {
                        selectedMainMeal = meals[checkedIndex]
                        showToast("已選擇: $selectedMainMeal")
                    }
                }
                .setNegativeButton("取消", null)
                .show()
        }

        // 2. 副餐選擇（多選 AlertDialog）
        btnSideDishes.setOnClickListener {
            val sides = arrayOf("薯條 (Fries)", "雞塊 (Nuggets)", "沙拉 (Salad)", "地瓜球 (Sweet Potato Balls)")
            val checkedItems = booleanArrayOf(false, false, false, false)

            for (i in sides.indices) {
                checkedItems[i] = selectedSideDishes.contains(sides[i])
            }

            AlertDialog.Builder(this)
                .setTitle("請選擇副餐（可複選）")
                .setMultiChoiceItems(sides, checkedItems) { _, which, isChecked ->
                    checkedItems[which] = isChecked
                }
                .setPositiveButton("確定") { _, _ ->
                    selectedSideDishes.clear()
                    for (i in sides.indices) {
                        if (checkedItems[i]) {
                            selectedSideDishes.add(sides[i])
                        }
                    }
                    showToast("已選擇副餐: ${selectedSideDishes.joinToString(", ")}")
                }
                .setNegativeButton("取消", null)
                .show()
        }

        // 3. 飲料選擇（單選 AlertDialog）
        btnDrink.setOnClickListener {
            val drinks = arrayOf("可樂 (Cola)", "冰紅茶 (Iced Tea)", "烏龍茶 (Oolong Tea)")
            var checkedIndex = -1
            if (selectedDrink != null) {
                checkedIndex = drinks.indexOf(selectedDrink)
            }

            AlertDialog.Builder(this)
                .setTitle("請選擇飲料")
                .setSingleChoiceItems(drinks, checkedIndex) { _, which ->
                    checkedIndex = which
                }
                .setPositiveButton("確定") { _, _ ->
                    if (checkedIndex != -1) {
                        selectedDrink = drinks[checkedIndex]
                        showToast("已選擇: $selectedDrink")
                    }
                }
                .setNegativeButton("取消", null)
                .show()
        }

        // 4. 確認訂單與完整性驗證
        btnConfirm.setOnClickListener {
            if (selectedMainMeal.isNullOrEmpty() || selectedSideDishes.isEmpty() || selectedDrink.isNullOrEmpty()) {
                Toast.makeText(
                    this,
                    "請完整選擇主餐、至少一項副餐與飲料！",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            AlertDialog.Builder(this)
                .setTitle("確認送出訂單")
                .setMessage(
                    "主餐：$selectedMainMeal\n" +
                            "副餐：${selectedSideDishes.joinToString(", ")}\n" +
                            "飲料：$selectedDrink\n\n" +
                            "是否確認送出這筆訂單？"
                )
                .setPositiveButton("確定送出") { dialog, _ ->
                    dialog.dismiss()
                    Toast.makeText(this, "訂單已成功送出！", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("返回修改", null)
                .show()
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}