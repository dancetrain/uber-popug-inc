package com.uberpopug.tasktracker

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

/**
 * @author Pavel Borsky
 */
fun Application.main() {
  install(DefaultHeaders)
  install(CallLogging)
  install(Routing) {
    get("/") {
      call.respondText(
        "",
        ContentType.parse("application/json"),
        HttpStatusCode.NoContent
      )
    }
  }
}
