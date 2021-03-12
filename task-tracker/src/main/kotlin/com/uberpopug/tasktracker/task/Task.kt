package com.uberpopug.tasktracker.task

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class UserInfo(
  val publicName: String
)

@Serializable
data class TaskInfo(
  val taskTitle: String,
  val taskDescription: String,
)

@Serializable
data class Task(
  val taskId: String,
  val taskInfo: TaskInfo? = null,
  val complete: Boolean = false,
  val assignedPopug: UserInfo? = null
)
