module com.example.mymanager {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.mymanager to javafx.fxml;
    exports com.example.mymanager;
    exports controller;
    opens controller to javafx.fxml;
    exports model;
    opens model to java.base;
}