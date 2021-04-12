package com.uberpopug.tasktracker.auth

/**
 * @author Pavel Borsky
 */
class AccountService(
  private val accountDAO: AccountDAO
) {

  @Throws(IllegalArgumentException::class)
  suspend fun createAccount(username: String, apply: (Account) -> Account): Account {
    if (accountDAO.findAccount(username) != null) {
      throw IllegalArgumentException("Account $username already exist")
    }

    return accountDAO.createAccount(username) {
      apply.invoke(
        it.copy(
          accountInfo = AccountInfo(username),
          roles = setOf(Role(AccountRoles.USER.name))
        )
      )
    }
  }

  suspend fun fetchAccounts(page: Int, pageSize: Int): List<Account> {
    val fromIndex = page * pageSize
    return accountDAO.listAll().subList(fromIndex, fromIndex + pageSize)
  }
}
