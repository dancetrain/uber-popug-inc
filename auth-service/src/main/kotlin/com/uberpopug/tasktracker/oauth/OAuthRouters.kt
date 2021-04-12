package com.uberpopug.tasktracker.auth

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import nl.myndocs.oauth2.client.AuthorizedGrantType
import nl.myndocs.oauth2.client.Client
import nl.myndocs.oauth2.client.ClientService
import nl.myndocs.oauth2.client.inmemory.InMemoryClient
import nl.myndocs.oauth2.identity.IdentityService
import nl.myndocs.oauth2.identity.inmemory.InMemoryIdentity
import nl.myndocs.oauth2.ktor.feature.Oauth2ServerFeature
import nl.myndocs.oauth2.token.TokenStore
import nl.myndocs.oauth2.tokenstore.inmemory.InMemoryTokenStore
import java.util.*

/**
 * @author Pavel Borsky
 */

data class OAuthAccessRights(
  val token: String
)

//data class ApplicationClient(
//  val clientId: String,
//  val clientScopes: Set<String>,
//  val redirectUris: Set<String>,
//  val authorizedGrantTypes: Set<String>,
//  val applicationId: String,
//): Client(clientId, clientScopes, redirectUris, authorizedGrantTypes)

data class Configuration(
  val identityService: IdentityService,
  val clientService: ClientService,
  val tokenStore: TokenStore
) {

}

fun Application.oAuthRouters(configuration: Configuration) {
  install(Oauth2ServerFeature) {
    identityService = configuration.identityService
    clientService = configuration.clientService
    tokenStore = configuration.tokenStore

//    identityService = InMemoryIdentity()
//      .identity {
//        username = "foo-1"
//        password = "bar"
//      }
//      .identity {
//        username = "foo-2"
//        password = "bar"
//      }
//    clientService = InMemoryClient()
//      .client {
//        clientId = "app1-client"
//        clientSecret = "testpass"
//        scopes = setOf("admin")
//        redirectUris = setOf("https://localhost:8080/callback")
//        authorizedGrantTypes = setOf(
//          AuthorizedGrantType.AUTHORIZATION_CODE,
//          AuthorizedGrantType.PASSWORD,
//          AuthorizedGrantType.IMPLICIT,
//          AuthorizedGrantType.REFRESH_TOKEN
//        )
//      }
//      .client {
//        clientId = "app2-client"
//        clientSecret = "testpass"
//        scopes = setOf("user")
//        redirectUris = setOf("https://localhost:8080/callback")
//        authorizedGrantTypes = setOf(
//          AuthorizedGrantType.AUTHORIZATION_CODE
//        )
//      }
//    tokenStore = InMemoryTokenStore()
  }

  routing {

  }
}
