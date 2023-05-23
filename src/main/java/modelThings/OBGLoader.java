package modelThings;

import com.mokiat.data.front.parser.IOBJParser;
import com.mokiat.data.front.parser.OBJModel;
import com.mokiat.data.front.parser.OBJParser;


import java.io.*;

public class OBGLoader {
    public static OBJModel loadModel(File f) throws IOException {
        try (InputStream in = new FileInputStream(f)) {
            final IOBJParser parser = new OBJParser();
            final OBJModel model = parser.parse(in);
            return model;
        }
    }
}
