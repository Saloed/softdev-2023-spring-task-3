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
}
