package org.example;

import org.example.Enum.Genre;
import org.example.StrategyPattern.CalculateExperienceContex;
import org.example.StrategyPattern.ProductionStrategy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaginaAdaugareSerial extends JFrame {

    private JTextField titluField;
    private JTextField regizoriField;
    private JTextField actoriField;
    private JTextField numSeasonsField;
    private JTextField anLansareField;
    private JTextField genuriField;
    private JTextArea povesteArea;

    public PaginaAdaugareSerial(Staff user) {
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

        JLabel anLansareLabel = new JLabel("Anul lansarii:");
        anLansareField = new JTextField();

        JLabel numSeasonsLabel = new JLabel("Cate sezoane sunt?");
        numSeasonsField = new JTextField();

        JButton adaugaButton = new JButton("Adauga film");
        adaugaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adaugaProductie(user);
            }
        });

        setLayout(new GridLayout(8, 2, 5, 5));

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
        add(numSeasonsLabel);
        add(numSeasonsField);
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

            int numSeasons = Integer.parseInt(numSeasonsField.getText());
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

            Map<String, List<Episode>> seasons = new HashMap<>();

            for (int i = 1; i <= numSeasons; i++) {
                int numEpisodes = Integer.parseInt(JOptionPane.showInputDialog("Introduceti numarul de episoade pentru sezonul " + i + ":"));

                List<Episode> episodeList = new ArrayList<>();

                for (int j = 1; j <= numEpisodes; j++) {
                    String episodeTitle = JOptionPane.showInputDialog("Introduceti titlul episodului " + j + " din sezonul " + i + ":");
                    int episodeDuration = Integer.parseInt(JOptionPane.showInputDialog("Introduceti durata episodului " + j + " din sezonul " + i + " (in minute):"));

                    Episode episode = new Episode(episodeTitle, episodeDuration);
                    episodeList.add(episode);
                }

                seasons.put("Sezonul " + i, episodeList);
            }

            Series series = new Series(titlu, regizori, actori, genuri, new ArrayList<>(), poveste, 0, anLansare, numSeasons, seasons);
            user.addProductionSystem(series);
            series.addObserverProduction(user);
            user.getProductieActoriAdaugati().add(series.getTitle());

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
