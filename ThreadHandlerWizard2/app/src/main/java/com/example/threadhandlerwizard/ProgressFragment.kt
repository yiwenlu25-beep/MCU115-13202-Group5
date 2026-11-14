package com.example.threadhandlerwizard

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class ProgressFragment : Fragment(R.layout.frag_progress) {
    private val vm: WorkViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bar = view.findViewById<ProgressBar>(R.id.progressBar)
        val txtPercent = view.findViewById<TextView>(R.id.txtPercent)
        val txtStatus = view.findViewById<TextView>(R.id.txt)

        vm.status.observe(viewLifecycleOwner) { s ->
            txtStatus.text = s
            when {
                s.startsWith("準備中") -> {
                    bar.isIndeterminate = true
                    txtPercent.text = "準備中"
                }
                s == "背景工作結束" -> {
                    bar.isIndeterminate = false
                    bar.max = 100
                    bar.progress = 100
                    txtPercent.text = "100%"
                }
                s.startsWith("工作中") -> {
                    bar.isIndeterminate = false
                }
                else -> {
                    bar.isIndeterminate = false
                    txtPercent.text = "0%"
                }
            }
        }

        vm.progress.observe(viewLifecycleOwner) { p ->
            val currentStatus = vm.status.value ?: ""
            if (currentStatus.startsWith("工作中")) {
                bar.isIndeterminate = false
                bar.max = 100
                bar.progress = p
                txtPercent.text = "$p%"
                txtStatus.text = "工作中… $p%"
            }
        }
    }
}
