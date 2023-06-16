module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
    exports com.example.demo.Controllers;
    opens com.example.demo.Controllers to javafx.fxml;
    exports com.example.demo.Controllers.Levels;
    opens com.example.demo.Controllers.Levels to javafx.fxml;
    exports com.example.demo.Controllers.SpriteAnimations;
    opens com.example.demo.Controllers.SpriteAnimations to javafx.fxml;
}