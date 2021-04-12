package com.uberpopug.tasktracker.oauth

import com.uberpopug.tasktracker.auth.AccountDAO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import nl.myndocs.oauth2.client.Client
import nl.myndocs.oauth2.identity.Identity
import nl.myndocs.oauth2.identity.IdentityService

/**
 * @author Pavel Borsky
 */
class PopupIdentityService(
  private val accountDAO: AccountDAO,
  private val coroutineDispatcher: CoroutineDispatcher
): IdentityService {

  override fun identityOf(forClient: Client, username: String): Identity? {
    return runBlocking(coroutineDispatcher) {
      accountDAO.findAccount(username)?.let {
        Identity(it.username)
      }
    }
  }

  override fun allowedScopes(forClient: Client, identity: Identity, scopes: Set<String>): Set<String> {
    return runBlocking(coroutineDispatcher) {
      accountDAO.findAccount(identity.username)?.roles?.map {
        it.name
      }?.toSet() ?: emptySet()
    }
  }

  override fun validCredentials(forClient: Client, identity: Identity, password: String): Boolean {
    return runBlocking(coroutineDispatcher) {
      accountDAO.findAccount(identity.username)?.authInfo?.passwordHash == password
    }
  }
}
