package com.shreysoral.stopwatch

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Chronometer
import android.widget.NumberPicker
import com.shreysoral.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isRunning = false
    private var minutes: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lapsList = ArrayList<String>()
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lapsList)
        binding.listView.adapter = arrayAdapter

        binding.lap.setOnClickListener {
            if (isRunning) {
                lapsList.add(binding.chronometer.text.toString())
                arrayAdapter.notifyDataSetChanged()
            }
        }

        binding.clock.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog)
            val numberPicker = dialog.findViewById<NumberPicker>(R.id.numberPicker)
            numberPicker.minValue = 0
            numberPicker.maxValue = 5
            dialog.findViewById<Button>(R.id.clock_time).setOnClickListener {
                minutes = numberPicker.value.toLong()
                binding.clockTime.text = "${numberPicker.value} mins"
                dialog.dismiss()
            }
            dialog.show()
        }

        binding.run.setOnClickListener {
            if (isRunning) {
                binding.chronometer.stop()
                isRunning = false
                binding.run.text = "Run"
            } else {
                if (minutes > 0) {
                    val totalMillis = minutes * 60 * 1000L
                    binding.chronometer.base = SystemClock.elapsedRealtime() + totalMillis
                    binding.chronometer.format = "%H:%M:%S"
                    binding.chronometer.setOnChronometerTickListener { chronometer ->
                        val elapsedMillis = SystemClock.elapsedRealtime() - chronometer.base
                        if (elapsedMillis >= totalMillis) {
                            chronometer.stop()
                            isRunning = false
                            binding.run.text = "Run"
                        }
                    }
                } else {
                    binding.chronometer.base = SystemClock.elapsedRealtime()
                    binding.chronometer.format = "%H:%M:%S"
                }
                binding.chronometer.start()
                isRunning = true
                binding.run.text = "Stop"
            }
        }
    }
}
