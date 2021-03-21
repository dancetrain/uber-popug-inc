package com.uberpopug.tasktracker.task

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mapdb.*
import java.lang.RuntimeException
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Pavel Borsky
 */
interface TaskDAO {

  suspend fun createTask(): Task
  suspend fun findTask(taskId: String): Task?
  suspend fun updateTask(taskId: String, updateFn: (Task) -> Task): Task
  suspend fun deleteTask(taskId: String): Task?
  suspend fun listAll(): List<Task>
  suspend fun findInComplete(): List<Task>
  suspend fun findByPopug(popugName: String): List<Task>
  suspend fun isEmpty(): Boolean
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

  override suspend fun findTask(taskId: String): Task? {
    return repository[taskId]
  }

  override suspend fun updateTask(taskId: String, updateFn: (Task) -> Task): Task {
    val task = repository.compute(taskId) { _, currentTask ->
      if (currentTask != null) {
        updateFn.invoke(currentTask)
      } else {
        null
      }
    }

    if (task != null) {
      return task
    } else {
      throw RuntimeException("Task $taskId NotFound")
    }
  }

  override suspend fun deleteTask(taskId: String): Task? {
    return repository.remove(taskId)
  }

  override suspend fun listAll(): List<Task> {
    return repository.values.toList()
  }

  override suspend fun findInComplete(): List<Task> {
    return repository.values.filterNot { it.complete }
  }

  override suspend fun findByPopug(popugName: String): List<Task> {
    return repository.values.filter { it.assignedPopug?.publicName == popugName }
  }

  override suspend fun isEmpty(): Boolean =
    repository.isEmpty()
}
class TaskSerializer: Serializer<Task> {
  override fun serialize(out: DataOutput2, value: Task) {
    val data = Json.encodeToString(value)
      .toByteArray(Charsets.UTF_8)
    out.write(data)
  }

  override fun deserialize(input: DataInput2, available: Int): Task {
    val data = ByteArray(available)
    input.readFully(data, 0, available)
    return Json.decodeFromString(data.toString(Charsets.UTF_8))
  }
}
class FileBasedTaskDAO(db: DB): TaskDAO {
  private val repository: HTreeMap<String, Task> =
    db.hashMap(
      name = "task-dao",
      keySerializer = Serializer.STRING,
      valueSerializer = TaskSerializer()
    ).createOrOpen() as HTreeMap<String, Task>

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

  override suspend fun updateTask(taskId: String, updateFn: (Task) -> Task): Task {
    val task = repository.compute(taskId) { _, currentTask ->
      if (currentTask != null) {
        updateFn.invoke(currentTask)
      } else {
        null
      }
    }

    if (task != null) {
      return task
    } else {
      throw RuntimeException("Task $taskId NotFound")
    }
  }

  override suspend fun deleteTask(taskId: String): Task? {
    return repository.remove(taskId)
  }

  override suspend fun listAll(): List<Task> {
    return repository.values.filterNotNull()
      .toList()
  }

  override suspend fun findInComplete(): List<Task> {
    return repository.values.filterNotNull()
      .filterNot { it.complete }
  }

  override suspend fun findByPopug(popugName: String): List<Task> {
    return repository.values.filterNotNull()
      .filter { it.assignedPopug?.publicName == popugName }
  }
  override suspend fun isEmpty(): Boolean =
    repository.isEmpty()
}
