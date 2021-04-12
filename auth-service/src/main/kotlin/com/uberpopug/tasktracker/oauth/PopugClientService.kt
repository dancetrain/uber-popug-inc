package com.uberpopug.tasktracker.oauth

import com.uberpopug.tasktracker.auth.ClientApplicationDAO
import com.uberpopug.tasktracker.auth.OAuthClientApplication
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import nl.myndocs.oauth2.client.Client
import nl.myndocs.oauth2.client.ClientService

/**
 *
 *
 * @author Pavel Borsky
 */

fun OAuthClientApplication.toClient(): Client {
  return Client(uid, scopes, redirectUri, grantTypes)
}

class PopugClientService(
  private val clientApplicationDAO: ClientApplicationDAO,
  private val coroutineDispatcher: CoroutineDispatcher
): ClientService {
    override fun clientOf(clientId: String): Client? {
      return runBlocking(coroutineDispatcher) {
        clientApplicationDAO.findClientApp(clientId)?.toClient()
      }
    }

    override fun validClient(client: Client, clientSecret: String): Boolean {
      return runBlocking(coroutineDispatcher) {
        clientApplicationDAO.findClientApp(client.clientId)?.secret == clientSecret
      }
    }
}
