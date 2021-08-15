module com.example.guichat {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.guichat to javafx.fxml;
    exports com.example.guichat;
}