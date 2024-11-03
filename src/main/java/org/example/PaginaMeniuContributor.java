package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaginaMeniuContributor extends JFrame {
    private User user;
    IMDB imdb = IMDB.getInstance();

    public PaginaMeniuContributor(User user) {
        this.user = user;
        setTitle("Pagina de Meniu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new GridLayout(10, 1));
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
                user.displayInfoGUI(PaginaMeniuContributor.this);            }
        });
        add(btnOptiune3);

        JButton btnOptiune4 = new JButton("Cautarea unui anumit film/serial/actor");
        btnOptiune4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PaginaCautareProductieActor paginaCautareProductieActor = new PaginaCautareProductieActor(PaginaMeniuContributor.this);
                paginaCautareProductieActor.setVisible(true);

                dispose();
            }
        });
        add(btnOptiune4);

        JButton btnOptiune5 = new JButton("Adaugarea/Stergerea unei productii/actor din lista de favorite");
        btnOptiune5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PaginaAdaugareStergereFav paginaAdaugareStergereFav = new PaginaAdaugareStergereFav(user, PaginaMeniuContributor.this);
                paginaAdaugareStergereFav.setVisible(true);

                dispose();
            }
        });
        add(btnOptiune5);

        JButton btnOptiune6 = new JButton("Crearea/Retragerea unei cereri");
        btnOptiune6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PaginaRequest paginaRequest = new PaginaRequest(user, PaginaMeniuContributor.this);
                paginaRequest.setVisible(true);

                dispose();            }
        });
        add(btnOptiune6);

        JButton btnOptiune7 = new JButton("Adaugarea/Stergerea unei productii/actor din sisteme");
        btnOptiune7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PaginaGestionareProductii paginaGestionareProductii = new PaginaGestionareProductii((Staff) user);
                paginaGestionareProductii.setVisible(true);
            }
        });
        add(btnOptiune7);

        JButton btnOptiune8 = new JButton("Vizualizarea si rezolvarea cererilor primite ");
        btnOptiune8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((Staff)user).resolveUserRequestGUI();
            }
        });
        add(btnOptiune8);

        JButton btnOptiune9 = new JButton("Actualizarea informatiilor despre productii/actori ");
        btnOptiune9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PaginaActualizari paginaActualizari = new PaginaActualizari((Staff)user);
                paginaActualizari.setVisible(true);
            }
        });
        add(btnOptiune9);

        JButton btnOptiune10 = new JButton("Delogare");
        btnOptiune10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PaginaAutentificare paginaAutentificare = new PaginaAutentificare();
                paginaAutentificare.setVisible(true);

                dispose();
            }
        });
        add(btnOptiune10);

        setVisible(true);
    }
}
