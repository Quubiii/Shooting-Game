package main.java;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        GameLogic.resetGameStates();
        GameGUI gui = new GameGUI();
        Circle circlePanel = gui.getCirclePanel();
        GameLogic game = new GameLogic(gui, circlePanel);
        GameGUI.welcomeMessage(gui);

        game.startCountdown(10);
    }

}
