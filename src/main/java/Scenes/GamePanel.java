package Scenes;

import Inputs.KeyPadInputs;
import Inputs.MouseInputs;

import javax.swing.*;
import java.awt.*;

import GameEngine.Game;

import static GameEngine.Game.GameHeight;
import static GameEngine.Game.GameWidth;

public class GamePanel extends JPanel {
    private MouseInputs mouseInputs;
    private Game game;

    public GamePanel(Game game) {
        KeyPadInputs keyPadInputs = new KeyPadInputs(this);
        mouseInputs = new MouseInputs(this);
        this.game = game;

        setSizeOfPanel();
        addKeyListener(keyPadInputs);
        addMouseMotionListener(mouseInputs);
        addMouseListener(mouseInputs);
    }

    public void setSizeOfPanel() {
        Dimension size = new Dimension(GameWidth, GameHeight);
        setPreferredSize(size);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);
    }

    public Game getGame(){
        return game;
    }
}
