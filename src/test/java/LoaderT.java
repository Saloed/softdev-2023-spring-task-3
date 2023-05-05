
import ModelThings.Face;
import ModelThings.Model;
import ModelThings.OBGLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lwjgl.util.vector.Vector3f;
import tools.VT;
import tools.Vertex;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LoaderT {
    @Test
    public void pars(){
        Path in = Paths.get("input", "cube.obj");
        List<Vertex> vertices = new ArrayList<>();
        vertices.add(new Vertex(1, 1, -1));
        vertices.add(new Vertex(1, -1, -1));
        vertices.add(new Vertex(1, 1, 1));
        vertices.add(new Vertex(1, -1, 1));
        vertices.add(new Vertex(-1, 1, -1));
        vertices.add(new Vertex(-1, -1, -1));
        vertices.add(new Vertex(-1, 1, 1));
        vertices.add(new Vertex(-1, 1, -1));
        List<Vector3f> normals = new ArrayList<>();
        normals.add(new Vector3f(-0, 1, -0));
        normals.add(new Vector3f(-0, 0, 1));
        normals.add(new Vector3f(-1, -0, -0));
        normals.add(new Vector3f(-0, -1, -0));
        normals.add(new Vector3f(1, -0, -0));
        normals.add(new Vector3f(-0, -0, -1));
        normals.add(new Vector3f(1, -0, -0));
        normals.add(new Vector3f(-0, -0, -1));
        List<VT> texture = new ArrayList<VT>();
        texture.add(new VT(0.625000, 0.500000));
        texture.add(new VT( 0.375000, 0.500000));
        texture.add(new VT( 0.625000, 0.750000));
        texture.add(new VT( 0.375000, 0.750000));
        texture.add(new VT(0.875000, 0.500000));
                texture.add(new VT(0.625000, 0.250000));
        texture.add(new VT(0.125000, 0.500000));
        texture.add(new VT(0.375000, 0.250000));
        texture.add(new VT(0.875000, 0.750000));
        texture.add(new VT(0.625000, 1.000000));
        texture.add(new VT(0.625000, 0.000000));
        texture.add(new VT(0.375000, 1.000000));
        texture.add(new VT(0.375000, 0.000000));
        texture.add(new VT(0.125000, 0.750000));
        List<Face> faces = new ArrayList<Face>();
        faces.add(new Face(new Vertex(1.0, 5.0, 7.0), new Vector3f(1, 1, 1)));
        faces.add(new Face(new Vertex(4, 3, 7), new Vector3f(2, 2, 2)));
        faces.add(new Face(new Vertex(8, 7, 5), new Vector3f(3, 3, 3)));
        faces.add(new Face(new Vertex(6, 2, 4), new Vector3f(4, 4, 4)));
        faces.add(new Face(new Vertex(2, 1, 3), new Vector3f(5, 5, 5)));
        faces.add(new Face(new Vertex(6, 5, 1), new Vector3f(6, 6, 6)));
        Model m = new Model();
        try {
            m = OBGLoader.loadModel(new File(in.toString()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<VT> tex = m.textureVer;
        List<Face> face = m.faces;
        Assertions.assertArrayEquals(tex.toArray(), texture.toArray());
        Assertions.assertArrayEquals(face.toArray(), faces.toArray());
    }
}
