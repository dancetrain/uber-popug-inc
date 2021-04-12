package com.uberpopug.tasktracker

import com.uberpopug.tasktracker.auth.*
import com.uberpopug.tasktracker.oauth.PopugClientService
import com.uberpopug.tasktracker.oauth.PopugTokenStore
import com.uberpopug.tasktracker.oauth.PopupIdentityService
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.mapdb.DBMaker
import org.slf4j.event.Level
import java.util.logging.Logger

/**
 * @author Pavel Borsky
 */
fun main(args: Array<String>): Unit {
  val logger = Logger.getLogger("com.uberpopug.tasktracker.Application")
  logger.info("Starting auth-service with params: [${args.joinToString(",")}]")
  io.ktor.server.netty.EngineMain.main(args)
  logger.info("Done.")
}

fun Application.module() {
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
  routing {
    trace {
      application.log.debug(it.buildText())
    }
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
    get("/routes") {

      call.respondText {

        call.application.locations.registeredLocations.map {
          it.path
        }.toString()
      }
    }
  }
  val db = DBMaker
    .fileDB("auth-service-db").checksumHeaderBypass().make()
  val accountDAO = FileBasedAccountDAO(db)

  val identityService = PopupIdentityService(
    accountDAO,
    Dispatchers.IO
  )

  val clientService = PopugClientService(
    FileBasedClientApplicationDAO(db),
    Dispatchers.IO
  )

  val tokenStore = PopugTokenStore()

  val oAuthConfiguration = Configuration(
    identityService = identityService,
    clientService = clientService,
    tokenStore = tokenStore
  )

  oAuthRouters(oAuthConfiguration)


  authRouters(AccountService(accountDAO))
  environment.monitor.subscribe(ApplicationStopped){
    db.commit()
    db.close()
  }
}
