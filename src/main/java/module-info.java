module com.felipemeireles.sistemadebiblioteca {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires java.desktop;


    opens com.felipemeireles.sistemadebiblioteca to javafx.fxml;
    opens com.felipemeireles.sistemadebiblioteca.entity to javafx.base;

    exports com.felipemeireles.sistemadebiblioteca;
    exports com.felipemeireles.sistemadebiblioteca.entity;

    exports com.felipemeireles.sistemadebiblioteca.controller;
    opens com.felipemeireles.sistemadebiblioteca.controller to javafx.fxml;
}