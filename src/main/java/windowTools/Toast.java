package windowTools;

import javax.swing.*;
import java.awt.*;

public class Toast extends JFrame {
    String message;
    JWindow window;

    public Toast(String message, int x, int y){
        window = new JWindow();
        window.setBackground(new Color(205, 205, 205, 0));

        JPanel panel = new JPanel(){
            public void paintComponent(Graphics2D g){
                int width = g.getFontMetrics().stringWidth(message);
                int height = g.getFontMetrics().getHeight();

                g.setColor(new Color(228, 228, 228));
                g.fillRect(10, 10, width + 30, height + 10);
                g.setColor(Color.black);
                g.drawRect(10, 10, width + 30, height + 10);

                g.setColor(Color.BLACK);
                g.drawString(message, 100, 100);

            }
        };
        add(panel);

        window.add(panel);
        window.setLocation(x, y);
        window.setSize(300, 100);
    }

    public void showToast(){
        window.setOpacity(1);
        window.setVisible(true);

        try {
            Thread.sleep(2000);
            for (double d = 1.0; d > 0.2; d -= 0.1) {
                Thread.sleep(100);
                window.setOpacity((float)d);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
