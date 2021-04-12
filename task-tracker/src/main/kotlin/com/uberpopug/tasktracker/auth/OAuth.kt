package com.uberpopug.tasktracker.auth

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.routing.*

/**
 * @author Pavel Borsky
 */
@Location("/login/{type?}")
class Login(val type: String = "")

sealed class OAuthAccessTokenResponse : Principal {
    data class OAuth1a(
        val token: String, val tokenSecret: String,
        val extraParameters: Parameters = Parameters.Empty
    ) : OAuthAccessTokenResponse()

    data class OAuth2(
        val accessToken: String, val tokenType: String,
        val expiresIn: Long, val refreshToken: String?,
        val extraParameters: Parameters = Parameters.Empty
    ) : OAuthAccessTokenResponse()
}

val loginProviders = listOf(
  OAuthServerSettings.OAuth2ServerSettings(
    name = "popugOauth",
    authorizeUrl = "http://localhost:9080/oauth/authorize",
    accessTokenUrl = "http://localhost:9080/oauth/token",
    clientId = "***",
    clientSecret = "***"
  )
).associateBy { it.name }

