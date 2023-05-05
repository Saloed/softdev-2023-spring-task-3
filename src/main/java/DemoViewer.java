import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.awt.*;

public class DemoViewer extends Application {
    double XY; double XZ; double YZ;
    double lightness; double radius; Color lightColor;


    public static void main(String[] args){
        Application.launch();
    }
    @Override
    public void start(Stage stage) throws Exception{

        Label label = new Label("Rotation");
        Slider sliderXZ = new Slider(0, 360, 180);

        sliderXZ.setShowTickLabels(true);
        sliderXZ.setShowTickMarks(true);

        sliderXZ.setBlockIncrement(10);
        sliderXZ.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, //
                                Number oldValue, Number newValue) {
                XZ = newValue.doubleValue();
            }
        });


        Slider sliderXY = new Slider(0, 360, 180);

        sliderXY.setShowTickLabels(true);
        sliderXY.setShowTickMarks(true);

        sliderXY.setBlockIncrement(10);

        sliderXY.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, //
                                Number oldValue, Number newValue) {
                XY = newValue.doubleValue();
            }
        });

        Slider sliderYZ = new Slider(0, 360, 180);

        sliderYZ.setShowTickLabels(true);
        sliderYZ.setShowTickMarks(true);

        sliderYZ.setBlockIncrement(10);

        sliderYZ.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, //
                                Number oldValue, Number newValue) {
                YZ = newValue.doubleValue();
            }
        });
        Button apply = new Button("Apply");

        VBox rotation = new VBox();
        rotation.setSpacing(10);
        rotation.getChildren().addAll(label, sliderXY, sliderXZ, sliderYZ, apply);

        stage.setTitle("Demoview");
        Scene scene = new Scene(rotation, 350, 200);
        stage.setScene(scene);
        stage.show();

    }
}
