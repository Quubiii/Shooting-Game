package main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Circle extends JPanel implements Runnable {

    private static int totalScore = 0;
    private ScoreUpdateListener scoreUpdateListener;

    public interface ScoreUpdateListener {
        void onScoreUpdate(int newScore);
    }

    public void setScoreUpdateListener(ScoreUpdateListener listener) {
        this.scoreUpdateListener = listener;
    }

    private ArrayList<CircleData> circles = new ArrayList<>();
    static boolean running = true;
    private int xSpeed = 10;
    private int ySpeed = 6;
    private final int PANEL_WIDTH = 800;
    private final int PANEL_HEIGHT = 800;

    public Circle() {
        setDoubleBuffered(true);
        drawCircles();
        Thread animationThread = new Thread(this);
        animationThread.start();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });
    }

    public void drawCircles() {
        circles.clear();
        int minPoints = 2;
        int decrement = 2;

        for (int i = 7; i > 0; i--) {
            int radius = i * 20;
            Color color = (i % 2 == 0) ? Color.WHITE : Color.RED;
            int points = minPoints - (i - 7)*decrement;
            circles.add(new CircleData(400, 400, radius, color, points));
        }
    }

    @Override
    protected synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (CircleData circle : circles) {
            g.setColor(circle.color);
            g.fillOval(circle.x - circle.radius, circle.y - circle.radius, 2 * circle.radius, 2 * circle.radius);
        }
    }

    private synchronized void handleMouseClick(int mouseX, int mouseY) {
        if (!GameLogic.isGameOver()) {
            for (int i = circles.size() - 1; i >= 0; i--) {
                CircleData circle = circles.get(i);
                int dx = mouseX - circle.x;
                int dy = mouseY - circle.y;
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance <= circle.radius) {
                    totalScore += circle.points;
                    if (scoreUpdateListener != null) {
                        scoreUpdateListener.onScoreUpdate(totalScore);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void run() {
        while (running) {
            synchronized (this) {
                CircleData largestCircle = circles.get(0);

                largestCircle.x += xSpeed;
                largestCircle.y += ySpeed;

                if (largestCircle.x - largestCircle.radius <= 0 || largestCircle.x + largestCircle.radius >= PANEL_WIDTH) {
                    xSpeed = -xSpeed;
                }
                if (largestCircle.y - largestCircle.radius <= 0 || largestCircle.y + largestCircle.radius >= PANEL_HEIGHT) {
                    ySpeed = -ySpeed;
                }

                for (int i = 1; i < circles.size(); i++) {
                    CircleData smallerCircle = circles.get(i);
                    smallerCircle.x = largestCircle.x;
                    smallerCircle.y = largestCircle.y;
                }
            }
            repaint();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


    public static synchronized int getTotalScore() {
        return totalScore;
    }

    public static void stopAnimation() {
        running = false;
        Thread.currentThread().interrupt();
    }


    public static void resetGameStates() {
        stopAnimation();
        resetScore();
        running = true;
    }

    public static void resetScore() {
        totalScore = 0;
    }


}
