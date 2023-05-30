import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mokiat.data.front.parser.OBJTexCoord;
import com.mokiat.data.front.parser.OBJVertex;
import modelThings.OBGLoader;
import com.mokiat.data.front.parser.OBJModel;
import tools.Vertex;

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
            if (chooser.showOpenDialog(button) == JFileChooser.APPROVE_OPTION) {
                try {
                    OBJModel model = OBGLoader.loadModel(new File(chooser.getSelectedFile().getAbsolutePath()));
                    if (!model.getVertices().isEmpty()) {
                        View modelView = new View(model);
                    }
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
        static  boolean textureOn;

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
                    g2.translate(getWidth() / 2, getHeight() / 2);
                    g2.setColor(Color.WHITE);
                    List<List<OBJVertex>> polygons = modelThings.OBGLoader.getPolygons(model);
                    if (textureOn){
                        //List<List<OBJTexCoord>> textureCoords = modelThings.OBGLoader.getTextureCoords(model);
                        BufferedImage img =
                                new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                        double[] zBuffer = new double[img.getWidth() * img.getHeight()];

                        for (int q = 0; q < zBuffer.length; q++) {
                            zBuffer[q] = Double.NEGATIVE_INFINITY;
                        }

                        for (List<OBJVertex> polygon: polygons) {
                            List<Vertex> actualPolygons = new ArrayList<>();
                            List<Double> Xcoords = new ArrayList<>();
                            List<Double> Ycoords = new ArrayList<>();
                            double minY = 0;
                            double minX = 0;
                            double maxX = img.getWidth() - 1;
                            double maxY = img.getHeight() - 1;

                            for (int i = 0; i < polygon.size(); ++i) {
                                Vertex ac = tools.Vertex.transformOBJ( polygon.get(0), XY.getValue(), 0, ZY.getValue());
                                Vertex actual = new Vertex(ac.getX() + getWidth()/2, ac.getY()+getHeight()/2,
                                        ac.getZ());

                                actualPolygons.add(actual);

                                Xcoords.add(actual.getX());
                                Ycoords.add(actual.getY());

                                minX = Math.max(minX, actual.getX());
                                minY = Math.max(minY, actual.getY());
                                maxX = Math.min(maxX, actual.getX());
                                maxY = Math.min(maxY, actual.getY());}

                            double space = 0;
                            if (polygon.size() == 3){
                                double Ver1y = actualPolygons.get(0).getY();
                                double Ver1x = actualPolygons.get(0).getX();
                                double Ver2y = actualPolygons.get(1).getY();
                                double Ver2x = actualPolygons.get(1).getX();
                                double Ver3y = actualPolygons.get(2).getY();
                                double Ver3x = actualPolygons.get(2).getX();
                                space = (Ver1y - Ver3y) * (Ver2x - Ver3x) +
                                        (Ver2y - Ver3y) *(Ver3x - Ver1x);
                                for (int y = (int) minY; y <= maxY; ++y){
                                    for (int x =(int) minX; x <= maxX; ++x){
                                        double b1 = ((y - Ver3y) * (Ver2x - Ver3x) + (Ver2y - Ver3y) * (Ver3x - x)) / space;
                                        double b2 = ((y - Ver1y) * (Ver3x - Ver1x) + (Ver3y - Ver1y) * (Ver1x - x)) / space;
                                        double b3 = ((y - Ver2y) * (Ver1x - Ver2x) + (Ver1y - Ver2y) * (Ver2x - x)) / space;
                                        double depth = b1 * actualPolygons.get(0).getZ() +
                                                b2 * actualPolygons.get(1).getZ() + b3 * actualPolygons.get(2).getZ();
                                        int zIndex = y * img.getWidth() + x;
                                        if (zBuffer[zIndex] < depth) {
                                            img.setRGB(x, y, Color.RED.getRGB());
                                            zBuffer[zIndex] = depth;
                                        }
                                    }
                                }
                            }
                        }

                        g2.drawImage(img, 0, 0, null);
                    } else {
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