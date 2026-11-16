package com.example.pomodorotimer

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import kotlin.math.ceil

class TimerService : Service() {

    private var remainingSeconds = 0
    private var isRunning = false
    private var timerThread: Thread? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    private val systemEventReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                Intent.ACTION_SCREEN_ON -> {
                    val minsLeft = ceil(remainingSeconds / 60.0).toInt()
                    Toast.makeText(
                        context,
                        "Focus Reminder: $minsLeft mins remaining!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                Intent.ACTION_HEADSET_PLUG -> {
                    val state = intent.getIntExtra("state", -1)
                    if (state == 0) {
                        Toast.makeText(context, "耳機已拔除：暫停環境專注音效", Toast.LENGTH_SHORT).show()
                    } else if (state == 1) {
                        Toast.makeText(context, "耳機已連接：恢復環境專注音效", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_HEADSET_PLUG)
        }
        registerReceiver(systemEventReceiver, filter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null && intent.hasExtra(MainActivity.EXTRA_REMAINING_SECONDS)) {
            remainingSeconds = intent.getIntExtra(MainActivity.EXTRA_REMAINING_SECONDS, 25 * 60)
        }

        if (!isRunning) {
            isRunning = true
            startCountdownThread()
        }

        return START_NOT_STICKY
    }

    private fun startCountdownThread() {
        timerThread = Thread {
            while (isRunning && remainingSeconds > 0) {
                try {
                    Thread.sleep(1000)
                    if (!isRunning) break

                    remainingSeconds--

                    val updateIntent = Intent(MainActivity.ACTION_TIMER_UPDATE).apply {
                        setPackage(packageName)
                        putExtra(MainActivity.EXTRA_REMAINING_SECONDS, remainingSeconds)
                    }
                    sendBroadcast(updateIntent)
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    break
                }
            }

            if (isRunning && remainingSeconds <= 0) {
                isRunning = false
                mainHandler.post {
                    val finishIntent = Intent(this@TimerService, FinishActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    startActivity(finishIntent)
                    stopSelf()
                }
            }
        }
        timerThread?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        timerThread?.interrupt()
        unregisterReceiver(systemEventReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}