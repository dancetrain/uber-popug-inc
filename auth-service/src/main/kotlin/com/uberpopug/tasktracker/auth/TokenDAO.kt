package com.uberpopug.tasktracker.auth

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.myndocs.oauth2.identity.Identity
import nl.myndocs.oauth2.token.AccessToken
import nl.myndocs.oauth2.token.ExpirableToken
import nl.myndocs.oauth2.token.RefreshToken
import org.mapdb.*
import java.lang.IllegalArgumentException
import java.time.Instant

/**
 * @author Pavel Borsky
 */


data class AccessTokenDTO(
  val accessToken: String,
  val tokenType: String,
  override val expireTime: Instant,
  val identity: Identity?,
  val clientId: String,
  val scopes: Set<String>,
  val refreshToken: RefreshToken?
): ExpirableToken {

  fun toAccessToken(): AccessToken {
    return AccessToken(
      accessToken,
      tokenType,
      expireTime,
      identity,
      clientId,
      scopes,
      refreshToken
    )
  }
}

interface TokenDAO<T: ExpirableToken> {
  suspend fun wipe()
  suspend fun storeToken(token: T)
  suspend fun locateToken(token: String): T?
}

class TokenSerializer: Serializer<AccessTokenDTO> {
  override fun serialize(out: DataOutput2, value: AccessTokenDTO) {
    val data = Json.encodeToString(value)
    out.write(data.toByteArray(Charsets.UTF_8))
  }

  override fun deserialize(input: DataInput2, available: Int): AccessTokenDTO {
    val data = ByteArray(available)
    input.readFully(data, 0, available)
    return Json.decodeFromString(data.toString(Charsets.UTF_8))
  }
}

class FileBasedAccessTokenDAO(
  db: DB
): TokenDAO<AccessToken> {

  private val repository: HTreeMap<String, AccessToken> =
    db.hashMap(
      name = "accountDAO",
      keySerializer = Serializer.STRING,
      valueSerializer = TokenSerializer()
    ).createOrOpen() as HTreeMap<String, AccessToken>


  override suspend fun wipe() {
    repository.clear()
  }

  override suspend fun storeToken(token: AccessToken) {
    repository.compute(token.accessToken) { _, stored ->
      if (stored != null) {
        throw IllegalArgumentException("Token is not unique")
      }
      token
    }
  }

  override suspend fun locateToken(token: String): AccessToken? {
    return repository[token]
  }
}
