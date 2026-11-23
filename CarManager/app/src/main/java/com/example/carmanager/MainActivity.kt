package com.example.carmanager // 請依據你的實際專案名稱修改

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var items: ArrayList<String> = ArrayList()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var dbrw: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 取得資料庫實體
        dbrw = MyDBHelper(this).writableDatabase
        // 宣告 Adapter 並連結 ListView
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        findViewById<ListView>(R.id.listView).adapter = adapter

        // 設定按鈕監聽器
        setListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbrw.close() // 關閉資料庫
    }

    private fun setListener() {
        val edBrand = findViewById<EditText>(R.id.edBrand)
        val edModel = findViewById<EditText>(R.id.edModel)
        val edYear = findViewById<EditText>(R.id.edYear)
        val edPrice = findViewById<EditText>(R.id.edPrice)

        // 1. 新增功能
        findViewById<Button>(R.id.btnInsert).setOnClickListener {
            if (edBrand.length() < 1 || edModel.length() < 1 || edYear.length() < 1 || edPrice.length() < 1) {
                showToast("四個欄位請勿留空")
            } else {
                try {
                    dbrw.execSQL(
                        "INSERT INTO carTable(brand, model, year, price) VALUES(?,?,?,?)",
                        arrayOf(
                            edBrand.text.toString(),
                            edModel.text.toString(),
                            edYear.text.toString(),
                            edPrice.text.toString()
                        )
                    )
                    showToast("新增：${edBrand.text} ${edModel.text} (${edYear.text}年)，價格：${edPrice.text}萬")
                    cleanEditText()
                } catch (e: Exception) {
                    showToast("新增失敗 (可能廠牌已存在)：$e")
                }
            }
        }

        // 2. 修改功能 (依據主鍵：廠牌，修改車型、年份、價格)
        findViewById<Button>(R.id.btnUpdate).setOnClickListener {
            if (edBrand.length() < 1 || edModel.length() < 1 || edYear.length() < 1 || edPrice.length() < 1) {
                showToast("修改時，廠牌與欲更新的車型、年份、價格皆請勿留空")
            } else {
                try {
                    dbrw.execSQL(
                        "UPDATE carTable SET model = ?, year = ?, price = ? WHERE brand LIKE ?",
                        arrayOf(
                            edModel.text.toString(),
                            edYear.text.toString(),
                            edPrice.text.toString(),
                            edBrand.text.toString()
                        )
                    )
                    showToast("更新廠牌 [${edBrand.text}] 的車輛資料成功")
                    cleanEditText()
                } catch (e: Exception) {
                    showToast("更新失敗：$e")
                }
            }
        }

        // 3. 刪除功能 (依據主鍵：廠牌刪除資料)
        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            if (edBrand.length() < 1) {
                showToast("刪除時，車子廠牌請勿留空")
            } else {
                try {
                    dbrw.execSQL("DELETE FROM carTable WHERE brand LIKE '${edBrand.text}'")
                    showToast("刪除廠牌：${edBrand.text}")
                    cleanEditText()
                } catch (e: Exception) {
                    showToast("刪除失敗：$e")
                }
            }
        }

        // 4. 查詢功能 (無輸入廠牌則查全部，有輸入則查特定廠牌)
        findViewById<Button>(R.id.btnQuery).setOnClickListener {
            val queryString = if (edBrand.length() < 1) {
                "SELECT * FROM carTable"
            } else {
                "SELECT * FROM carTable WHERE brand LIKE '${edBrand.text}'"
            }

            val c = dbrw.rawQuery(queryString, null)
            c.moveToFirst() // 從第一筆開始輸出
            items.clear()   // 清空舊資料
            showToast("共有 ${c.count} 筆車輛資料")

            for (i in 0 until c.count) {
                // 欄位索引：0=brand, 1=model, 2=year, 3=price
                val brand = c.getString(0)
                val model = c.getString(1)
                val year = c.getInt(2)
                val price = c.getInt(3)

                items.add("廠牌:$brand\t\t車型:$model\n年份:${year}年\t\t價格:${price}萬")
                c.moveToNext() // 移動到下一筆
            }
            adapter.notifyDataSetChanged() // 更新清單資料
            c.close() // 關閉 Cursor
        }
    }

    // 顯示 Toast 訊息
    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    // 清空四個輸入框
    private fun cleanEditText() {
        findViewById<EditText>(R.id.edBrand).setText("")
        findViewById<EditText>(R.id.edModel).setText("")
        findViewById<EditText>(R.id.edYear).setText("")
        findViewById<EditText>(R.id.edPrice).setText("")
    }
}