module org.group77.mailMe {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.mail;
    requires commons.email;
    requires javafx.web;
    requires java.datatransfer;

    exports org.group77.mailMe;
    exports org.group77.mailMe.controller;
    exports org.group77.mailMe.model;
    exports org.group77.mailMe.model.data;
    opens org.group77.mailMe.controller to javafx.fxml;
}

