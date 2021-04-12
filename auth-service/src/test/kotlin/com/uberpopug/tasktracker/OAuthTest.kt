package com.uberpopug.tasktracker

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.util.concurrent.Executors
import kotlinx.html.*

/**
 * @author Pavel Borsky
 */
@Location("/")
class Index()
@Location("/login/{type?}")
class Login(val type: String = "")


val loginProviders = listOf(
          OAuthServerSettings.OAuth2ServerSettings(
                name = "popug-oauth",
                authorizeUrl = "http://localhost:9080/authorize",
                accessTokenUrl = "http://localhost:9080/token",
                clientId = "***",
                clientSecret = "***"
        ),
).associateBy { it.name }

private val exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4)

fun Application.OAuthLoginApplication() {
  OAuthLoginApplicationWithDeps(
    oauthHttpClient = HttpClient(Apache).apply {
      environment.monitor.subscribe(ApplicationStopping) {
        close()
      }
    }
  )
}

fun Application.OAuthLoginApplicationWithDeps(oauthHttpClient: HttpClient) {
  val authOauthForLogin = "authOauthForLogin"

  install(DefaultHeaders)
  install(CallLogging)
  install(Locations)
  install(Authentication) {
    oauth(authOauthForLogin) {
      client = oauthHttpClient
      providerLookup = {
        loginProviders[application.locations.resolve<Login>(Login::class, this).type]
      }
      urlProvider = { p ->
        redirectUrl(Login(p.name), false)
      }
    }
  }

  install(Routing) {
    get<Index> {
      call.respondHtml {
        head {
          title { +"index page" }
        }
        body {
          h1 {
            +"Try to login"
          }
          p {
            a(href = locations.href(Login())) {
              +"Login"
            }
          }
        }
      }
    }

    authenticate(authOauthForLogin) {
      location<Login>() {
        param("error") {
          handle {
            call.loginFailedPage(call.parameters.getAll("error").orEmpty())
          }
        }

        handle {
          val principal = call.authentication.principal<OAuthAccessTokenResponse>()
          if (principal != null) {
            call.loggedInSuccessResponse(principal)
          } else {
            call.loginPage()
          }
        }
      }
    }
  }
}


private fun <T : Any> ApplicationCall.redirectUrl(t: T, secure: Boolean = true): String {
    val hostPort = request.host()!! + request.port().let { port -> if (port == 80) "" else ":$port" }
    val protocol = when {
        secure -> "https"
        else -> "http"
    }
    return "$protocol://$hostPort${application.locations.href(t)}"
}

private suspend fun ApplicationCall.loginPage() {
    respondHtml {
        head {
            title { +"Login with" }
        }
        body {
            h1 {
                +"Login with:"
            }

            for (p in loginProviders) {
                p {
                    a(href = application.locations.href(Login(p.key))) {
                        +p.key
                    }
                }
            }
        }
    }
}

private suspend fun ApplicationCall.loginFailedPage(errors: List<String>) {
    respondHtml {
        head {
            title { +"Login with" }
        }
        body {
            h1 {
                +"Login error"
            }

            for (e in errors) {
                p {
                    +e
                }
            }
        }
    }
}

private suspend fun ApplicationCall.loggedInSuccessResponse(callback: OAuthAccessTokenResponse) {
    respondHtml {
        head {
            title { +"Logged in" }
        }
        body {
            h1 {
                +"You are logged in"
            }
            p {
                +"Your token is $callback"
            }
        }
    }
}
