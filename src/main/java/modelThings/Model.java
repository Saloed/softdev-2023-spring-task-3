package modelThings;

import com.mokiat.data.front.parser.OBJMesh;
import com.mokiat.data.front.parser.OBJNormal;
import tools.VT;
import tools.Vertex;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.Display.getHeight;
import static org.lwjgl.opengl.Display.getWidth;

public class Model {
    public List<Vertex> vertices = new ArrayList<>();
    public List<OBJNormal> normals = new ArrayList<>();
    public List<Face> faces = new ArrayList<>();
    public List<VT> textureVer = new ArrayList<>();
    public List<OBJMesh> materials = new ArrayList<>();
    public Model(){}
    public Model(List<Vertex> vertices, List<OBJNormal> normals, List<Face> faces, List<VT> textureVer,
                 List<OBJMesh> materials){
        this.faces = faces;
        this.normals = normals;
        this.vertices = vertices;
        this.textureVer = textureVer;
        this.materials = materials;

    }
    public Image projection(Model model){
        BufferedImage img =
                new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        List<Vertex> vertices = model.vertices;

        return img;
    }
}
