package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class FirstPage extends JFrame {
    static Regular regularUser;

    JButton b1, b2;
    JLabel label, label2;
    String username, password;

    public FirstPage() {
        super("First page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Font font = new Font("Arial", Font.PLAIN, 20);

        label = new JLabel("Bine ati venit pe IMDB\n");
        label.setFont(font);

        label2 = new JLabel("Cum doresti sa navighezi pe platforma?");
        label2.setFont(font);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        panel.add(label);
        panel.add(label2);

        b1 = new JButton("Terminal");
        b2 = new JButton("Interfata grafica");

        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

                System.out.println("You chose to use terminal\nLets get started!");

                Scanner scanner = new Scanner(System.in);

                System.out.println("Enter your credentials");

                username = scanner.nextLine();
                password = scanner.nextLine();

                verificare(username, password);
            }
        });

        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame guiFrame = new JFrame("Interfata Grafica Frame");
                guiFrame.setSize(300, 200);
                guiFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                guiFrame.setVisible(true);

                dispose();
            }
        });

        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout(1, 2, 10, 5));
        panel2.add(b1);
        panel2.add(b2);

        panel2.setBorder(new EmptyBorder(10, 10, 10, 10)); // Top, left, bottom, right padding de 10 pixeli

        JPanel bigPanel = new JPanel();
        bigPanel.setLayout(new GridLayout(2, 1));
        bigPanel.add(panel);
        bigPanel.add(panel2);
        this.add(bigPanel);

        setVisible(true);
        getContentPane().setBackground(new Color(64, 64, 64));
        setSize(400, 300);
        setLayout(new FlowLayout());
    }

    private void verificare(String username, String password) {
        int count = 0;
        do {
            if (username.equals(regularUser.getCredentials().getUsername())
                    && password.equals(regularUser.getCredentials().getPassword())) {
                System.out.printf("You are now connected as %s!\n", username);
                break;
            } else {
                count++;
                if (count == 3) {
                    System.out.println("You were wrong 3 times");
                    break;
                }

                System.out.println("Your username or password is incorrect!");
                System.out.println("Enter your credentials:");

                Scanner scanner = new Scanner(System.in);

                username = scanner.nextLine();
                password = scanner.nextLine();
            }
        } while (true);
    }
}