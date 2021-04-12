package com.uberpopug.tasktracker.auth

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class AccountCreateRequest(
  val username: String,
  val password: String
)


var account: String? = null

/**
 * @author Pavel Borsky
 */
fun Application.authRouters(accountService: AccountService) {
  routing {
    route("/account") {
      get("") {
        call.respond( accountService.fetchAccounts(0, 50))
      }
      post {
        val request = call.receive<AccountCreateRequest>()
        accountService.createAccount(
          request.username,
        ){
          it.copy(
            authInfo = AuthInfo(
              passwordHash = request.password,
              passwordSalt = ""
            )
          )
        }.also {
          call.respond(HttpStatusCode.Created, it)
        }
      }
      put("/{publicId}/info") {
        val accountInfo = call.receive<AccountInfo>()
        // we can't change name
        val publicId = call.parameters["publicId"]!!
//        val account = repository.updateAccount(publicId, accountInfo)!!
        call.respond(HttpStatusCode.OK, "account")
      }
      put("/{publicId}/roles") {
        val roles = call.receive<Set<Role>>()
        val publicId = call.parameters["publicId"]!!
//        val account = repository.updateAccount(publicId, roles)!!
        call.respond(HttpStatusCode.OK, "account")
      }
      get("/{publicId}") {
//        val account = repository.findAccount(call.parameters["publicId"]!!)
        if (account != null) {
          call.respond(HttpStatusCode.OK, "account")
        } else {
          call.respond(HttpStatusCode.NotFound)
        }
      }
      get("/{publicId}/info") {
//        val account = repository.findAccount(call.parameters["publicId"]!!)
        if (account != null) {
          call.respond(HttpStatusCode.OK, "account.accountInfo")
        } else {
          call.respond(HttpStatusCode.NotFound)
        }
      }
      get("/{publicId}/roles") {
//        val account = repository.findAccount(call.parameters["publicId"]!!)
        if (account != null) {
          call.respond(HttpStatusCode.OK, "account.roles")
        } else {
          call.respond(HttpStatusCode.NotFound)
        }
      }
      delete("/{publicId}") {
        val publicId = call.parameters["publicId"]!!
//        val account = repository.deleteAccount(publicId)
        if (account != null) {
          call.respond(HttpStatusCode.OK, "account")
        } else {
          // For security reasons, return OK?
          call.respond(HttpStatusCode.NotFound)
        }
      }
    }
  }
}
