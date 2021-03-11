package com.uberpopug.tasktracker

import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * @author Pavel Borsky
 */
class ApplicationTest {

  @Test
  fun testRootRequest() {
    withTestApplication({ module(testing = true) }) {
      handleRequest(HttpMethod.Get, "/").apply {
        assertEquals(HttpStatusCode.NoContent, response.status())
      }
    }
  }

  @Test
  fun testStatusRequest() {
    withTestApplication({ module(testing = true) }) {
      handleRequest(HttpMethod.Get, "/status").apply {
        assertEquals(HttpStatusCode.OK, response.status())
        response.headers[HttpHeaders.ContentType].apply {
          assertTrue(this?.contains("application/json") ?: false, "Wrong Content-Type: $this")
        }
      }
    }
  }
}
