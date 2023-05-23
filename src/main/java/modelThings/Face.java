package modelThings;

import tools.Vertex;
import com.mokiat.data.front.parser.OBJNormal;


public class Face {
    public Vertex vertex;
    public  OBJNormal normals;
    public Face(Vertex vertex, OBJNormal normals){
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
    @Override
    public int hashCode() {
        return 31 * vertex.hashCode() + 31 * normals.hashCode();
    }

}
