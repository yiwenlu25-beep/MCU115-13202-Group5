package com.example.threadhandlerwizard

import android.app.Application
import android.media.MediaPlayer
import android.os.Handler
import android.os.HandlerThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class WorkViewModel(application: Application) : AndroidViewModel(application) {
    private val handlerThread = HandlerThread("VM-Work").apply { start() }
    private val worker = Handler(handlerThread.looper)

    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> = _progress

    private val _status = MutableLiveData("Idle")
    val status: LiveData<String> = _status

    @Volatile private var running = false
    private var mediaPlayer: MediaPlayer? = null

    fun start() {
        if (running) return
        running = true
        _status.postValue("準備中…")
        _progress.postValue(0)

        worker.post {
            try {
                // 1. 準備階段 (3 秒)
                repeat(30) {
                    if (!running) {
                        finishWork("背景工作結束")
                        return@post
                    }
                    Thread.sleep(100)
                }

                // 2. 開始背景工作，播放背景音樂
                _status.postValue("工作中…")
                startMusic()

                // 3. 執行背景工作 1% ~ 100%
                for (i in 1..100) {
                    if (!running) break
                    Thread.sleep(100)
                    _progress.postValue(i)
                }

                finishWork("背景工作結束")
            } catch (_: InterruptedException) {
                finishWork("背景工作結束")
            }
        }
    }

    private fun startMusic() {
        try {
            stopMusic()
            mediaPlayer = MediaPlayer.create(getApplication(), R.raw.bgm).apply {
                isLooping = true
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
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
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaPlayer = null
        }
    }

    private fun finishWork(finalStatus: String) {
        running = false
        stopMusic()
        _status.postValue(finalStatus)
    }

    fun cancel() {
        finishWork("背景工作結束")
    }

    override fun onCleared() {
        finishWork("背景工作結束")
        handlerThread.quitSafely()
        super.onCleared()
    }
}
