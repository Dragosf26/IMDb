package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaginaMeniuRegular extends JFrame {
    private User user;
    IMDB imdb = IMDB.getInstance();

    public PaginaMeniuRegular(User user) {
        this.user = user;
        setTitle("Pagina de Meniu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new GridLayout(8, 1));
        setLocationRelativeTo(null);


        JButton btnOptiune1 = new JButton("Vizualizarea detaliilor tuturor productiilor din sistem");
        btnOptiune1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
                for (Production production : imdb.productions) {
                    mainFrame.adaugaDetalii(production.displayInfoGUI());
                }
            }
        });
        add(btnOptiune1);

        JButton btnOptiune2 = new JButton("Vizualizarea detaliilor tuturor actorilor din sistem");
        btnOptiune2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
                for (Actor actor : imdb.actors) {
                    mainFrame.adaugaDetalii(actor.displayInfoGUI());
                }
            }
        });
        add(btnOptiune2);

        JButton btnOptiune3 = new JButton("Vizualizarea notificarilor primite");
        btnOptiune3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                user.displayInfoGUI(PaginaMeniuRegular.this);
            }
        });
        add(btnOptiune3);

        JButton btnOptiune4 = new JButton("Cautarea unui anumit film/serial/actor");
        btnOptiune4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PaginaCautareProductieActor paginaCautareProductieActor = new PaginaCautareProductieActor(PaginaMeniuRegular.this);
                paginaCautareProductieActor.setVisible(true);

                dispose();
            }
        });
        add(btnOptiune4);

        JButton btnOptiune5 = new JButton("Adaugarea/Stergerea unei productii/actor din lista de favorite");
        btnOptiune5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PaginaAdaugareStergereFav paginaAdaugareStergereFav = new PaginaAdaugareStergereFav(user, PaginaMeniuRegular.this);
                paginaAdaugareStergereFav.setVisible(true);

                dispose();
            }
        });
        add(btnOptiune5);

        JButton btnOptiune6 = new JButton("Crearea/Retragerea unei cereri");
        btnOptiune6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PaginaRequest paginaRequest = new PaginaRequest(user, PaginaMeniuRegular.this);
                paginaRequest.setVisible(true);

                dispose();
            }
        });
        add(btnOptiune6);

        JButton btnOptiune7 = new JButton("Adaugarea/Stergerea unei recenzii pentru o productie/actor");
        btnOptiune7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String productie = JOptionPane.showInputDialog("Alege o producție pentru a-i adauga un rating:");

                if (productie != null && !productie.isEmpty()) {
                    Production ratedProduction = findProductionByName(productie);
                    if (ratedProduction != null) {
                        PaginaRating ratingFrame = new PaginaRating(ratedProduction, null,user);
                        ratingFrame.setVisible(true);
                    } else {
                        Actor ratedActor = findActorByName(productie);
                        if (ratedActor != null) {
                            PaginaRating ratingFrame = new PaginaRating(null, ratedActor,user);
                            ratingFrame.setVisible(true);
                        } else {
                        JOptionPane.showMessageDialog(PaginaMeniuRegular.this, "Nu a fost gasita aceasta producție/acest actor!");
                        }
                    }
                }
            }
        });
        add(btnOptiune7);

        JButton btnOptiune8 = new JButton("Delogare");
        btnOptiune8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PaginaAutentificare paginaAutentificare = new PaginaAutentificare();
                paginaAutentificare.setVisible(true);

                dispose();
            }
        });
        add(btnOptiune8);

        setVisible(true);
    }

    private Production findProductionByName(String name) {
        for (Production production : imdb.productions) {
            if (name.equals(production.getTitle())) {
                return production;
            }
        }
        return null;
    }

    private Actor findActorByName(String name) {
        for (Actor actor : imdb.actors) {
            if (name.equals(actor.getName())) {
                return actor;
            }
        }
        return null;
    }
}
