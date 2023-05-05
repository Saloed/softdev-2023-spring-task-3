package ModelThings;

import org.lwjgl.util.vector.Vector3f;
import tools.Vertex;


public class Face {
    public Vertex vertex;
    public Vector3f normals;
    public Face(Vertex vertex, Vector3f normals){
        this.normals = normals;
        this.vertex = vertex;
    }
    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }
        if (o == null|| o.getClass() != getClass()){
            return false;
        }
        Face f = (Face) o;
        return f.vertex == this.vertex && f.normals == this.normals;
    }

}
