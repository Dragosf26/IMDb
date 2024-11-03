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

public class PaginaAdaugareFilm extends JFrame {

    private JTextField titluField;
    private JTextField regizoriField;
    private JTextField actoriField;
    private JTextField durataField;
    private JTextField anLansareField;
    private JTextField genuriField;
    private JTextArea povesteArea;

    public PaginaAdaugareFilm(Staff user) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Adaugare Film");

        JLabel titluLabel = new JLabel("Titlu:");
        titluField = new JTextField();

        JLabel regizoriLabel = new JLabel("Regizori (separati cu virgula):");
        regizoriField = new JTextField();

        JLabel actoriLabel = new JLabel("Actori (separati cu virgula):");
        actoriField = new JTextField();

        JLabel genuriLabel = new JLabel("Genuri (separate cu virgula):");
        genuriField = new JTextField();

        JLabel povesteLabel = new JLabel("Poveste:");
        povesteArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(povesteArea);

        JLabel durataLabel = new JLabel("Durata (in minute):");
        durataField = new JTextField();

        JLabel anLansareLabel = new JLabel("Anul lansarii:");
        anLansareField = new JTextField();

        JButton adaugaButton = new JButton("Adauga film");
        adaugaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adaugaProductie(user);
            }
        });

        setLayout(new GridLayout(9, 2, 5, 5));

        add(titluLabel);
        add(titluField);
        add(regizoriLabel);
        add(regizoriField);
        add(actoriLabel);
        add(actoriField);
        add(genuriLabel);
        add(genuriField);
        add(povesteLabel);
        add(scrollPane);
        add(durataLabel);
        add(durataField);
        add(anLansareLabel);
        add(anLansareField);
        add(adaugaButton);

        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void adaugaProductie(Staff user) {
        try {

            String titlu = titluField.getText();
            String regizoriInput = regizoriField.getText();
            List<String> regizori = List.of(regizoriInput.split(","));

            String actoriInput = actoriField.getText();
            List<String> actori = List.of(actoriInput.split(","));

            int durata = Integer.parseInt(durataField.getText());
            int anLansare = Integer.parseInt(anLansareField.getText());

            String genuriInput = genuriField.getText();
            List<Genre> genuri = new ArrayList<>();
            String[] genreNames = genuriInput.split(",");
            for (String genreName : genreNames) {
                try {
                    Genre genre = Genre.valueOf(genreName.trim());
                    genuri.add(genre);
                } catch (IllegalArgumentException ex) {
                    System.out.println("Genul " + genreName.trim() + " nu este valid.");
                }
            }

            String poveste = povesteArea.getText();


            Movie movie = new Movie(durata, anLansare, titlu, regizori, actori, genuri, new ArrayList<>(), poveste, 0);
            user.addProductionSystem(movie);
            movie.addObserverProduction(user);
            user.getProductieActoriAdaugati().add(movie.getTitle());

            CalculateExperienceContex calculateExperienceContex = new CalculateExperienceContex();
            calculateExperienceContex.setExperienceStrategy(new ProductionStrategy(user.getExperienta()));
            user.setExperienta(calculateExperienceContex.calculateExp());

            JOptionPane.showMessageDialog(this, "Productie adaugata cu succes!");
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Introduceti valori numerice valide pentru durata ai anul lansarii!");
        }
    }
}
