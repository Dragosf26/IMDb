package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;

public class PaginaRequest extends JFrame {
    IMDB imdb = IMDB.getInstance();

    private JTextArea textArea;
    private JTextField inputField;

    User user;

    public PaginaRequest(User user, JFrame paginaAnterioara) {
        this.user = user;
        setTitle("Creeaza/Sterge Cerere");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);

        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel northWestPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
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

        textArea = new JTextArea();
        updateTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));

        JButton createButton = new JButton("Creeaza Cerere");
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afiseazaOptiuniCreareCerere();
            }
        });

        JButton deleteButton = new JButton("Sterge Cerere");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afiseazaOptiuniStergereCerere(user);
            }
        });

        buttonPanel.add(createButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void afiseazaOptiuniCreareCerere() {
        String[] options = {"DELETE_ACCOUNT", "ACTOR_ISSUE", "MOVIE_ISSUE", "OTHERS"};
        String selectedType = (String) JOptionPane.showInputDialog(
                this,
                "Alegeti tipul de cerere:",
                "Creeaza Cerere",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        if (selectedType != null) {
            LocalDateTime createdDate = LocalDateTime.now();
            DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            String formattedDate = createdDate.format(DATE_TIME_FORMATTER);
            LocalDateTime parsedDate = LocalDateTime.parse(formattedDate, DATE_TIME_FORMATTER);

            String username = user.getUsername();

            String actorName = null;
            String movieTitle = null;

            if ("ACTOR_ISSUE".equals(selectedType)) {
                actorName = JOptionPane.showInputDialog("Actor name:");
            } else if ("MOVIE_ISSUE".equals(selectedType)) {
                movieTitle = JOptionPane.showInputDialog("Movie title:");
            }

            String to = null;
            boolean found = false;

            if (!("DELETE_ACCOUNT".equals(selectedType) || "OTHERS".equals(selectedType))) {
                System.out.println("da");
                for (User user1 : imdb.users) {
                    if (user1 instanceof Contributor) {
                        for (Object productieActori : ((Contributor) user1).productieActoriAdaugati) {
                            if ("ACTOR_ISSUE".equals(selectedType) && ((String) productieActori).equals(actorName)) {
                                to = user1.getUsername();
                                System.out.println(productieActori);
                                found = true;
                                break;
                            } else if ("MOVIE_ISSUE".equals(selectedType) && ((String) productieActori).equals(movieTitle)) {
                                to = user1.getUsername();
                                found = true;
                                break;
                            }
                        }
                    }
                    if (found) {
                        break;
                    }
                }
            } else {
                to = "ADMIN";
            }

            if (("ACTOR_ISSUE".equals(selectedType) || "MOVIE_ISSUE".equals(selectedType)) && !found) {
                JOptionPane.showMessageDialog(this, "Nu a fost gasit");
                return;
            }

            String description = JOptionPane.showInputDialog("Description:");

            Request.RequestType requestType = getRequestType(selectedType);
            Request request = new Request(requestType, parsedDate, username, to, description, actorName, movieTitle);

            if (user instanceof Regular) {
                ((Regular) user).createRequest(request);
            } else if (user instanceof Contributor) {
                ((Contributor) user).createRequest(request);
            }

            updateTextArea();
        }
    }

    private void afiseazaOptiuniStergereCerere(User user) {
        int indexRequest = 0;
        StringBuilder requestsInfo = new StringBuilder();

        for (Request request : imdb.requests) {
            if (request.getUsername().equals(user.getUsername())) {
                indexRequest++;
                requestsInfo.append("----------").append(indexRequest).append("----------\n");
                requestsInfo.append(request.displayInfoGUI()).append("\n");
            }
        }

        if (indexRequest > 0) {
            String input = JOptionPane.showInputDialog("Selectati numarul cererii pe care doriti sa o stergeti:\n" + requestsInfo);

            if (input != null) {
                try {
                    int indexCerere = Integer.parseInt(input);
                    int counter = 0;

                    for (Request request : imdb.requests) {
                        if (request.getUsername().equals(user.getUsername())) {
                            counter++;
                            if (indexCerere == counter) {
                                imdb.requests.remove(request);
                                if (user instanceof Regular) {
                                    ((Regular) user).removeRequest(request);
                                } else if (user instanceof Contributor) {
                                    ((Contributor) user).removeRequest(request);
                                }
                                updateTextArea();
                                break;
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Va rugam introduceti un numar valid.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nu exista cereri de sters.");
        }
    }

    private void updateTextArea() {
        StringBuilder requestsInfo = new StringBuilder();
        for (Request request : imdb.requests) {
            if (request.getUsername().equals(user.getUsername())) {
                requestsInfo.append("----------\n");
                requestsInfo.append(request.displayInfoGUI()).append("\n");
            }
        }
        textArea.setText(requestsInfo.toString());
    }

    private Request.RequestType getRequestType(String selectedType) {
        switch (selectedType) {
            case "DELETE_ACCOUNT":
                return Request.RequestType.DELETE_ACCOUNT;
            case "ACTOR_ISSUE":
                return Request.RequestType.ACTOR_ISSUE;
            case "MOVIE_ISSUE":
                return Request.RequestType.MOVIE_ISSUE;
            case "OTHERS":
                return Request.RequestType.OTHERS;
            default:
                throw new IllegalArgumentException("Tip de cerere nevalid: " + selectedType);
        }
    }

}
