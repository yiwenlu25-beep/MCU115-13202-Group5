package com.example.pomodorotimer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            Log.d("BootReceiver", "裝置已重新啟動，準備開始每日學習課程")
            Toast.makeText(context, "歡迎回來！別忘了開啟今日的番茄鐘專注課程。", Toast.LENGTH_LONG).show()
        }
    }
}