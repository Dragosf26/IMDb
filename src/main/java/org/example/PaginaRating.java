package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaginaRating extends JFrame {
    private Production ratedProduction;
    private Actor ratedActor;
    User user;

    public PaginaRating(Production ratedProduction, Actor ratedActor, User user) {
        this.user = user;
        this.ratedProduction = ratedProduction;
        this.ratedActor = ratedActor;
        initialize();
    }

    private void initialize() {
        setLayout(new GridLayout(4, 2));

        JLabel nameLabel = new JLabel("Nume producție/actor:");
        JTextField nameField = new JTextField();
        nameField.setEditable(false);
        if (ratedProduction != null) {
            nameField.setText(ratedProduction.getTitle());
        } else {
            nameField.setText(ratedActor.getName());
        }

        JLabel ratingLabel = new JLabel("Rating (1-10):");
        JTextField ratingField = new JTextField();

        JLabel commentLabel = new JLabel("Comentariu:");
        JTextField commentField = new JTextField();

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int rating = Integer.parseInt(ratingField.getText());
                    String comment = commentField.getText();
                    String username = user.getUsername();

                    Rating newRating = new Rating(username, rating, comment);
                    if (ratedProduction != null) {
                        ((Regular) user).addRatingProduction(newRating, ratedProduction);
                    } else {
                        ((Regular) user).addRatingActor(newRating, ratedActor);
                    }
                    JOptionPane.showMessageDialog(PaginaRating.this, "Rating adaugat cu succes!");
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(PaginaRating.this, "Introduceți un rating valid (1-10)!");
                }
            }
        });

        add(nameLabel);
        add(nameField);
        add(ratingLabel);
        add(ratingField);
        add(commentLabel);
        add(commentField);
        add(submitButton);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Adaugare Rating");
    }
}