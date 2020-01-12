module WordCount.main {
    requires javafx.web;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.logging;
    requires org.apache.commons.text;
    exports data;
    exports fx;
    opens fx;
}