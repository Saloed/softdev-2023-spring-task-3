package Scenes;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameWindow {

    public GameWindow(GamePanel gamePanel) {
        JFrame jFrame = new JFrame();
        ImageIcon image = new ImageIcon("src/main/resources/images/gold.png");
        jFrame.setIconImage(image.getImage());

        jFrame.add(gamePanel);
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {

            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.getGame().lostFocus();
            }
        });
    }
}
