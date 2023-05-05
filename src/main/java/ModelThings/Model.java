package ModelThings;

import org.lwjgl.util.vector.Vector3f;
import tools.VT;
import tools.Vertex;

import java.util.ArrayList;
import java.util.List;

public class Model {
    public List<Vertex> vertices = new ArrayList<Vertex>();
    public List<Vector3f> normals = new ArrayList<Vector3f>();
    public List<Face> faces = new ArrayList<Face>();
    public List<VT> textureVer = new ArrayList<VT>();

    public Model(){};
    public Model(List<Vertex> vertices, List<Vector3f> normals, List<Face> faces, List<VT> textureVer){
        this.faces = faces;
        this.normals = normals;
        this.vertices = vertices;
        this.textureVer = textureVer;

    }

}
