package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaginaStergereProductie extends JFrame {
    IMDB imdb = IMDB.getInstance();
    private JComboBox<String> produciiComboBox;

    public PaginaStergereProductie(Staff user) {
        initialize(user);
    }

    private void initialize(Staff user) {
        setLayout(new GridLayout(3, 2));

        JLabel selectLabel = new JLabel("Selectati productia:");
        produciiComboBox = new JComboBox<>();

        for (Production production : imdb.productions) {
            produciiComboBox.addItem(production.getTitle());
        }

        JButton stergeButton = new JButton("Sterge Productie");
        stergeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stergeProductie(user);
            }
        });

        add(selectLabel);
        add(produciiComboBox);
        add(new JLabel());
        add(new JLabel());
        add(stergeButton);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Sterge Productie");
    }

    private void stergeProductie(Staff user) {
        String titluSelectat = (String) produciiComboBox.getSelectedItem();

        if (titluSelectat != null) {
            for (Production production : imdb.productions) {
                if (titluSelectat.equals(production.getTitle())) {
                    user.removeProductionSystem(production.getTitle());
                    JOptionPane.showMessageDialog(this, "Productie atearsa cu succes!");
                    dispose();
                    return;
                }
            }
        }

        JOptionPane.showMessageDialog(this, "Selectati o productie valida!");
    }
}
