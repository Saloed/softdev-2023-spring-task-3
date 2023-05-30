package modelThings;

import com.mokiat.data.front.parser.*;
import tools.Vertex;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OBGLoader {
    public static OBJModel loadModel(File f) throws IOException {
        try (InputStream in = new FileInputStream(f)) {
            final IOBJParser parser = new OBJParser();
            final OBJModel model = parser.parse(in);
            return model;
        }
    }

     static class triangle{
        Vertex a;
        Vertex b;
        Vertex c;
        triangle(Vertex a, Vertex b, Vertex c){
            this.a = a;
            this.b = b;
            this.c =c;
        }
    }
    public static List<List<OBJVertex>> getPolygons(OBJModel model){
        List<List<OBJVertex>> res = new ArrayList<>();
        for (OBJObject object : model.getObjects()) {
            for (OBJMesh mesh : object.getMeshes()) {
                for (OBJFace face : mesh.getFaces()) {
                    List<OBJVertex> actFace = new ArrayList<>();
                    for (OBJDataReference reference: face.getReferences()){
                        OBJVertex vertex = model.getVertex(reference);
                        actFace.add(vertex);
                    }
                    res.add(actFace);
                }
            }
        }
        return res;
    }
    public static  List<List<OBJTexCoord>> getTextureCoords(OBJModel model){
        List<List<OBJTexCoord>> res = new ArrayList<>();
        for (OBJObject object : model.getObjects()) {
            for (OBJMesh mesh : object.getMeshes()) {
                for (OBJFace face : mesh.getFaces()) {
                    List<OBJTexCoord> actFace = new ArrayList<>();
                    for (OBJDataReference reference: face.getReferences()){
                        OBJTexCoord vertex = model.getTexCoord(reference);
                        actFace.add(vertex);
                    }
                    res.add(actFace);
                }
            }
        }
        return res;

    }
    public static List<OBJVertex> transformPolygons(List<OBJVertex> input, double a, double b, double c){
        List<OBJVertex> res = new ArrayList<>();
        for (OBJVertex vertex: input) {
                Vertex ver = new Vertex(vertex);
                Vertex newVer = tools.Vertex.transform(ver, a, b, c);
                res.add(newVer);
        }
        return res;
    }
}
