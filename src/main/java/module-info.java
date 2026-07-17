module ec.edu.uce.taller2flitros {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires transitive javafx.graphics;


    opens ec.edu.uce.taller2flitros to javafx.fxml;
    exports ec.edu.uce.taller2flitros;

    opens ec.edu.uce.taller2flitros.controller to javafx.fxml;
    exports ec.edu.uce.taller2flitros.controller;

    opens ec.edu.uce.taller2flitros.demo to javafx.fxml;
    exports ec.edu.uce.taller2flitros.demo;
}
