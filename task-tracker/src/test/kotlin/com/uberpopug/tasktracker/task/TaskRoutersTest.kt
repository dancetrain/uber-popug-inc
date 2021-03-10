package com.uberpopug.tasktracker.task

import com.uberpopug.tasktracker.module
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail

/**
 * @author Pavel Borsky
 */
class TaskRoutersTest {

  @Test
  fun testTaskListRequest() {
    withTestApplication({ module(testing = true) }) {
      handleRequest(HttpMethod.Get, "/task").apply {
        assertEquals(HttpStatusCode.OK, response.status())
      }
    }
  }

  @Test
  fun testTaskCreateRequest() {
    withTestApplication({ module(testing = true) }){
      handleRequest(HttpMethod.Post, "/task"){
        setBody("""{"taskTitle": "Test Task Title"}""")
        addHeader(HttpHeaders.ContentType, "application/json")
      }.apply {
        assertEquals(HttpStatusCode.Created, response.status())
        val body = response.content
        if (body == null) {
          fail("Missing response")
        } else {
          val task = Json.decodeFromString<Task>(body)

          assertEquals("Test Task Title", task.taskInfo?.taskTitle)
        }
      }
    }
  }

  @Test
  fun testTaskCRUD() {
    withTestApplication({ module(testing = true) }) {
      // C for create
      val task = handleRequest(HttpMethod.Post, "/task") {
        setBody("""{"taskTitle": "Test Task Title"}""")
        addHeader(HttpHeaders.ContentType, "application/json")
      }.let {
        assertEquals(HttpStatusCode.Created, it.response.status())
        parseTask(it.response)
      }

      assertEquals("Test Task Title", task.taskInfo?.taskTitle)

      // R for read
      val taskResponse =
        handleRequest(HttpMethod.Get, "/task/${task.taskId}") {
      }.let {
          parseTask(it.response)
      }
      assertEquals(task, taskResponse)

      // U for update
      val newTaskInfo = TaskInfo(taskTitle = "New Task Title")
      val taskModified =
        handleRequest(HttpMethod.Put, "/task/${task.taskId}") {
          setBody(Json.encodeToString(newTaskInfo))
          addHeader(HttpHeaders.ContentType, "application/json")
        }.let {
          parseTask(it.response)
        }

      assertEquals(newTaskInfo, taskModified.taskInfo)

      // D for delete
      val taskRemoved =
        handleRequest(HttpMethod.Delete, "/task/${task.taskId}") {
        }.let {
          parseTask(it.response)
        }

      assertEquals(taskModified, taskRemoved)

      // verify removal
      handleRequest(HttpMethod.Delete, "/task/${task.taskId}") {
        }.also {
          assertEquals(HttpStatusCode.NotFound, it.response.status())
        }
    }
  }

  companion object {
    fun parseTask(response: TestApplicationResponse): Task {
      val body = response.content
      return if (body == null) {
        fail("Missing response")
      } else {
        Json.decodeFromString(body)
      }
    }
  }

}
