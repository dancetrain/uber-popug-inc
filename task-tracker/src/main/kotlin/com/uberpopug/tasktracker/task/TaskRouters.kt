package com.uberpopug.tasktracker.task

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory


/**
 * @author Pavel Borsky
 */
fun Application.taskRouters() {
  val logger = LoggerFactory.getLogger("com.uberpopug.tasktracker.task.TaskRouters")
  val taskDAO: TaskDAO = InMemoryTaskDAO()

  runBlocking {
    repeat(10) {
      taskDAO.updateTask(taskId = taskDAO.createTask().taskId) { task ->
        task.copy(
          taskInfo = TaskInfo(
            taskTitle = "Task Title $it",
            taskDescription = "Task description $it"
          ),
        )
      }
    }
  }

  routing {
    route("/task-manage") {
      put("/reassign") {
        val popugRange = 1..10

        val tasks = taskDAO.findInComplete().map { task ->
          // get random popug
          taskDAO.updateTask(taskId = task.taskId) {
            if (it.complete) {
              // ignore
              it
            } else {
              val currentPopug = it.assignedPopug
              // @TODO(pavelb): subtract from popug account somehow
              it.copy(assignedPopug = UserInfo("Popug ${popugRange.random()}"))
            }
          }
        }.filterNot { it.complete }

        tasks.forEach { task ->
          // notify about assignment
          logger.info("New task assignment! {}", task)
        }
        call.respond(tasks)
      }
    }
    route("/task") {
      get("") {
        call.respond(taskDAO.findInComplete())
      }
      post {
        val taskInfo = call.receive<TaskInfo>()
        val task = taskDAO.updateTask(taskDAO.createTask().taskId) { task ->
          task.copy(taskInfo = taskInfo)
        }
        call.respond(HttpStatusCode.Created, task)
      }
      put("/{taskId}") {
        val taskInfo = call.receive<TaskInfo>()
        val taskId = call.parameters["taskId"]!!
        val task = taskDAO.updateTask(taskId) { task ->
          task.copy(taskInfo = taskInfo)
        }
        call.respond(HttpStatusCode.OK, task)
      }
      put("/{taskId}/complete") {
        val taskId = call.parameters["taskId"]!!
        val task = taskDAO.updateTask(taskId) { task ->
          task.copy(complete = true)
        }
        call.respond(HttpStatusCode.OK, task)
      }
      get("/{taskId}") {
        val task = taskDAO.findTask(call.parameters["taskId"]!!)
        if (task != null) {
          call.respond(HttpStatusCode.OK, task)
        } else {
          call.respond(HttpStatusCode.NotFound)
        }
      }
      delete("/{taskId}") {
        val taskId = call.parameters["taskId"]!!
        val task = taskDAO.deleteTask(taskId)
        if (task != null) {
          call.respond(HttpStatusCode.OK, task)
        } else {
          // For security reasons, return OK?
          call.respond(HttpStatusCode.NotFound)
        }
      }
    }
  }

}
