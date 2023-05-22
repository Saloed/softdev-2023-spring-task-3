package UI;

import javax.swing.*;


public class Window extends JFrame {

        private  Screen screen;

        public Window() {
            super("Google Dino game");
            setSize(600, 175);
            setLocation(400, 200);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            screen = new Screen();
            add(screen);
            addKeyListener(screen);
        }

        public void start() {
            screen.start();
        }

        public static void main(String[] args) {
            Window w = new Window();
            w.setVisible(true);
            w.start();
        }
}
