module org.kvantoriumproject.kvantoriumproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;


    opens org.kvantoriumproject.kvantoriumproject to javafx.fxml;
    exports org.kvantoriumproject.kvantoriumproject;
}