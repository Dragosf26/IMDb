package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaginaAdaugareStergereFav extends JFrame {
    private User user;
    private JTextArea displayArea;
    IMDB imdb = IMDB.getInstance();
    public PaginaAdaugareStergereFav(User user, JFrame paginaAnterioara) {
        this.user = user;
        setTitle("Pagina favoriti");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Afiaeaza zona de afiaare a detaliilor
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        JButton addButton = new JButton("Adaugati");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddAction();
            }
        });

        JButton deleteButton = new JButton("Stergeti");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDeleteAction();
            }
        });

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

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        displayInfo();
    }

    private void handleAddAction() {
        String search = JOptionPane.showInputDialog("Scrie-ti un film/serial/actor pe care doriti sa-l adaugati:");

        if (search != null) {
            boolean found = false;

            for (Production production : imdb.productions) {
                if (production.getTitle().equals(search)) {
                    user.getProductieActoriPreferati().add(production.getTitle());
                    found = true;
                    break;
                }
            }

            if (!found) {
                for (Actor actor : imdb.actors) {
                    if (actor.getName().equals(search)) {
                        user.getProductieActoriPreferati().add(actor.getName());
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "Productia/Actorul nu a fost gasit");
            }

            displayInfo();
        }
    }

    private void handleDeleteAction() {
        String search = JOptionPane.showInputDialog("Scrie-ti un film/serial/actor pe care doriti sa-l stergeti:");

        if (search != null) {
            boolean found = false;

            for (Production production : imdb.productions) {
                if (production.getTitle().equals(search)) {
                    user.getProductieActoriPreferati().remove(production.getTitle());
                    found = true;
                    break;
                }
            }

            if (!found) {
                for (Actor actor : imdb.actors) {
                    if (actor.getName().equals(search)) {
                        user.getProductieActoriPreferati().remove(actor.getName());
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "Productia/Actorul nu a fost gasit");
            }

            displayInfo();
        }
    }

    private void displayInfo() {
        StringBuilder info = new StringBuilder("Productie/Actori favoriti:\n");
        for (Object productieActori : user.getProductieActoriPreferati()) {
            info.append((String) productieActori).append("\n");
        }

        displayArea.setText(info.toString());
    }

}
