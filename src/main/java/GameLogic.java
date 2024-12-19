package main.java;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

import static main.java.Main.main;

public class GameLogic {
    private static Timer timer = new Timer();
    private static int score = 0;
    private static boolean isGameOver = false;
    private GameGUI gui;
    private Circle circle;

    public GameLogic(GameGUI gui, Circle circle) {
        this.gui = gui;
        this.circle = circle;
    }

    public void startCountdown(int seconds) {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            int timeLeft = seconds;

            @Override
            public void run() {
                if (timeLeft > 0) {
                    timeLeft--;
                } else {
                    isGameOver = true;
                    timer.cancel();
                    circle.stopAnimation();
                    checkWinCondition();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }


    public void checkWinCondition() {
        int currentScore = Circle.getTotalScore();
        if (currentScore >= 75) {
            GameGUI.winMessage(gui);
        } else {
            GameGUI.lostMessage(gui);
            System.out.println("You lost. Try again!");
        }
        isGameOver = true;
    }

    public static boolean isGameOver() {
        return isGameOver;
    }

    public static int getScore() {
        return score;
    }

    public static void restartGame() {
        SwingUtilities.invokeLater(() -> {
            JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(new JPanel());
            if (currentFrame != null) {
                currentFrame.dispose();
            }

            Circle.resetScore();
            resetGameStates();
            isGameOver = false;

            GameGUI gui = new GameGUI();
            Circle circlePanel = gui.getCirclePanel();
            circlePanel.setScoreUpdateListener(gui::updateScore);
            gui.updateScore(0);

            Thread animationThread = new Thread(circlePanel);
            animationThread.start();

            GameLogic game = new GameLogic(gui, circlePanel);
            GameGUI.welcomeMessage(gui);
            game.startCountdown(10);
        });
    }

    public static void resetGameStates() {
        Circle.resetGameStates();
        stopTimer();
        score = 0;
        isGameOver = false;
    }

    public static void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }
}
