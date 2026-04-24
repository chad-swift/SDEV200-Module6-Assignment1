module org.example.sdev200module6assignment1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires java.sql;
    requires java.desktop;


    opens org.example.sdev200module6assignment1 to javafx.fxml;
    exports org.example.sdev200module6assignment1;
}