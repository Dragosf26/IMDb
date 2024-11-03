package org.example;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaginaMeniuAdmin extends JFrame {
    IMDB imdb = IMDB.getInstance();
    private User user;

    public PaginaMeniuAdmin(User user) {
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
                user.displayInfoGUI(PaginaMeniuAdmin.this);
            }
        });
        add(btnOptiune3);

        JButton btnOptiune4 = new JButton("Cautarea unui anumit film/serial/actor");
        btnOptiune4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PaginaCautareProductieActor paginaCautareProductieActor = new PaginaCautareProductieActor(PaginaMeniuAdmin.this);
                paginaCautareProductieActor.setVisible(true);

                dispose();
            }
        });
        add(btnOptiune4);

        JButton btnOptiune5 = new JButton("Adaugarea/Stergerea unei productii/actor din lista de favorite");
        btnOptiune5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PaginaAdaugareStergereFav paginaAdaugareStergereFav = new PaginaAdaugareStergereFav(user, PaginaMeniuAdmin.this);
                paginaAdaugareStergereFav.setVisible(true);

                dispose();
            }
        });
        add(btnOptiune5);

        JButton btnOptiune6 = new JButton("Adaugarea/Stergerea unei productii/actor din sistem");
        btnOptiune6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PaginaGestionareProductii paginaGestionareProductii = new PaginaGestionareProductii((Staff) user);
                paginaGestionareProductii.setVisible(true);
            }
        });
        add(btnOptiune6);

        JButton btnOptiune7 = new JButton("Vizualizarea si rezolvarea cererilor primite");
        btnOptiune7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((Staff)user).resolveUserRequestGUI();
            }
        });
        add(btnOptiune7);

        JButton btnOptiune8 = new JButton("Actualizarea informatiilor despre productii/actori");
        btnOptiune8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PaginaActualizari paginaActualizari = new PaginaActualizari((Staff)user);
                paginaActualizari.setVisible(true);
            }
        });
        add(btnOptiune8);

        JButton btnOptiune9 = new JButton("Adaugarea/Stergerea unui utilizator din sistem");
        btnOptiune9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PaginaAdaugareUtilizator paginaAdaugareUtilizator = new PaginaAdaugareUtilizator(user);
                paginaAdaugareUtilizator.setVisible(true);
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
