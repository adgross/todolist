package io.github.adgross.todolist.datasource

import io.github.adgross.todolist.model.Task

object TaskDataSource {
    private val list = arrayListOf<Task>()
    private var lastId: Int = 1

    fun getList() = list

    fun findById(taskId: Int) = list.find { it.id == taskId }

    fun insertTask(task: Task) {
        if (task.id == 0) {
            list.add(task.copy(id = lastId++))
        } else {
            val index = list.indexOf(task)
            list[index] = task
        }
    }

    fun deleteTask(task: Task) {
        list.remove(task)
    }

}