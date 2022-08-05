module com.example.prova {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.rometools.rome;
    requires telegrambots;
    requires telegrambots.meta;
    requires org.slf4j;

    opens com.loginform.loginform to javafx.fxml;
    exports com.loginform.loginform;
}