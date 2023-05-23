package lightThings;

import java.awt.*;

public class Light {
    double lightness;
    Color color;
    //double radius;

    double y; double x;

    public Light(){}
    public Light(double lightness, Color color, double y, double x){
        this.x = x;
        this.y = y;
        this.lightness = lightness;
        this.color = color;
    }

    

}

