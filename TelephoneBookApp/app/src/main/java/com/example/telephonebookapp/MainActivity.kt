package com.example.telephonebookapp

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.telephonebookapp.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: TaskViewModel by viewModels()
    private var mediaPlayer: MediaPlayer? = null
    private var workerThread: Thread? = null
    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Start"
                1 -> "Progress"
                2 -> "Result"
                else -> ""
            }
        }.attach()

        binding.btnStart.setOnClickListener {
            if (!isRunning) {
                startBackgroundTask()
            }
        }

        binding.btnCancel.setOnClickListener {
            cancelBackgroundTask()
        }
    }

    private fun startBackgroundTask() {
        isRunning = true
        // Play background music
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(this, R.raw.background_music).apply {
                isLooping = true
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        workerThread = Thread {
            try {
                // 1. Preparing phase
                handler.post {
                    viewModel.updateProgress(0, "準備中", "準備中")
                }
                Thread.sleep(1000)

                if (!isRunning) return@Thread

                // 2. Working phase: Progress 0% to 100%
                for (i in 0..100 step 10) {
                    if (!isRunning) break
                    handler.post {
                        viewModel.updateProgress(i, "$i%", "工作中")
                    }
                    Thread.sleep(300)
                }

                if (isRunning) {
                    // Finished successfully
                    handler.post {
                        viewModel.updateProgress(100, "背景工作結束", "背景工作結束")
                        stopMusic()
                        isRunning = false
                    }
                }
            } catch (e: InterruptedException) {
                // Interrupted
            }
        }.apply { start() }
    }

    private fun cancelBackgroundTask() {
        if (isRunning) {
            isRunning = false
            workerThread?.interrupt()
            stopMusic()
            handler.post {
                viewModel.updateProgress(100, "背景工作結束", "背景工作結束")
            }
        }
    }

    private fun stopMusic() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.release()
            }
            mediaPlayer = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        workerThread?.interrupt()
        stopMusic()
    }
}
