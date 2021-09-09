package io.github.adgross.todolist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import io.github.adgross.todolist.databinding.ActivityMainBinding
import io.github.adgross.todolist.datasource.TaskDataSource

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    // code that will execute on "AddTaskActivity" result
    private val launchAddTask = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            updateList()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        insertListeners()
        updateList()
    }

    private fun updateList() {
        val list = TaskDataSource.getList()

        // hide empty message if we have tasks
        binding.includeEmpty.emptyLayout.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE

        binding.rvTasks.adapter = adapter
        adapter.submitList(list)
    }

    private fun insertListeners() {
        binding.fabAddTask.setOnClickListener {
            launchAddTask.launch(Intent(this, AddTaskActivity::class.java))
        }
        adapter.deleteCallBack = {
            TaskDataSource.deleteTask(it)
            updateList()
        }
        adapter.editCallBack = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra("TASK_ID", it.id)
            launchAddTask.launch(intent)
        }
    }
}