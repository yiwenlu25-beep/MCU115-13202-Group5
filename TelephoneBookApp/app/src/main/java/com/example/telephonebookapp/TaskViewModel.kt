package com.example.telephonebookapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskViewModel : ViewModel() {
    private val _progressPercent = MutableLiveData(0)
    val progressPercent: LiveData<Int> = _progressPercent

    private val _progressText = MutableLiveData("準備中")
    val progressText: LiveData<String> = _progressText

    private val _resultText = MutableLiveData("準備中")
    val resultText: LiveData<String> = _resultText

    fun updateProgress(percent: Int, pText: String, rText: String) {
        _progressPercent.value = percent
        _progressText.value = pText
        _resultText.value = rText
    }
}
