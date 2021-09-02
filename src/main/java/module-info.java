module org.group77.mejl {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.mail;


    opens org.group77.mejl to javafx.fxml;
    exports org.group77.mejl;
    exports org.group77.mejl.controllers;
    opens org.group77.mejl.controllers to javafx.fxml;
}