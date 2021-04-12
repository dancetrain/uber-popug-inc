package com.uberpopug.tasktracker.auth

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

import org.mapdb.*

@Serializable
data class OAuthClientApplication(
  val name: String,
  val uid: String,
  val secret: String,
  val redirectUri: Set<String>,
  val scopes: Set<String>,
  val grantTypes: Set<String>,
  val confidential: Boolean
)

/**
 * @author Pavel Borsky
 */
interface ClientApplicationDAO {

  suspend fun wipe();
  suspend fun updateClientApplication(clientApplication: OAuthClientApplication)
  suspend fun findClientApp(clientId: String): OAuthClientApplication?
  suspend fun fetchAll(): List<OAuthClientApplication>
}

class ClientSerializer(): Serializer<OAuthClientApplication> {
  override fun serialize(out: DataOutput2, value: OAuthClientApplication) {
    val json = Json.encodeToString(value)
    out.write(json.toByteArray(Charsets.UTF_8))
  }

  override fun deserialize(input: DataInput2, available: Int): OAuthClientApplication {
    val data = ByteArray(available)
    input.readFully(data, 0, available)
    return Json.decodeFromString(data.toString(Charsets.UTF_8))
  }
}

class FileBasedClientApplicationDAO(
  db: DB
): ClientApplicationDAO {

  private val repository: HTreeMap<String, OAuthClientApplication> =
    db.hashMap(
      name = "ClientApplicationDAO",
      keySerializer = Serializer.STRING,
      valueSerializer = ClientSerializer()
    ).createOrOpen() as HTreeMap<String, OAuthClientApplication>

  override suspend fun wipe() {
    repository.clear()
  }

  override suspend fun updateClientApplication(clientApplication: OAuthClientApplication) {
    repository[clientApplication.uid] = clientApplication
  }

  override suspend fun findClientApp(clientId: String): OAuthClientApplication? {
    return repository[clientId]
  }

  override suspend fun fetchAll(): List<OAuthClientApplication> {
    return repository.values.filterNotNull()
  }
}
