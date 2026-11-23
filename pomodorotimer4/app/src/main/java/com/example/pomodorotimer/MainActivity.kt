package com.example.pomodorotimer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    companion object {
        const val ACTION_TIMER_UPDATE = "com.example.pomodorotimer.TIMER_UPDATE"
        const val EXTRA_REMAINING_SECONDS = "remaining_seconds"
        private const val FOCUS_DURATION_SECONDS = 25 * 60 // 測試時可先改成 10
    }

    private lateinit var tvStatus: TextView
    private lateinit var tvTimer: TextView
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button
    private var isReceiverRegistered = false

    private val timerUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (ACTION_TIMER_UPDATE == intent.action) {
                val remainingSeconds = intent.getIntExtra(EXTRA_REMAINING_SECONDS, 0)
                updateTimerUI(remainingSeconds)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tvStatus)
        tvTimer = findViewById(R.id.tvTimer)
        btnStart = findViewById(R.id.btnStart)
        btnStop = findViewById(R.id.btnStop)

        registerTimerReceiver()

        btnStart.setOnClickListener {
            registerTimerReceiver()
            tvStatus.text = "Focusing..."
            tvStatus.setTextColor(Color.parseColor("#E53935"))

            val serviceIntent = Intent(this, TimerService::class.java).apply {
                putExtra(EXTRA_REMAINING_SECONDS, FOCUS_DURATION_SECONDS)
            }
            startService(serviceIntent)
        }

        btnStop.setOnClickListener {
            val serviceIntent = Intent(this, TimerService::class.java)
            stopService(serviceIntent)

            tvStatus.text = "Ready to Focus"
            tvStatus.setTextColor(Color.parseColor("#2E7D32"))
            updateTimerUI(FOCUS_DURATION_SECONDS)
        }
    }

    private fun registerTimerReceiver() {
        if (!isReceiverRegistered) {
            val filter = IntentFilter(ACTION_TIMER_UPDATE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                registerReceiver(timerUpdateReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
            } else {
                registerReceiver(timerUpdateReceiver, filter)
            }
            isReceiverRegistered = true
        }
    }

    private fun updateTimerUI(totalSeconds: Int) {
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        tvTimer.text = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isReceiverRegistered) {
            unregisterReceiver(timerUpdateReceiver)
            isReceiverRegistered = false
        }
    }
}