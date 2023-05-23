package events;

import javafx.beans.property.BooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class DragLight {

    private double recordMouseX;
    private double recordMouseY;

    private double actMouseX;
    private double actMouseY;

    private EventHandler<MouseEvent> recordMouse;
    private EventHandler<MouseEvent> changeActMouse;
    private EventHandler<MouseEvent> fixedPosition;

    private BooleanProperty isChangeable;
    public Boolean isDraggable(){return isChangeable.get();}
    public BooleanProperty getIsChangeable(){return isChangeable;}

    private boolean Cycle = false;

    private Node target;

    public DragLight(Node target, boolean isChangeable){
        this.target = target;
        this.isChangeable.set(isChangeable);
    }

    public DragLight(Node target){
        this(target, false);
    }

    // oбработчик

    private void createHandler(){
        recordMouse = event -> {
            if (event.isPrimaryButtonDown()){
                Cycle = true;
                recordMouseX = event.getSceneX();
                recordMouseY = event.getSceneY();
                actMouseX = event.getX();
                actMouseY = event.getY();
            }
            if (event.isSecondaryButtonDown()){
                target.setTranslateX(300);
                target.setTranslateY(250);
                Cycle = false;
            }
        };
        changeActMouse = event -> {
          if (Cycle){
              target.setTranslateY(event.getSceneY() - recordMouseY);
              target.setTranslateX(event.getSceneX() - recordMouseX);
          }
        };
        fixedPosition = event -> {
            if (Cycle){
                target.setLayoutX(event.getSceneX() - actMouseX);
                target.setLayoutY(event.getSceneY() - actMouseY);
                target.setTranslateX(0);
                target.setTranslateX(0);
            }
        };
    }
    public void createDraggableProperty(){

    }
}
