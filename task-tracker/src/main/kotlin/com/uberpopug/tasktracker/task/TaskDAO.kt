package com.uberpopug.tasktracker.task

import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Pavel Borsky
 */
interface TaskDAO {

  suspend fun createTask(): Task
  suspend fun findTask(taskId: String): Task?
  suspend fun updateTask(task: Task): Task
  suspend fun deleteTask(taskId: String): Task?
  suspend fun listAll(): List<Task>
}

/**
 * @FIXME: A lot of data races will occur here
 */
class InMemoryTaskDAO(): TaskDAO {
  private val repository: MutableMap<String, Task> = ConcurrentHashMap()

  init {
    repeat(10) {
      val task = Task(
        taskId = UUID.randomUUID().toString(),
        taskInfo = TaskInfo(
          taskTitle = "Task Title $it",
          taskDescription = "Task description $it"
        ),
        taskOwner = UserInfo(
          publicName = "admin"
        )
      )
      repository[task.taskId] = task
    }
  }

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

  override suspend fun findTask(taskId: String): Task? {
    return repository[taskId]
  }

  override suspend fun updateTask(task: Task): Task {
    repository[task.taskId] = task
    return task
  }

  override suspend fun deleteTask(taskId: String): Task? {
    return repository.remove(taskId)
  }

  override suspend fun listAll(): List<Task> {
    return repository.values.toList()
  }
}
