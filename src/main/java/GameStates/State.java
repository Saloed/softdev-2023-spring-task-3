package GameStates;

import GameEngine.Game;
import UI.MenuButtons;

import java.awt.event.MouseEvent;

public class State {
    protected Game game;

    public State (Game game){
        this.game = game;
    }

    public boolean IsInButton(MouseEvent e, MenuButtons menuButton) {
        return menuButton.getBorders().contains(e.getX(), e.getY());
    }

    public Game getGame(){
        return game;
    }

}
