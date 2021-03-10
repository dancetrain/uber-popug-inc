package com.uberpopug.tasktracker.task

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.json.Json

/**
 * @author Pavel Borsky
 */
fun Application.taskRouters() {
  val taskDAO: TaskDAO = InMemoryTaskDAO()

  routing {
    route("/task") {
      get {
        call.respond(taskDAO.listAll())
      }
      post {
        val taskInfo = call.receive<TaskInfo>()
        val task = taskDAO.createTask()
          .copy(taskInfo = taskInfo)
        taskDAO.updateTask(task)
        call.respond(HttpStatusCode.Created, task)
      }
      put("{taskId}") {

      }
      get("{taskId}") {

      }
      delete("{taskId}") {

      }
    }

  }
}
