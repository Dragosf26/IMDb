package org.example;

import org.example.Enum.Genre;
import org.example.StrategyPattern.CalculateExperienceContex;
import org.example.StrategyPattern.RequestStrategy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PaginaResolveRequest extends JFrame {
    IMDB imdb = IMDB.getInstance();
    private JTextArea textArea;
    private JComboBox<String> requestComboBox;
    private JButton resolveButton;

    private Staff currentUser;

    private JPanel mainPanel;

    public PaginaResolveRequest(Staff user) {
        this.currentUser = user;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Resolve Requests");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        textArea = new JTextArea(10, 40);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        populateRequestComboBox();

        resolveButton = new JButton("Resolve Selected Request");
        resolveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resolveSelectedRequest();
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(resolveButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);


        int selectedIndex = requestComboBox.getSelectedIndex();

        if (selectedIndex != -1) {
            if (currentUser instanceof Contributor) {
                textArea.setText(((Request) currentUser.getRequestList().get(selectedIndex)).displayInfoGUI());
            } else if (currentUser instanceof Admin) {
                textArea.setText(RequestsHolder.requestList.get(selectedIndex).displayInfoGUI());
            }
        }

        requestComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    int selectedIndex = requestComboBox.getSelectedIndex();

                    if (currentUser instanceof Contributor) {
                        textArea.setText(((Request) currentUser.getRequestList().get(selectedIndex)).displayInfoGUI());
                    } else if (currentUser instanceof Admin) {
                        textArea.setText(RequestsHolder.requestList.get(selectedIndex).displayInfoGUI());
                    }
                }
            }
        });

        add(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void populateRequestComboBox() {
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        if (currentUser instanceof Contributor) {
            for (Object requestObj : currentUser.getRequestList()) {
                comboBoxModel.addElement(((Request) requestObj).getDescription());
            }
        } else if (currentUser instanceof  Admin) {
            for (Request requestObj : RequestsHolder.requestList) {
                comboBoxModel.addElement(requestObj.getDescription());
            }
        }
        requestComboBox = new JComboBox<>(comboBoxModel);
        JPanel comboBoxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        comboBoxPanel.add(requestComboBox);
        mainPanel.add(comboBoxPanel, BorderLayout.NORTH);
    }

    private void resolveSelectedRequest() {
        int selectedIndex = requestComboBox.getSelectedIndex();

        if (selectedIndex != -1) {
            Request requestObj = null;
            if (currentUser instanceof Contributor) {
                requestObj = (Request) currentUser.getRequestList().get(selectedIndex);
            } else if (currentUser instanceof Admin) {
                requestObj = RequestsHolder.requestList.get(selectedIndex);
            }

            if (requestObj.getType() == Request.RequestType.ACTOR_ISSUE) {
                handleActorIssue(requestObj);
            } else if (requestObj.getType() == Request.RequestType.MOVIE_ISSUE) {
                handleMovieIssue(requestObj);
            } else if (requestObj.getType() == Request.RequestType.DELETE_ACCOUNT) {
                handleDeleteAccount(requestObj);
            } else if (requestObj.getType() == Request.RequestType.OTHERS) {
                handleOthersIssue(requestObj);
            }

            textArea.setText("Request resolved successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Selectati o cerere inainte de a rezolva.", "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleActorIssue(Request actorRequest) {
        for (Actor actor : imdb.actors) {
            if (actor.getName().equals(actorRequest.getActorName())) {
                String[] options = {"Nume", "Performante", "Biografie"};
                JComboBox<String> optionsComboBox = new JComboBox<>(options);

                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Selectati ce doriti sa modificati:"));
                panel.add(optionsComboBox);

                int result = JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Modificare Actor",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    String selectedOption = (String) optionsComboBox.getSelectedItem();

                    switch (selectedOption) {
                        case "Nume":
                            handleActorNameModification(actor, actorRequest);
                            break;
                        case "Performante":
                            handleActorPerformancesModification(actor, actorRequest);
                            break;
                        case "Biografie":
                            handleActorBiographyModification(actor, actorRequest);
                            break;
                        default:
                            break;
                    }
                }

                String userUpdateExp = actorRequest.getUsername();
                for (User user : imdb.users) {
                    if (user.getUsername().equals(userUpdateExp)) {
                        CalculateExperienceContex calculateExperienceContex = new CalculateExperienceContex();
                        calculateExperienceContex.setExperienceStrategy(new RequestStrategy(user.getExperienta()));
                        user.setExperienta(calculateExperienceContex.calculateExp());
                        break;
                    }
                }
                break;
            }
        }
    }

    private void handleActorNameModification(Actor actor, Request actorRequest) {
        String newActorName = JOptionPane.showInputDialog(this, "Introduceti noul nume pentru actor:", "Modificare Nume Actor", JOptionPane.PLAIN_MESSAGE);
        actor.setName(newActorName);
        actorRequest.setResolved(true);
    }

    private void handleActorPerformancesModification(Actor actor, Request actorRequest) {
        StringBuilder performancesInfo = new StringBuilder("Performante curente:\n");
        for (Map<String, String> performance : actor.getPerformances()) {
            performancesInfo.append("   - ").append(performance.get("title")).append(": ").append(performance.get("type")).append("\n");
        }

        JTextArea performancesTextArea = new JTextArea(performancesInfo.toString());
        performancesTextArea.setEditable(false);

        JComboBox<String> performancesComboBox = new JComboBox<>();
        for (Map<String, String> performance : actor.getPerformances()) {
            performancesComboBox.addItem(performance.get("title"));
        }

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JLabel("Selectati performanta de modificat:"), BorderLayout.NORTH);
        inputPanel.add(performancesComboBox, BorderLayout.CENTER);

        // Show a dialog with the JComboBox
        int result = JOptionPane.showConfirmDialog(
                this,
                new JScrollPane(performancesTextArea),
                "Performante Actor",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String selectedPerformance = (String) performancesComboBox.getSelectedItem();
            if (selectedPerformance != null) {
                String newTitle = JOptionPane.showInputDialog(this, "Introduceti noul titlu pentru performanta:", selectedPerformance);

                for (int i = 0; i < actor.getPerformances().size(); i++) {
                    Map<String, String> performance = actor.getPerformances().get(i);
                    if (performance.get("title").equals(selectedPerformance)) {
                        System.out.println("Introduceti noul titlu pentru performanta:");

                        actor.getPerformances().set(i, Map.of("title", newTitle, "type", "Movie"));

                        System.out.println("Performanta modificata cu succes.");
                        (actorRequest).setResolved(true);
                        break;
                    }
                }

                JOptionPane.showMessageDialog(this, "Performanta modificata cu succes.");
            }
        }
    }



    private void handleActorBiographyModification(Actor actor, Request actorRequest) {
        String newBiography = JOptionPane.showInputDialog(this, "Introduceti noua biografie pentru actor:", "Modificare Biografie Actor", JOptionPane.PLAIN_MESSAGE);
        actor.setBiography(newBiography);
        actorRequest.setResolved(true);
    }



    private void handleMovieIssue(Request movieRequest) {
        for (Production production : imdb.productions) {
            if (production.getTitle().equals(movieRequest.getMovieTitle())) {
                String[] options = {"Titlul", "Regizorii", "Actorii", "Genurile", "Descrierea", "Durata", "Anul lansarii"};
                JComboBox<String> optionsComboBox = new JComboBox<>(options);

                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Selectati ce doriti sa modificati:"));
                panel.add(optionsComboBox);

                int result = JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Modificare Film",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    String selectedOption = (String) optionsComboBox.getSelectedItem();

                    switch (selectedOption) {
                        case "Titlul":
                            handleMovieTitleModification(production, movieRequest);
                            break;
                        case "Regizorii":
                            handleMovieDirectorsModification(production, movieRequest);
                            break;
                        case "Actorii":
                            handleMovieActorsModification(production, movieRequest);
                            break;
                        case "Genurile":
                            handleMovieGenresModification(production, movieRequest);
                            break;
                        case "Descrierea":
                            handleMoviePlotModification(production, movieRequest);
                            break;
                        case "Durata":
                            handleMovieDurationModification((Movie) production, movieRequest);
                            break;
                        case "Anul lansarii":
                            handleMovieReleaseYearModification((Movie) production, movieRequest);
                            break;
                        default:
                            // Optiune invalida
                            break;
                    }
                }
                String userUpdateExp = movieRequest.getUsername();
                for (User user : imdb.users) {
                    if (user.getUsername().equals(userUpdateExp)) {
                        CalculateExperienceContex calculateExperienceContex = new CalculateExperienceContex();
                        calculateExperienceContex.setExperienceStrategy(new RequestStrategy(user.getExperienta()));
                        user.setExperienta(calculateExperienceContex.calculateExp());
                        break;
                    }
                }
                break;
            }
        }
    }

    private void handleMovieTitleModification(Production production, Request movieRequest) {
        String newTitle = JOptionPane.showInputDialog(this, "Introduceti noul titlu pentru film:", "Modificare Titlu Film", JOptionPane.PLAIN_MESSAGE);
        production.setTitle(newTitle);
        movieRequest.setResolved(true);
    }

    private void handleMovieDirectorsModification(Production production, Request movieRequest) {
        String newDirectorsInput = JOptionPane.showInputDialog(this, "Introduceti noul regizor (separate by commas):", "Modificare Regizori Film", JOptionPane.PLAIN_MESSAGE);
        java.util.List<String> newDirectors = Arrays.asList(newDirectorsInput.split(","));
        production.setDirectors(newDirectors);
        movieRequest.setResolved(true);

    }

    private void handleMovieActorsModification(Production production, Request movieRequest) {
        String newActorsInput = JOptionPane.showInputDialog(this, "Introduceti noi actori (separate by commas):", "Modificare Actorii Film", JOptionPane.PLAIN_MESSAGE);
        java.util.List<String> newActors = Arrays.asList(newActorsInput.split(","));
        production.setActors(newActors);
        movieRequest.setResolved(true);
    }

    private void handleMovieGenresModification(Production production, Request movieRequest) {
        String newGenresInput = JOptionPane.showInputDialog(this, "Introduceti noile genuri (separate by commas):", "Modificare Genuri Film", JOptionPane.PLAIN_MESSAGE);
        List<Genre> newGenres = Arrays.stream(newGenresInput.split(","))
                .map(Genre::valueOf)
                .collect(Collectors.toList());
        production.setGenres(newGenres);
        movieRequest.setResolved(true);
    }

    private void handleMoviePlotModification(Production production, Request movieRequest) {
        String newPlot = JOptionPane.showInputDialog(this, "Introduceti noua descriere pentru film:", "Modificare Descriere Film", JOptionPane.PLAIN_MESSAGE);
        production.setPlot(newPlot);
        movieRequest.setResolved(true);
    }

    private void handleMovieDurationModification(Movie movie, Request movieRequest) {
        int newDuration = Integer.parseInt(JOptionPane.showInputDialog(this, "Introduceti noua durata pentru film (in minute):", "Modificare Durata Film", JOptionPane.PLAIN_MESSAGE));
        movie.setDuration(newDuration);
        movieRequest.setResolved(true);
    }

    private void handleMovieReleaseYearModification(Movie movie, Request movieRequest) {
        int newReleaseYear = Integer.parseInt(JOptionPane.showInputDialog(this, "Introduceti noul an de lansare pentru film:", "Modificare An Lansare Film", JOptionPane.PLAIN_MESSAGE));
        movie.setReleaseYear(newReleaseYear);
        movieRequest.setResolved(true);
    }

    private void handleDeleteAccount(Request request) {
        for (User user1 : imdb.users) {
            if (request.getUsername().equals(user1.getUsername())) {
                imdb.users.remove(user1);
                request.setResolved(true);
                break;
            }
        }
    }

    private void handleOthersIssue(Request request) {
            request.setResolved(true);

            String[] options = {
                    "Username-ul",
                    "Experienta",
                    "Email-ul",
                    "Parola",
                    "Numele",
                    "Tara",
                    "Varsta",
                    "Genul",
                    "Data nasterii"
            };

            JComboBox<String> optionsComboBox = new JComboBox<>(options);
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Ce modificati?"));
            panel.add(optionsComboBox);

            int result = JOptionPane.showConfirmDialog(
                    this,
                    panel,
                    "Modificare Informatii Utilizator",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                String selectedOption = (String) optionsComboBox.getSelectedItem();

                for (User user1 : imdb.users) {
                    if (request.getUsername().equals(user1.getUsername())) {
                        switch (selectedOption) {
                            case "Username-ul":
                                String newUsername = JOptionPane.showInputDialog(this, "Introduceti noul username:", "Modificare Username", JOptionPane.PLAIN_MESSAGE);
                                user1.setUsername(newUsername);
                                break;
                            case "Experienta":
                                int newExperience = Integer.parseInt(JOptionPane.showInputDialog(this, "Introduceti noua experienta:", "Modificare Experienta", JOptionPane.PLAIN_MESSAGE));
                                user1.setExperienta(newExperience);
                                break;
                            case "Email-ul":
                                String newEmail = JOptionPane.showInputDialog(this, "Introduceti noul email:", "Modificare Email", JOptionPane.PLAIN_MESSAGE);
                                user1.information.getCredentials().setUsername(newEmail);
                                break;
                            case "Parola":
                                String newPassword = JOptionPane.showInputDialog(this, "Introduceti noua parola:", "Modificare Parola", JOptionPane.PLAIN_MESSAGE);
                                user1.information.getCredentials().setPassword(newPassword);
                                break;
                            case "Numele":
                                String newName = JOptionPane.showInputDialog(this, "Introduceti noul nume:", "Modificare Nume", JOptionPane.PLAIN_MESSAGE);
                                user1.information.setName(newName);
                                break;
                            case "Tara":
                                String newCountry = JOptionPane.showInputDialog(this, "Introduceti noua tara:", "Modificare Tara", JOptionPane.PLAIN_MESSAGE);
                                user1.information.setCountry(newCountry);
                                break;
                            case "Varsta":
                                int newAge = Integer.parseInt(JOptionPane.showInputDialog(this, "Introduceti noua varsta:", "Modificare Varsta", JOptionPane.PLAIN_MESSAGE));
                                user1.information.setAge(newAge);
                                break;
                            case "Genul":
                                char newGender = JOptionPane.showInputDialog(this, "Introduceti noul gen(M/F):", "Modificare Gen", JOptionPane.PLAIN_MESSAGE).charAt(0);
                                user1.information.setGender(newGender);
                                break;
                            case "Data nasterii":
                                String birthDateInput = JOptionPane.showInputDialog(this, "Introduceti noua data de nastere (yyyy-MM-dd):", "Modificare Data Nasterii", JOptionPane.PLAIN_MESSAGE);
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                LocalDate birthDate = LocalDate.parse(birthDateInput, formatter);
                                user1.information.setBirthDate(birthDate);
                                break;
                            default:
                                JOptionPane.showMessageDialog(this, "Alegere incorecta!", "Eroare", JOptionPane.ERROR_MESSAGE);
                                break;
                        }
                        break;
                    }
                }
            }
    }

}
