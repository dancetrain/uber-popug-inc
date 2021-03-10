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
  val taskOwner: UserInfo? = null,
  val assignedUser: UserInfo? = null
)

@Serializable
data class Task(
  val taskId: String,
  val taskInfo: TaskInfo? = null
)
