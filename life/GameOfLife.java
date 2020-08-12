package life;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class GameOfLife extends JFrame {

    JLabel gen;
    JLabel alive;
    JLabel[][] labels;

    public GameOfLife() {
        super("Game of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(380, 400);
        setLocationRelativeTo(null);
        gen = new JLabel();
        gen.setName("GenerationLabel");
        alive = new JLabel();
        alive.setName("AliveLabel");
        setVisible(true);
    }

    public void init(State state) {
        labels = new JLabel[state.n][state.n];

        setLayout(null);
        gen.setBounds(10, 5, 300, 20);
        alive.setBounds(10, 25, 300, 20);
        add(gen);
        add(alive);

        JPanel body = new JPanel();
        body.setName("body");
        body.setBounds(5, 45, 350, 300);
        add(body);

        Border border = BorderFactory.createLineBorder(Color.black, 1);
        for (int i = 0; i < state.n; i++) {
            for (int j = 0; j < state.n; j++) {
                labels[i][j] = new JLabel(" ");
                labels[i][j].setBorder(border);
                labels[i][j].setOpaque(true);
                labels[i][j].setName("lables" + i + j);
                body.add(labels[i][j]);
            }
        }
        body.setLayout(new GridLayout(state.n, state.n));
    }

    public void display(State state) {
        wait(1000);
        gen.setText("Generation #" + State.gen);
        alive.setText("Alive: " + state.alive);
        for (int i = 0; i < state.n; i++) {
            for (int j = 0; j < state.n; j++) {
                if (state.current[i][j] == 'O') {
                    labels[i][j].setBackground(Color.BLUE);
                } else {
                    labels[i][j].setBackground(Color.WHITE);
                }
            }
        }
    }

    private void wait(int mstime) {
        try {
            Thread.sleep(mstime);
        } catch (InterruptedException e) {
            System.out.println(e.getClass().getName());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        State state = new State(n);
        GameOfLife game = new GameOfLife();
        game.init(state);
        for (int i = 0; i < 11; i++) {
            state.generate();
            game.display(state);
        }
    }
}

class State {
    char[][] current;
    char[][] next;
    Random random;
    int n;
    static int gen = 0;
    int alive;

    State(int n) {
        this.n = n;
        current = new char[n][n];
        next = new char[n][n];
        random = new Random();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                current[i][j] = random.nextBoolean() ? 'O' : ' ';
            }
        }
    }

    void generate() {
        gen++;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int aliveNeighbors = getAliveNeighbors(i, j);
                if (current[i][j] == 'O') {
                    next[i][j] = aliveNeighbors < 2 || aliveNeighbors > 3 ? ' ' : 'O';
                } else {
                    next[i][j] = aliveNeighbors == 3 ? 'O' : ' ';
                }
            }
        }
        alive = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                current[i][j] = next[i][j];
                alive += current[i][j] == 'O' ? 1 : 0;
            }
        }
    }

    void clear() {
        try {
            Thread.sleep(1000);
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getClass().getName());
        }
    }

    void display() {
        clear();
        System.out.println("Generation #" + gen);
        System.out.println("Alive: " + alive);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(current[i][j]);
            }
            System.out.println();
        }
    }

    int getAliveNeighbors(int i, int j) {
        Pos topleft = new Pos(i - 1, j - 1);
        Pos midleft = new Pos(i, j - 1);
        Pos botleft = new Pos(i + 1, j - 1);

        Pos topright = new Pos(i - 1, j + 1);
        Pos midright = new Pos(i, j + 1);
        Pos botright = new Pos(i + 1, j + 1);

        Pos topup = new Pos(i - 1, j);
        Pos botdown = new Pos(i + 1, j);

        int aliveNeighbors = 0;
        aliveNeighbors += getAliveOrDie(topleft);
        aliveNeighbors += getAliveOrDie(midleft);
        aliveNeighbors += getAliveOrDie(botleft);
        aliveNeighbors += getAliveOrDie(topright);
        aliveNeighbors += getAliveOrDie(midright);
        aliveNeighbors += getAliveOrDie(botright);
        aliveNeighbors += getAliveOrDie(topup);
        aliveNeighbors += getAliveOrDie(botdown);

        return aliveNeighbors;
    }

    int getAliveOrDie(Pos pos) {
        int i = pos.i;
        int j = pos.j;
        if (i == -1) {
            i = n - 1;
        }
        if (i == n) {
            i = 0;
        }
        if (j == -1) {
            j = n - 1;
        }
        if (j == n) {
            j = 0;
        }
        return current[i][j] == 'O' ? 1 : 0;
    }

}

class Pos {
    int i;
    int j;

    Pos(int i, int j) {
        this.i = i;
        this.j = j;
    }
}

