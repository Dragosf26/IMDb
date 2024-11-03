package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaginaGestionareProductii extends JFrame {

    public PaginaGestionareProductii(Staff user) {
        initialize(user);
    }

    private void initialize(Staff user) {
        setLayout(new GridLayout(3, 1));

        JButton adaugareButton = new JButton("Adaugare Productie");
        adaugareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deschideAdaugareProductieFrame(user);
            }
        });

        JButton stergereButton = new JButton("Stergere Productie");
        stergereButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deschideStergereProductieFrame(user);
            }
        });

        add(adaugareButton);
        add(stergereButton);
        add(new JLabel());

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Gestionare Productii");
    }

    private void deschideAdaugareProductieFrame(Staff user) {
        PaginaAdaugareProductieActor paginaAdaugareProductieActor = new PaginaAdaugareProductieActor(user);
        paginaAdaugareProductieActor.setVisible(true);
    }

    private void deschideStergereProductieFrame(Staff user) {
        PaginaStergereProductie paginaStergereProductie = new PaginaStergereProductie(user);
        paginaStergereProductie.setVisible(true);
    }
}