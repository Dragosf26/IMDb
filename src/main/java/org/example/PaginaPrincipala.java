package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;

public class PaginaPrincipala extends JFrame {
    IMDB imdb = IMDB.getInstance();

    public PaginaPrincipala(User userAutentificat) {
        super("Pagina principala");

        setSize(600, 400);

        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        setLocationRelativeTo(null);

        mainPanel.add(createRecommendationsPanel());

        mainPanel.add(createSearchPanel());

        mainPanel.add(createActorsPanel());

        mainPanel.add(createMeniuPanel(userAutentificat));

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createMeniuPanel(User user) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Meniu"));

        JButton button = new JButton("Meniu");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (user instanceof Regular) {
                    PaginaMeniuRegular paginaMeniu = new PaginaMeniuRegular(user);
                } else if (user instanceof Contributor){
                    PaginaMeniuContributor paginaMeniuContributor = new PaginaMeniuContributor(user);
                } else if (user instanceof Admin) {
                    PaginaMeniuAdmin paginaMeniuAdmin = new PaginaMeniuAdmin(user);
                }
                dispose();
            }
        });
        panel.add(button);

        return panel;
    }

    private JPanel createRecommendationsPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Recomandari"));

        for (int i = 0; i < 3; i++) {
            Production production = getRandomProduction();
            JButton button = new JButton(production.getTitle());
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showDetailsProduction(production);
                }
            });
            panel.add(button);
        }

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Cautare"));

        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Cauta");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText();
                Production foundProduction = searchProduction(searchTerm);
                if (foundProduction != null) {
                    showDetailsProduction(foundProduction);
                } else {
                    Actor foundActor = searchActor(searchTerm);
                    if (foundActor != null) {
                        showDetailsActor(foundActor);
                    } else {
                        JOptionPane.showMessageDialog(PaginaPrincipala.this, "Niciun rezultat gasit pentru: " + searchTerm);
                    }
                }
            }
        });

        panel.add(searchField);
        panel.add(searchButton);

        return panel;
    }

    private JPanel createActorsPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Navigare la Actor"));

        JButton actorsButton = new JButton("Actorii din Lista");
        actorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showActorsList();
            }
        });

        panel.add(actorsButton);

        return panel;
    }

    private void showDetailsProduction(Production production) {
        JOptionPane.showMessageDialog(this, "Detalii despre " + production.getTitle() +
                "\nRegizor: " + production.getDirectors() +
                "\nGen: " + production.getGenres());
    }

    private void showDetailsActor(Actor actor) {
        JOptionPane.showMessageDialog(this, "Detalii despre " + actor.getName() +
                "\nPerformante: " + actor.getPerformances() +
                "\nBiografie: " + actor.getBiography());
    }

    private void showActorsList() {
        ListaActorilorFrame listaActorilorFrame = new ListaActorilorFrame(imdb.actors);
        listaActorilorFrame.setVisible(true);
    }

    private Production getRandomProduction() {
        Random random = new Random();
        return imdb.productions.get(random.nextInt(imdb.productions.size()));
    }

    private Production searchProduction(String searchTerm) {
        for (Production production : imdb.productions) {
            if (production.getTitle().equalsIgnoreCase(searchTerm)) {
                return production;
            }
        }
        return null;
    }

    private Actor searchActor(String searchTerm) {
        for (Actor actor : imdb.actors) {
            if (actor.getName().equalsIgnoreCase(searchTerm)) {
                return actor;
            }
        }
        return null;
    }
}