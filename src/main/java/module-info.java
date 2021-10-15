module org.group77.mailMe {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.mail;
    requires commons.email;
    requires javafx.web;
    requires java.datatransfer;
    requires org.controlsfx.controls;

    exports org.group77.mailMe.services.storage;
    exports org.group77.mailMe;
    exports org.group77.mailMe.model.exceptions;
    exports org.group77.mailMe.controller;
    exports org.group77.mailMe.model;
    exports org.group77.mailMe.model.data;
    opens org.group77.mailMe.controller to javafx.fxml;
}

