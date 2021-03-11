package com.uberpopug.tasktracker.auth

import com.uberpopug.tasktracker.module
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author Pavel Borsky
 */
class AccountRoutersTest {

  @Test
  fun testAccountCRUD() {
    withTestApplication({ module(testing = true) }) {
      // C for create
      val createdAccount = handleRequest(HttpMethod.Post, "/auth") {
        setBody(Json.encodeToString(AccountInfo(username = "dancetrain")))
        addHeader(HttpHeaders.ContentType, "application/json")
      }.let {
        assertEquals(HttpStatusCode.Created, it.response.status())
        val account = Json.decodeFromString<Account>(it.response.content!!)
        assertEquals(AccountInfo(username = "dancetrain"), account.accountInfo)
        account
      }

      val infoUpdatedAccount = handleRequest(HttpMethod.Put, "/auth/${createdAccount.publicId}/info") {
        setBody(Json.encodeToString(AccountInfo(username = "[sd]Tommy")))
        addHeader(HttpHeaders.ContentType, "application/json")
      }.let {
        assertEquals(HttpStatusCode.OK, it.response.status())
        val account = Json.decodeFromString<Account>(it.response.content!!)
        assertEquals(AccountInfo(username = "[sd]Tommy"), account.accountInfo)
        account
      }
      handleRequest(HttpMethod.Get, "/auth/${createdAccount.publicId}/info") {
      }.also {
        assertEquals(HttpStatusCode.OK, it.response.status())
        val accountInfo = Json.decodeFromString<AccountInfo>(it.response.content!!)
        assertEquals(AccountInfo(username = "[sd]Tommy"), accountInfo)
      }
      val rolesUpdatedAccount = handleRequest(HttpMethod.Put, "/auth/${createdAccount.publicId}/roles") {
        setBody(Json.encodeToString(setOf(Role("admin"))))
        addHeader(HttpHeaders.ContentType, "application/json")
      }.let {
        assertEquals(HttpStatusCode.OK, it.response.status())
        val account = Json.decodeFromString<Account>(it.response.content!!)
        assertEquals(AccountInfo(username = "[sd]Tommy"), account.accountInfo)
        assertEquals(setOf(Role("admin")), account.roles)
        account
      }
      handleRequest(HttpMethod.Get, "/auth/${createdAccount.publicId}/roles") {
      }.also {
        assertEquals(HttpStatusCode.OK, it.response.status())
        val roles = Json.decodeFromString<Set<Role>>(it.response.content!!)
        assertEquals(setOf(Role("admin")), roles)
      }
    }
  }
}
