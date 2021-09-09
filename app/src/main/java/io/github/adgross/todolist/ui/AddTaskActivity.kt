package io.github.adgross.todolist.ui

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import io.github.adgross.todolist.R
import io.github.adgross.todolist.databinding.ActivityAddTaskBinding
import io.github.adgross.todolist.datasource.TaskDataSource
import io.github.adgross.todolist.extensions.text
import io.github.adgross.todolist.model.Task
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private var newTaskId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set our task id, default means a new task should be created
        newTaskId = intent.getIntExtra("TASK_ID", 0)

        // if edit mode, load values
        if(intent.hasExtra("TASK_ID")) {
            TaskDataSource.findById(newTaskId)?.let {
                binding.tilTitle.text = it.title
                binding.tilDate.text = it.date
                binding.tilTime.text = it.time
            }
            // change button text
            binding.btnCreatetask.text = resources.getText(R.string.modify_task)
        }

        insertListeners()
    }

    private fun insertListeners() {
        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val date = Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC"))
                val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                binding.tilDate.text = date.format(dateFormatter)
            }
            datePicker.show(supportFragmentManager, "DATA_PICKER")
        }

        binding.tilTime.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder().setTimeFormat(CLOCK_24H).build()
            timePicker.addOnPositiveButtonClickListener {
                binding.tilTime.text =
                    String.format("%02d:%02d", timePicker.hour, timePicker.minute)
            }
            timePicker.show(supportFragmentManager, "TIME_PICKER")
        }

        binding.btnCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        binding.btnCreatetask.setOnClickListener {
            val task = Task(
                title = binding.tilTitle.text,
                date = binding.tilDate.text,
                time = binding.tilTime.text,
                id = newTaskId
            )
            TaskDataSource.insertTask(task)

            setResult(Activity.RESULT_OK)
            finish()
        }

        binding.toolbar.setNavigationOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}
