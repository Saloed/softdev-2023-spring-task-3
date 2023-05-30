package org.main;

import org.inputs.KeyboardListener;
import org.inputs.MyMouseListener;

import javax.swing.*;
import java.awt.*;
public class GameScreen extends JPanel { //JPanel, в котором будут рисоваться все состояния игры

    private MyMouseListener myMouseListener;
    private KeyboardListener keyboardListener;

    private Dimension size;

    private Game game;

    public GameScreen(Game game) {
        this.game = game;

        setPanelSize();

    }

    public void initInputs() {
        myMouseListener = new MyMouseListener(game);
        keyboardListener = new KeyboardListener(game);

        addMouseListener(myMouseListener);
        addMouseMotionListener(myMouseListener);
        addKeyListener(keyboardListener);

        requestFocusInWindow();
    }

    private void setPanelSize() {
        size = new Dimension(640, 800);
        setMinimumSize(size);
        setMaximumSize(size);
        setPreferredSize(size);
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        game.getRender().render(g);
    }
}

