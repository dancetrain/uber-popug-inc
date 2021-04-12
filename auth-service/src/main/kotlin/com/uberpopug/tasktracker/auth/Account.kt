package com.uberpopug.tasktracker.auth

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * @author Pavel Borsky
 */

@Serializable
data class Role(
  val name: String
)

@Serializable
data class AccountInfo(
  val fullName: String
) {
  companion object {
    val EMPTY = AccountInfo("")
  }
}
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
  val username: String,
  val accountInfo: AccountInfo = AccountInfo.EMPTY,

  @Transient
  val authInfo: AuthInfo = AuthInfo.NONE,
  val roles: Set<Role> = emptySet()
) {
  val publicId: String
    get() = username
}
