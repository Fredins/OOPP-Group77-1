package org.group77.mejl.model;

public class AccountFactory {

    public Account createAccount(String emailAddress, String password){
        if (emailAddress.contains("@gmail.com")){
           return new Account(emailAddress, password, ServerProvider.GMAIL);
        }
        return null;
    }
    
    private Account createGmailAccount(String emailAddress, String password) {
        return null;
    }
    
    private Account createMicrosoftAccount(String emailAddress, String password) {
        return null;
    }

}
