package org.example;

import org.example.Enum.Genre;
import org.example.StrategyPattern.CalculateExperienceContex;
import org.example.StrategyPattern.ProductionStrategy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PaginaAdaugareActor extends JFrame {

    private JTextField nameField;
    private JTextField biographyField;
    private JTextField performancesField;


    public PaginaAdaugareActor(Staff user) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Adaugare Film");

        JLabel nameLabel = new JLabel("Nume:");
        nameField = new JTextField();

        JLabel biographyLabel = new JLabel("Biografie:");
        biographyField = new JTextField();

        JLabel performancesLabel = new JLabel("Numar Sezoane:");
        performancesField = new JTextField();


        JButton adaugaButton = new JButton("Adauga actor");
        adaugaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adaugaActor(user);
            }
        });

        setLayout(new GridLayout(9, 2, 5, 5));

        add(nameLabel);
        add(nameField);
        add(biographyLabel);
        add(biographyField);
        add(performancesLabel);
        add(performancesField);
        add(adaugaButton);

        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void adaugaActor(Staff user) {
        try {

            String nume = nameField.getText();
            String biography = biographyField.getText();
            int numSeasons = Integer.parseInt(performancesField.getText());

            List<Map<String, String>> performances = new ArrayList<>();
            for (int i = 0; i < numSeasons; i++) {
                String title = JOptionPane.showInputDialog("Introduceti titlul productiei");
                String type = JOptionPane.showInputDialog("Introduceti tipul productiei");
                performances.add(Map.of("title", title, "type", type));
            }

            Actor actor = new Actor(nume, performances, biography);
            user.addActorSystem(actor);
            actor.addObserverProduction(user);
            user.getProductieActoriAdaugati().add(actor.getName());


            CalculateExperienceContex calculateExperienceContex = new CalculateExperienceContex();
            calculateExperienceContex.setExperienceStrategy(new ProductionStrategy(user.getExperienta()));
            user.setExperienta(calculateExperienceContex.calculateExp());

            JOptionPane.showMessageDialog(this, "Productie adaugata cu succes!");
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Introduceti valori numerice valide pentru durata ti anul lansarii!");
        }
    }
}