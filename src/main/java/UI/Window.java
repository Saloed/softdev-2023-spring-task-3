package UI;

import javax.swing.*;


public class Window extends JFrame {

    private final Screen screen;
    private static final int FRAME_TIME = 16;

        public Window() {
            super("Google Dino game");
            setSize(600, 175);
            setLocation(400, 200);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            screen = new Screen();
            add(screen);
            addKeyListener(screen);
        }

        public static void main(String[] args) {
            Window w = new Window();
            w.setVisible(true);
            w.startGameLoop();
        }

    public void startGameLoop() {
        Thread gameThread = new Thread(() -> {
            long frameStartTime;
            long frameEndTime;
            long sleepTime;

            while (true) {
                frameStartTime = System.currentTimeMillis();

                screen.update();
                screen.repaint();

                frameEndTime = System.currentTimeMillis();
                sleepTime = FRAME_TIME - (frameEndTime - frameStartTime);
                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        gameThread.start();
    }
}
