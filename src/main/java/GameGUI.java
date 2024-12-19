package main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameGUI extends JFrame {

    private JLabel scoreLabel;
    private int currentScore = 0;

    public GameGUI() {
        setTitle("Shooting Game");
        setSize(800, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        Circle circlePanel = new Circle();
        add(circlePanel, BorderLayout.CENTER);

        JPanel scorePanel = new JPanel();
        scorePanel.setPreferredSize(new Dimension(800, 30));
        scorePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        scoreLabel = new JLabel("Score: 0");
        scorePanel.add(scoreLabel);

        add(scorePanel, BorderLayout.SOUTH);

        circlePanel.setScoreUpdateListener(this::updateScore);

        setVisible(true);
    }

    public static void welcomeMessage(JFrame frame) {
        String message = "Welcome to the Shooting Game!\nYou need to get 75 points in 10 seconds to win.\nEach circle gives you different amount of points so be agile and have fun!";
        JOptionPane.showMessageDialog(frame, message, "The Shooting Game", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void lostMessage(JFrame frame) {
        SwingUtilities.invokeLater(() -> {
            String message = "Game Over!\nYou scored " + Circle.getTotalScore() + " out of 75.";
            Object[] options = {"Play Again", "Exit"};
            int choice = JOptionPane.showOptionDialog(
                    frame, message, "The Shooting Game",
                    JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE,
                    null, options, options[0]
            );

            frame.dispose();

            if (choice == JOptionPane.YES_OPTION) {
                GameLogic.restartGame();
            } else {
                System.exit(0);
            }
        });
    }

    public static void winMessage(JFrame frame) {
        SwingUtilities.invokeLater(() -> {
            String message = "Congratulations, You won!\nYou scored " + Circle.getTotalScore() + " out of 75.";
            Object[] options = {"Play Again", "Exit"};
            int choice = JOptionPane.showOptionDialog(
                    frame, message, "The Shooting Game",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[0]
            );

            frame.dispose();

            if (choice == JOptionPane.YES_OPTION) {
                GameLogic.restartGame();
            } else {
                System.exit(0);
            }
        });
    }

    public Circle getCirclePanel() {
        for (Component component : getContentPane().getComponents()) {
            if (component instanceof Circle) {
                return (Circle) component;
            }
        }
        return null;
    }

    void updateScore(int newScore) {
        currentScore = newScore;
        scoreLabel.setText("Score: " + currentScore);
    }
}
