module org.group77.mejl {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.mail;
    requires activation;

    exports org.group77.mejl;
    exports org.group77.mejl.controller;
    opens org.group77.mejl.controller to javafx.fxml;
}

