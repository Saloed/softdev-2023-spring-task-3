package modelThings;

import com.mokiat.data.front.parser.OBJVertex;
import tools.Vertex;

import java.awt.*;

public class Triangle {
    private Vertex v1;
    private Vertex v2;
    private Vertex v3;
    Color color;
    Triangle(Vertex v1, Vertex v2, Vertex v3, Color color) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.color = color;
    }
    Triangle(OBJVertex v1, OBJVertex v2, OBJVertex v3, Color color) {
        this.v1 = new Vertex(v1);
        this.v2 = new Vertex(v2);
        this.v3 = new Vertex(v3);
        this.color = color;
    }

    public Vertex getV1() {
        return v1;
    }

    public Vertex getV2() {
        return v2;
    }
    public Vertex getV3(){
        return v3;
    }

    public Color getColor() {
        return color;
    }
public static Color getShadow(Color color, double shadowConst){
        //Color shade = new Color(205, 123, 202);
    double redLinear = Math.pow(color.getRed(), 2.4) * shadowConst;
    double greenLinear = Math.pow(color.getGreen(), 2.4) * shadowConst;
    double blueLinear = Math.pow(color.getBlue(), 2.4) * shadowConst;

    int red = (int) Math.pow(redLinear, 1/2.4);
    int green = (int) Math.pow(greenLinear, 1/2.4);
    int blue = (int) Math.pow(blueLinear, 1/2.4);

    return new Color(red, green, blue);
}

}
