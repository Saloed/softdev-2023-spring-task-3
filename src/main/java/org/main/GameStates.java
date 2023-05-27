package org.main;

public enum GameStates {
//Перечисление всех состояний программы
    PLAYING, MENU, EDIT, GAME_OVER, GAME_WIN;

    public static GameStates gameState = MENU;

    public static void SetGameState(GameStates state) {
        gameState = state;
    }

}
