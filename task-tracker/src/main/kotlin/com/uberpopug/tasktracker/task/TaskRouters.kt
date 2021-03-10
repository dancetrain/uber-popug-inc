package com.uberpopug.tasktracker.task

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*


/**
 * @author Pavel Borsky
 */
fun Application.taskRouters() {
  val taskDAO: TaskDAO = InMemoryTaskDAO()

  routing {
    route("/task") {
      get("") {
        call.respond(taskDAO.listAll())
      }
      post {
        val taskInfo = call.receive<TaskInfo>()
        val task = taskDAO.createTask()
          .copy(taskInfo = taskInfo)
        taskDAO.updateTask(task)
        call.respond(HttpStatusCode.Created, task)
      }
      put("/{taskId}") {
        val taskInfo = call.receive<TaskInfo>()
        val taskId = call.parameters["taskId"]!!
        val task = taskDAO.updateTask(Task(taskId, taskInfo))
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
