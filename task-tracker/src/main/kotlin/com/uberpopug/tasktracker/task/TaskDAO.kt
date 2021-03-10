package com.uberpopug.tasktracker.task

import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Pavel Borsky
 */
interface TaskDAO {

  suspend fun createTask(): Task
  suspend fun updateTask(task: Task): Task
  suspend fun deleteTask(task: Task): Task
  suspend fun listAll(): List<Task>
}

/**
 * @FIXME: A lot of data races will occur here
 */
class InMemoryTaskDAO(): TaskDAO {
  private val repository: MutableMap<String, Task> = ConcurrentHashMap()

  override suspend fun createTask(): Task {
    val taskId = UUID.randomUUID().toString()
    // recursive call until we succeed with random generation
    return if (repository.containsKey(taskId)) {
      createTask()
    } else {
      val task = Task(taskId)
      repository[taskId] = task
      task
    }
  }

  override suspend fun updateTask(task: Task): Task {
    repository[task.taskId] = task
    return task
  }

  override suspend fun deleteTask(task: Task): Task {
    repository.remove(task.taskId)
    return task
  }

  override suspend fun listAll(): List<Task> {
    return repository.values.toList()
  }
}
