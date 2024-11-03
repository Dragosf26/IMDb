package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaginaAdaugareProductieActor extends JFrame {
    private JComboBox<String> tipProductieComboBox;

    public PaginaAdaugareProductieActor(Staff user) {
        initialize(user);
    }

    private void initialize(Staff user) {
        setLayout(new GridLayout(10, 2));

        JLabel tipLabel = new JLabel("Ce adaugi:");
        String[] tipuri = {"Film", "Serial", "Actor"};
        tipProductieComboBox = new JComboBox<>(tipuri);

        JButton adaugaButton = new JButton("Adauga productie/actor");
        adaugaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String alegere = (String) tipProductieComboBox.getSelectedItem();
                if (alegere.equals("Film")) {
                    PaginaAdaugareFilm paginaAdaugareFilm = new PaginaAdaugareFilm(user);
                    paginaAdaugareFilm.setVisible(true);

                    dispose();
                } else if (alegere.equals("Serial")) {
                    PaginaAdaugareSerial paginaAdaugareSerial = new PaginaAdaugareSerial(user);
                    paginaAdaugareSerial.setVisible(true);

                    dispose();
                } else if (alegere.equals("Actor")) {
                    PaginaAdaugareActor paginaAdaugareActor = new PaginaAdaugareActor(user);
                    paginaAdaugareActor.setVisible(true);

                    dispose();
                }
            }
        });

        add(tipLabel);
        add(tipProductieComboBox);

        add(adaugaButton);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Adauga Productie");
    }

}
