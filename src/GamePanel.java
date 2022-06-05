import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel  implements ActionListener {
    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 800;
    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    //  begin with 6 body parts on the snake
    int bodyParts = 2;
    int appleEaten;
    //  apples positioning
    int appleX;
    int appleY;
    //  begin by going right
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    GamePanel() {
        random = new Random();
        this.setPreferredSize((new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)));
        //  background color
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new KeyPlayAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        // how fast the game is running
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
           /*   draw line across game panel
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            } */
            //  red apples
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // draw head & snake's body
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.cyan);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    g.setColor(new Color(45, 180, 0));
                    //  multiples color of snake's body parts
                    g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            // Game score text
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + appleEaten, SCREEN_WIDTH - metrics.stringWidth("Score: " + appleEaten) - 315, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        //  shifting body parts
        for(int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        //  change direction
        switch (direction) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            appleEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        //  check if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            //  x[0]: head
            //  trigger game over method
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }
        //  check if head touches left border
        if (x[0] < 0) {
            running = false;
        }
        //  check if head touches right border
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        //  check if head touches top border
        if (y[0] < 0) {
            running = false;
        }
        //  check if head touches bottom border
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        //  if !running
        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        // Game score text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + appleEaten, SCREEN_WIDTH - metrics1.stringWidth("Score: " + appleEaten) - 315, g.getFont().getSize());
        // Game over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", SCREEN_WIDTH - metrics2.stringWidth("GAME OVER") - 175, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class KeyPlayAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    // can not change snake's direction to "left" if the snake is going "right"
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    // can not change snake's direction to "right" if the snake is going "left"
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    // can not change snake's direction to "up" if the snake is going "down"
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    // can not change snake's direction to "down" if the snake is going "up"
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
