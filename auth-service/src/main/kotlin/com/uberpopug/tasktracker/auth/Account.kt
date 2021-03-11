package com.uberpopug.tasktracker.auth

import kotlinx.serialization.Serializable

/**
 * @author Pavel Borsky
 */

@Serializable
data class Role(
  val name: String
)
@Serializable
data class AccountInfo(
  val username: String
)

@Serializable
data class AuthInfo(
  val passwordHash: String,
  val passwordSalt: String
) {
  companion object {
    val NONE = AuthInfo("", "")
  }
}
@Serializable
data class Account(
  val accountInfo: AccountInfo,
  val authInfo: AuthInfo = AuthInfo.NONE,
  val roles: Set<Role> = emptySet()
) {
  val publicId: String
    get() = accountInfo.username
}
