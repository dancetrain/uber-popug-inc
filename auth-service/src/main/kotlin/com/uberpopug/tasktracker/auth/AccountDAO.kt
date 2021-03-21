package com.uberpopug.tasktracker.auth

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mapdb.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Pavel Borsky
 */
interface AccountDAO {
  suspend fun createAccount(accountInfo: AccountInfo): Account
  suspend fun findAccount(id: String): Account?
  suspend fun updateAccount(id: String, accountInfo: AccountInfo): Account?
  suspend fun updateAccount(id: String, roles: Iterable<Role>): Account?
  suspend fun deleteAccount(id: String): Account?
  suspend fun listAll(): List<Account>
}


class InMemoryAccountDAO(): AccountDAO {
  private val repository: MutableMap<String, Account> = ConcurrentHashMap()

  override suspend fun createAccount(accountInfo: AccountInfo): Account {
    val account = Account(accountInfo)
    if (!repository.contains(account.publicId)) {
      repository[account.publicId] = account
    } else {
      // @FIXME(pavelb): throw an error if same public id?
    }
    return account
  }

  override suspend fun findAccount(id: String): Account? {
    return repository[id]
  }

  override suspend fun updateAccount(id: String, accountInfo: AccountInfo): Account? {
    return repository.computeIfPresent(id) { _, acc ->
      acc.copy(accountInfo = accountInfo)
    }
  }

  override suspend fun updateAccount(id: String, roles: Iterable<Role>): Account? {
    return repository.computeIfPresent(id) { _, acc ->
      acc.copy(roles = roles.toSet())
    }
  }

  override suspend fun deleteAccount(id: String): Account? {
    return repository.remove(id)
  }

  override suspend fun listAll(): List<Account> {
    return repository.values.toList()
  }
}

class AccountSerializer: Serializer<Account> {

  override fun serialize(out: DataOutput2, value: Account) {
    val data = Json.encodeToString(value)
    out.write(data.toByteArray(Charsets.UTF_8))
  }

  override fun deserialize(input: DataInput2, available: Int): Account {
    val data = ByteArray(available)
    input.readFully(data, 0, available)
    return Json.decodeFromString(data.toString(Charsets.UTF_8))
  }
}

class FileBasedAccountDAO(db: DB): AccountDAO {

  private val repository: HTreeMap<String, Account> =
    db.hashMap(
      name = "accountDAO",
      keySerializer = Serializer.STRING,
      valueSerializer = AccountSerializer()
    ).createOrOpen() as HTreeMap<String, Account>

  override suspend fun createAccount(accountInfo: AccountInfo): Account {
    val account = Account(accountInfo)
    if (!repository.contains(account.publicId)) {
      repository[account.publicId] = account
    } else {
      // @FIXME(pavelb): throw an error if same public id?
    }
    return account
  }

  override suspend fun findAccount(id: String): Account? {
    return repository[id]
  }

  override suspend fun updateAccount(id: String, accountInfo: AccountInfo): Account? {
    return repository.computeIfPresent(id) { _, acc ->
      acc.copy(accountInfo = accountInfo)
    }
  }

  override suspend fun updateAccount(id: String, roles: Iterable<Role>): Account? {
    return repository.computeIfPresent(id) { _, acc ->
      acc.copy(roles = roles.toSet())
    }
  }

  override suspend fun deleteAccount(id: String): Account? {
    return repository.remove(id)
  }

  override suspend fun listAll(): List<Account> {
    return repository.values.filterNotNull().toList()
  }
}
