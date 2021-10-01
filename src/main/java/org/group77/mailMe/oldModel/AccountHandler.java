package org.group77.mailMe.oldmodel;

import org.group77.mailMe.model.data.*;

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

  public Account createAccount(String emailAddress, char[] password) {
    return accountFactory.createAccount(emailAddress, password);
  }


}
