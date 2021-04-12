package com.uberpopug.tasktracker.oauth

import nl.myndocs.oauth2.token.*

/**
 * TODO: Security revise
 * TODO: Multithreading
 *
 * @author Pavel Borsky
 */
class PopugTokenStore(

) : TokenStore {
  private val accessTokens = mutableMapOf<String, AccessToken>()
  private val codes = mutableMapOf<String, CodeToken>()
  private val refreshTokens = mutableMapOf<String, RefreshToken>()

  override fun storeAccessToken(accessToken: AccessToken) {
    accessTokens[accessToken.accessToken] = accessToken

    if (accessToken.refreshToken != null) {
      storeRefreshToken(accessToken.refreshToken!!)
    }
  }

  override fun accessToken(token: String): AccessToken? =
    locateToken(accessTokens, token)

  override fun storeCodeToken(codeToken: CodeToken) {
    codes[codeToken.codeToken] = codeToken
  }

  override fun codeToken(token: String): CodeToken? =
    locateToken(codes, token)

  override fun consumeCodeToken(token: String): CodeToken? = codes.remove(token)

  override fun storeRefreshToken(refreshToken: RefreshToken) {
    refreshTokens[refreshToken.refreshToken] = refreshToken
  }

  override fun refreshToken(token: String): RefreshToken? =
    locateToken(refreshTokens, token)

  private fun <T : ExpirableToken> locateToken(tokens: MutableMap<String, T>, token: String): T? {
    var tokenFromMap = tokens[token]

    if (tokenFromMap != null && tokenFromMap.expired()) {
      tokens.remove(token)

      tokenFromMap = null
    }

    return tokenFromMap
  }

  override fun revokeAccessToken(token: String) {
    accessTokens.remove(token)
  }

  override fun revokeRefreshToken(token: String) {
    refreshTokens.remove(token)
  }
}
