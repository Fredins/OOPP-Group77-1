module org.group77.mejl {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.mail;
    requires commons.email;

    exports org.group77.mailMe;
    exports org.group77.mailMe.controller;
    opens org.group77.mailMe.controller to javafx.fxml;
}

