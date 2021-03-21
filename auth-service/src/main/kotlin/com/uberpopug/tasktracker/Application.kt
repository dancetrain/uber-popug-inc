package com.uberpopug.tasktracker

import com.uberpopug.tasktracker.auth.FileBasedAccountDAO
import com.uberpopug.tasktracker.auth.authRouters
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import kotlinx.serialization.json.Json
import nl.myndocs.oauth2.client.inmemory.InMemoryClient
import nl.myndocs.oauth2.identity.inmemory.InMemoryIdentity
import nl.myndocs.oauth2.ktor.feature.Oauth2ServerFeature
import nl.myndocs.oauth2.tokenstore.inmemory.InMemoryTokenStore
import org.mapdb.DBMaker
import org.slf4j.event.Level
import java.util.logging.Logger

/**
 * @author Pavel Borsky
 */
fun main(args: Array<String>): Unit {
  val logger = Logger.getLogger("com.uberpopug.tasktracker.Application")
  logger.info("Starting task-tracker with params: [${args.joinToString(",")}]")
  io.ktor.server.netty.EngineMain.main(args)
  logger.info("Done.")
}

fun Application.module(testing: Boolean = false) {
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
  install(Routing)
  install(CORS) {
    method(HttpMethod.Options)
    method(HttpMethod.Put)

    header(HttpHeaders.AccessControlAllowHeaders)
    header(HttpHeaders.AccessControlAllowOrigin)
    header(HttpHeaders.ContentType)

    host("localhost:3000")
  }
  install(Oauth2ServerFeature)
  routing {
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
  }

  val db = DBMaker.fileDB("auth-db").make()
  val accountDAO = FileBasedAccountDAO(db)
  authRouters(accountDAO)
}
