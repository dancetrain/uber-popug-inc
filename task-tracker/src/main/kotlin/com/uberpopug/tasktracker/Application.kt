package com.uberpopug.tasktracker

import com.uberpopug.tasktracker.task.taskRouters
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import kotlinx.serialization.json.Json
import org.slf4j.event.Level
import java.util.logging.Logger

fun main(args: Array<String>): Unit {
  val logger = Logger.getLogger("com.uberpopug.tasktracker.Application")
  logger.info("Starting task-tracker with params: [${args.joinToString(",")}]")
  io.ktor.server.netty.EngineMain.main(args)
  logger.info("Done.")
}

/**
 * @author Pavel Borsky
 */
fun Application.main() {
  install(DefaultHeaders)
  install(CallLogging) {
    level = Level.DEBUG
  }
  install(ContentNegotiation) {
    json(Json {
      prettyPrint = true
    })
  }
  install(Locations)
  install(Routing) {
    get("/") {
      call.respondText(
        "",
        ContentType.parse("application/json"),
        HttpStatusCode.NoContent
      )
    }
    get("/status") {
      call.respondText(
        """{"status": "OK"}""",
        ContentType.parse("application/json"),
        HttpStatusCode.OK
      )
    }

    taskRouters()
  }
}
