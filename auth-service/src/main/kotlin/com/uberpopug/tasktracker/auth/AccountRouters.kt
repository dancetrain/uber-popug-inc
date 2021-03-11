package com.uberpopug.tasktracker.auth

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

/**
 * @author Pavel Borsky
 */
fun Application.authRouters() {
  val repository: AccountDAO = InMemoryAccountDAO()

  routing {
    route("/auth") {
      get("") {
        call.respond(repository.listAll())
      }
      post {
        val accountInfo = call.receive<AccountInfo>()
        repository.createAccount(accountInfo).also {
          call.respond(HttpStatusCode.Created, it)
        }
      }
      put("/{publicId}/info") {
        val accountInfo = call.receive<AccountInfo>()
        val publicId = call.parameters["publicId"]!!
        val account = repository.updateAccount(publicId, accountInfo)!!
        call.respond(HttpStatusCode.OK, account)
      }
      put("/{publicId}/roles") {
        val roles = call.receive<Set<Role>>()
        val publicId = call.parameters["publicId"]!!
        val account = repository.updateAccount(publicId, roles)!!
        call.respond(HttpStatusCode.OK, account)
      }
      get("/{publicId}") {
        val account = repository.findAccount(call.parameters["publicId"]!!)
        if (account != null) {
          call.respond(HttpStatusCode.OK, account)
        } else {
          call.respond(HttpStatusCode.NotFound)
        }
      }
      get("/{publicId}/info") {
        val account = repository.findAccount(call.parameters["publicId"]!!)
        if (account != null) {
          call.respond(HttpStatusCode.OK, account.accountInfo)
        } else {
          call.respond(HttpStatusCode.NotFound)
        }
      }
      get("/{publicId}/roles") {
        val account = repository.findAccount(call.parameters["publicId"]!!)
        if (account != null) {
          call.respond(HttpStatusCode.OK, account.roles)
        } else {
          call.respond(HttpStatusCode.NotFound)
        }
      }
      delete("/{publicId}") {
        val publicId = call.parameters["publicId"]!!
        val account = repository.deleteAccount(publicId)
        if (account != null) {
          call.respond(HttpStatusCode.OK, account)
        } else {
          // For security reasons, return OK?
          call.respond(HttpStatusCode.NotFound)
        }
      }
    }
  }
}
