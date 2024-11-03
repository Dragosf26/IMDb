package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaginaCautareProductieActor extends JFrame {
    IMDB imdb = IMDB.getInstance();

    public PaginaCautareProductieActor(JFrame paginaAnterioara) {
        super("Pagina cautare productie/actor");

        setSize(600,400);

        JPanel mainPanel = new JPanel();

        mainPanel.add(createSearchPanel());

        JPanel northWestPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        setLocationRelativeTo(null);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (paginaAnterioara != null) {
                    paginaAnterioara.setVisible(true);
                }

                dispose();
            }
        });
        northWestPanel.add(backButton);
        add(northWestPanel, BorderLayout.NORTH);

        add(mainPanel, BorderLayout.CENTER);
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
                        JOptionPane.showMessageDialog(PaginaCautareProductieActor.this, "Niciun rezultat gasit pentru: " + searchTerm);
                    }
                }
            }
        });

        panel.add(searchField);
        panel.add(searchButton);

        return panel;
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
    private void showDetailsProduction(Production production) {
        JOptionPane.showMessageDialog(this, "Detalii despre " + production.displayInfoGUI());
    }

    private void showDetailsActor(Actor actor) {
        JOptionPane.showMessageDialog(this, "Detalii despre " + actor.displayInfoGUI());
    }
}
