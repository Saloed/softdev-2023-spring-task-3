module org.game.view_control {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.game.view_control to javafx.fxml;
    exports org.game.view_control;
}