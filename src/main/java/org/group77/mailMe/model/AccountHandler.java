package org.group77.mailMe.model;

public class AccountHandler {
  /*
   * @author
   * */
  Account activeAccount;
  AccountFactory accountFactory;

  public AccountHandler() {
    this.accountFactory = new AccountFactory();
  }


  public Account getActiveAccount() {
    return activeAccount;
  }

  public boolean setActiveAccount(Account account) {
    activeAccount = account;
    return true;
  }

  public Account createAccount(String emailAddress, String password) {
    return accountFactory.createAccount(emailAddress, password);
  }


}
