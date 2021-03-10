package com.uberpopug.tasktracker

import com.uberpopug.tasktracker.task.taskRouters
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import kotlinx.serialization.json.Json

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

/**
 * @author Pavel Borsky
 */
fun Application.main() {
  install(DefaultHeaders)
  install(CallLogging)
  install(ContentNegotiation) {
    json(Json {
      prettyPrint = true
    })
  }
  install(Routing) {
    get("/") {
      call.respondText(
        "",
        ContentType.parse("application/json"),
        HttpStatusCode.NoContent
      )
    }

    taskRouters()
  }
}
