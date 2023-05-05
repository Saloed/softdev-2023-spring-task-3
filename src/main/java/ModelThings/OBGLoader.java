package ModelThings;

import org.lwjgl.util.vector.Vector3f;
import tools.Vertex;
import tools.VT;

import java.io.*;

public class OBGLoader {
    public static Model loadModel(File f) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        Model m = new Model();
        String line;
        while ((line = reader.readLine()) != null){
            if(line.startsWith("v ")){
                double x = Double.parseDouble(line.split(" ")[1]);
                double y = Double.parseDouble(line.split(" ")[2]);
                double z = Double.parseDouble(line.split(" ")[3]);
                m.vertices.add(new Vertex(x, y, z));
            } else if(line.startsWith("vn ")){
                float x = Float.parseFloat(line.split(" ")[1]);
                float y = Float.parseFloat(line.split(" ")[2]);
                float z = Float.parseFloat(line.split(" ")[3]);
                m.normals.add(new Vector3f(x, y, z));
            } else if (line.startsWith("f ")){
                Vertex vertexInd = new Vertex(Double.parseDouble(line.split(" ")[1]
                        .split("/")[0]), Double.parseDouble(line.split(" ")[2]
                        .split("/")[0]), Double.parseDouble(line.split(" ")[3]
                        .split("/")[0]));
                Vector3f normalInd = new Vector3f(Float.parseFloat(line.split(" ")[1]
                        .split("/")[2]), Float.parseFloat(line.split(" ")[2]
                        .split("/")[2]), Float.parseFloat(line.split(" ")[3]
                        .split("/")[2]));
                m.faces.add(new Face(vertexInd, normalInd));
            } else if (line.startsWith("vt ")){
                double u = Double.parseDouble(line.split(" ")[1]);
                double v = Double.parseDouble(line.split(" ")[2]);
                m.textureVer.add(new VT(u, v));
            }
        }
        return m;
    }
}
