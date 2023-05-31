import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import com.mokiat.data.front.parser.OBJVertex;
import modelThings.OBGLoader;
import com.mokiat.data.front.parser.OBJModel;
import modelThings.Triangle;
import tools.Vertex;
import windowTools.Toast;

public class DemoViewer {


    public static void main(String[] args) {
        JFrame frame = menu();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    static JFrame menu() {
        JFrame frame = new JFrame();
        frame.setSize(200, 200);
        frame.setVisible(true);
        JPanel panel = new JPanel();
        JButton button = new JButton("Select file");
        button.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("C:\\Users"));
            chooser.setDialogTitle("Select .obj file");

            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

            chooser.addChoosableFileFilter(new FileNameExtensionFilter("OBJ documents", "obj"));
            chooser.setAcceptAllFileFilterUsed(true);

            if (chooser.showOpenDialog(button) == JFileChooser.APPROVE_OPTION) {
                try {
                    OBJModel model = OBGLoader.loadModel(new File(chooser.getSelectedFile().getAbsolutePath()));
                        View modelView = new View(model);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());

                }
            }
        });
        panel.add(button);

        frame.add(panel);
        return frame;
    }

    static class View extends JFrame {

        static OBJModel model;
        static JPanel renderPanel;

        public View(OBJModel model) {
            setSize(500, 500);
            setVisible(true);
            JLabel label = new JLabel("View model");
            setLocationRelativeTo(null);
            JSlider ZY = new JSlider(0, 180, 0);
            ZY.setPaintTicks(true);
            ZY.setPaintLabels(true);
            add(ZY, BorderLayout.SOUTH);

            JSlider XY = new JSlider(SwingConstants.VERTICAL, 0, 180, 0);
            XY.setPaintLabels(true);
            XY.setPaintTicks(true);
            add(XY, BorderLayout.EAST);
            ZY.addChangeListener(e -> renderPanel.repaint());
            XY.addChangeListener(e -> renderPanel.repaint());




            renderPanel = new JPanel() {

                public void paintComponent(Graphics g) {

                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(Color.BLACK);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    List<List<OBJVertex>> polygons = modelThings.OBGLoader.getPolygons(model);
                    boolean textureOn = true;
                    if (textureOn){
                        BufferedImage img =
                                new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                        double[] zBuffer = new double[img.getWidth() * img.getHeight()];

                        for (int q = 0; q < zBuffer.length; q++) {
                            zBuffer[q] = Double.NEGATIVE_INFINITY;
                        }
                        List<Triangle> triangles = modelThings.OBGLoader.getTriangles(polygons);

                        for (Triangle triangle: triangles){
                            Vertex vertex1 = Vertex.transform(triangle.getV1(), XY.getValue(), 0, ZY.getValue());
                            Vertex vertex2 = Vertex.transform(triangle.getV2(), XY.getValue(), 0, ZY.getValue());
                            Vertex vertex3 = Vertex.transform(triangle.getV3(), XY.getValue(), 0, ZY.getValue());

                            Vertex newVertex1 = new Vertex(vertex1.getX() + getWidth()/2,
                                    vertex1.getY() + getHeight()/2, vertex1.getZ());
                            Vertex newVertex2 = new Vertex(vertex2.getX() + getWidth()/2,
                                    vertex2.getY() + getHeight()/2, vertex2.getZ());
                            Vertex newVertex3 = new Vertex(vertex3.getX() + getWidth()/2,
                                    vertex3.getY() + getHeight()/2, vertex3.getZ());

                            Vertex ab = new Vertex(newVertex2.getX() - newVertex1.getX(),
                                    newVertex2.getY() - newVertex1.getY(), newVertex2.getZ() - newVertex1.getZ());
                            Vertex ac = new Vertex(newVertex3.getX() - newVertex1.getX(),
                                    newVertex3.getY() - newVertex1.getY(), newVertex3.getZ() - newVertex1.getZ());
                            Vertex normal = new Vertex(ab.getY()*ac.getZ() - ab.getZ()*ac.getY(),
                                    ab.getZ()*ac.getX() - ab.getX() *ac.getZ(),
                                    ab.getX()*ac.getY() - ab.getY() * ac.getX());
                            double normalLength = Math.sqrt(normal.getX() * normal.getX() + normal.getY() * normal.getY()
                                    + normal.getZ() * normal.getZ());


                            int minX = (int) Math.max(0, Math.ceil(Math.min(newVertex1.getX(),
                                    Math.min(newVertex2.getX(), newVertex3.getX()))));
                            int maxX = (int) Math.min(img.getWidth() - 1, Math.floor(Math.max(newVertex1.getX(),
                                    Math.max(newVertex2.getX(), newVertex3.getX()))));
                            int minY = (int) Math.max(0, Math.ceil(Math.min(newVertex1.getY(),
                                    Math.min(newVertex2.getY(), newVertex3.getY()))));
                            int maxY = (int) Math.min(img.getHeight() - 1, Math.floor(Math.max(newVertex1.getY(),
                                    Math.max(newVertex2.getY(), newVertex3.getY()))));

                            double triangleArea = (newVertex1.getY() - newVertex3.getY()) * (newVertex2.getX() - newVertex3.getX())
                                    + (newVertex2.getY() - newVertex3.getY()) * (newVertex3.getX() - newVertex1.getX());

                            for (int y = minY; y <= maxY; y++) {
                                for (int x = minX; x <= maxX; x++) {
                                    double b1 = ((y - newVertex3.getY()) * (newVertex2.getX() - newVertex3.getX()) +
                                            (newVertex2.getY() - newVertex3.getY()) * (newVertex3.getX() - x)) / triangleArea;
                                    double b2 = ((y - newVertex1.getY()) * (newVertex3.getX() - newVertex1.getX()) +
                                            (newVertex3.getY() - newVertex1.getY()) * (newVertex1.getX() - x)) / triangleArea;
                                    double b3 = ((y - newVertex2.getY()) * (newVertex1.getX() - newVertex2.getX()) +
                                            (newVertex1.getY() - newVertex2.getY()) * (newVertex2.getX() - x)) / triangleArea;
                                    if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
                                        double depth = b1 * newVertex3.getZ() + b2 * newVertex2.getZ() + b3 * newVertex1.getZ();
                                        int zIndex = y * img.getWidth() + x;
                                        if (zBuffer[zIndex] < depth) {
                                            img.setRGB(x, y,
                                                    modelThings.Triangle.getShadow(triangle.getColor(), normal.getZ()/normalLength).getRGB());
                                            zBuffer[zIndex] = depth;
                                        }
                                    }
                                }
                            }
                        }
                                g2.drawImage(img, 0, 0, null);

                    } else {
                        g2.translate(getWidth() / 2, getHeight() / 2);
                        g2.setColor(Color.WHITE);
                        for (List<OBJVertex> polygon: polygons){
                            Path2D path = new Path2D.Double();
                            for (int i = 0; i < polygon.size(); ++i){
                                Vertex start = tools.Vertex.transformOBJ( polygon.get(0), XY.getValue(), 0, ZY.getValue());
                                path.moveTo(start.getX(), start.getY());
                                if (i > 0){
                                    Vertex actual = tools.Vertex.transformOBJ(polygon.get(i), XY.getValue(), 0, ZY.getValue());
                                    path.lineTo(actual.getX(), actual.getY());
                                }
                            }
                            path.closePath();
                            g2.draw(path);
                        }
                    }
                }
            };
            add(renderPanel);

        }
    }
}
