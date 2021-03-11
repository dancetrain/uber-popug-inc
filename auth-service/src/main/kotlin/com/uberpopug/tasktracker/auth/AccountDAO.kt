package com.uberpopug.tasktracker.auth

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


class InMemoryAccountDAO: AccountDAO {
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
