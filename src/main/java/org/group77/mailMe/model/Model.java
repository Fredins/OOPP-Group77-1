package org.group77.mailMe.model;

import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.control.cell.*;
import javafx.util.*;
import org.group77.mailMe.model.data.*;

import java.util.*;

public class Model {

  public ObservableList<Email> visibleEmails;
  public ObservableList<Folder> folders;
  public SimpleObjectProperty<Account> activeAccount;
  public SimpleObjectProperty<Pair<Boolean, Email>> readingEmail; // if (readingEmail !=null) then openReadingView(readingEmail)

  public void refresh() {} // updates folders state
  public void addAccount(String emailAddress, String password) {}
  public void send() {}


  public Model() {
    // initial values of properties

    readingEmail = new SimpleObjectProperty<>(new Pair<>(false, null));

    activeAccount = new SimpleObjectProperty<>(
      new Account(
        "77grupp@gmail.com",
        new char[]{'a', 'b'},
        ServerProvider.GMAIL_PROVIDER)
    );

    visibleEmails = FXCollections.observableList(new ArrayList<>());

    folders = FXCollections.observableList(
      List.of(
        new Folder(
          "inbox",
          new Email[]{
            new Email("mig", new String[]{"dig"}, "inbox email", "hej"),
            new Email("mig2", new String[]{"dig2"}, "inbox email1!", "hej")
          }
        ),
        new Folder(
          "trash",
          new Email[]{
            new Email("mig", new String[]{"dig"}, "trash mail", "hej"),
          }
        )
      )
    );
  }

}



