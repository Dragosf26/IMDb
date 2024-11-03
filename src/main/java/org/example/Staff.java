package org.example;

import org.example.Enum.AccountType;
import org.example.Enum.Genre;
import org.example.Interfaces.ObserverProduction;
import org.example.Interfaces.StaffInterface;
import org.example.StrategyPattern.CalculateExperienceContex;
import org.example.StrategyPattern.RatingStrategy;
import org.example.StrategyPattern.RequestStrategy;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Staff extends User implements StaffInterface, ObserverProduction {
    IMDB imdb = IMDB.getInstance();
    List<Request> requestList;
    SortedSet<Object> productieActoriAdaugati;

    private static Scanner scanner = new Scanner(System.in);

    public Staff(JSONObject jsonObject) {
        super(jsonObject);

        requestList = new ArrayList<>();

        SortedSet<Object> adaugati = new TreeSet<>();
        if(jsonObject.containsKey("productionsContribution")) {
            JSONArray JSONproductionsContribution = (JSONArray) jsonObject.get("productionsContribution");
            for(Object item : JSONproductionsContribution) {
                adaugati.add((Object) item);
            }
        }

        if(jsonObject.containsKey("actorsContribution")) {
            JSONArray JSONactorsContribution = (JSONArray) jsonObject.get("actorsContribution");
            for(Object item : JSONactorsContribution) {
                adaugati.add((Object) item);
            }
        }

        this.productieActoriAdaugati = adaugati;
    }

    public Staff(Information information, AccountType accountType, String username, int experienta, List notificari, SortedSet productieActoriPreferati, List<Request> requestList, SortedSet<Object> productieActoriAdaugati) {
        super(information, accountType, username, experienta, notificari, productieActoriPreferati);
        this.requestList = requestList;
        this.productieActoriAdaugati = productieActoriAdaugati;
    }

    public List<Request> getRequestList() {
        return requestList;
    }

    public SortedSet<Object> getProductieActoriAdaugati() {
        return productieActoriAdaugati;
    }

    @Override
    public void addProductionSystem(Production p) {
        imdb.productions.add(p);
    }

    @Override
    public void addActorSystem(Actor a) {
        imdb.actors.add(a);
    }

    @Override
    public void removeProductionSystem(String name) {
        for (Production production : imdb.productions) {
            if (production.getTitle().equals(name)) {
                imdb.productions.remove(production);
                break;
            }
        }
    }

    @Override
    public void removeActorSystem(String name) {
        for (Actor actor : imdb.actors) {
            if (actor.getName().equals(name)) {
                imdb.actors.remove(actor);
            }
        }
    }

    @Override
    public void updateProduction(Production production) {
        if (production instanceof Movie) {
            System.out.println("Ce modificati?");
            System.out.println("1. Titlul");
            System.out.println("2. Regizorii");
            System.out.println("3. Actorii");
            System.out.println("4. Genurile");
            System.out.println("5. Descrierea");
            System.out.println("6. Durata");
            System.out.println("7. Anul lansarii");

            int alegere2 = scanner.nextInt();
            if (alegere2 == 1) {
                System.out.println("Introduceti noul nume");
                scanner.nextLine();
                String newTitle = scanner.nextLine();
                production.setTitle(newTitle);
                return;
            } else if (alegere2 == 2) {
                System.out.println("Introduceti noul regizor (separate by commas)");
                scanner.nextLine();
                String newDirectorsInput = scanner.nextLine();
                List<String> newDirectors = Arrays.asList(newDirectorsInput.split(","));
                production.setDirectors(newDirectors);
                return;
            } else if (alegere2 == 3) {
                System.out.println("Introduceti noul actor (separate by commas)");
                scanner.nextLine();
                String newActorsInput = scanner.nextLine();
                List<String> newActors = Arrays.asList(newActorsInput.split(","));
                production.setActors(newActors);
                return;
            } else if (alegere2 == 4) {
                System.out.println("Introduceti noul gen (separate by commas)");
                scanner.nextLine();
                String newGenresInput = scanner.nextLine();
                List<Genre> newGenres = Arrays.stream(newGenresInput.split(","))
                        .map(Genre::valueOf)
                        .collect(Collectors.toList());
                production.setGenres(newGenres);
                return;
            } else if (alegere2 == 5) {
                System.out.println("Introduceti noua descriere");
                scanner.nextLine();
                String newPlot = scanner.nextLine();
                production.setPlot(newPlot);
                return;
            } else if (alegere2 == 6) {
                System.out.println("Introduceti noua durata");
                scanner.nextLine();
                int newDuration = scanner.nextInt();
                ((Movie)production).setDuration(newDuration);
                return;
            } else if (alegere2 == 7) {
                System.out.println("Introduceti noul an de lansare");
                scanner.nextLine();
                int newReleaseYear = scanner.nextInt();
                ((Movie)production).setReleaseYear(newReleaseYear);
                return;
            }
        } else {
            System.out.println("Ce modificati?");
            System.out.println("1. Titlul");
            System.out.println("2. Regizorii");
            System.out.println("3. Actorii");
            System.out.println("4. Genurile");
            System.out.println("5. Descrierea");
            System.out.println("6. Numarul de sezoane");
            System.out.println("7. Anul lansarii");
            System.out.println("8. Sezoanele si Episoadele");

            int alegere2 = scanner.nextInt();
            if (alegere2 == 1) {
                System.out.println("Introduceti noul nume");
                scanner.nextLine();
                String newTitle = scanner.nextLine();
                System.out.println("Vechiul nume " + production.getTitle());
                production.setTitle(newTitle);
                System.out.println("Noul nume " + production.getTitle());
                return;
            } else if (alegere2 == 2) {
                System.out.println("Introduceti noul regizor (separate by commas)");
                scanner.nextLine();
                String newDirectorsInput = scanner.nextLine();
                List<String> newDirectors = Arrays.asList(newDirectorsInput.split(","));
                production.setDirectors(newDirectors);
                return;
            } else if (alegere2 == 3) {
                System.out.println("Introduceti noul actor (separate by commas)");
                scanner.nextLine();
                String newActorsInput = scanner.nextLine();
                List<String> newActors = Arrays.asList(newActorsInput.split(","));
                production.setActors(newActors);
                return;
            } else if (alegere2 == 4) {
                System.out.println("Introduceti noul gen (separate by commas)");
                scanner.nextLine();
                String newGenresInput = scanner.nextLine();
                List<Genre> newGenres = Arrays.stream(newGenresInput.split(","))
                        .map(Genre::valueOf)
                        .collect(Collectors.toList());
                production.setGenres(newGenres);
                return;
            } else if (alegere2 == 5) {
                System.out.println("Introduceti noua descriere");
                scanner.nextLine();
                String newPlot = scanner.nextLine();
                production.setPlot(newPlot);
                return;
            } else if (alegere2 == 6) {
                System.out.println("Introduceti numarul de sezoane");
                scanner.nextLine();
                int newNumSeasons = scanner.nextInt();
                ((Series) production).setNumSeasons(newNumSeasons);
                return;
            } else if (alegere2 == 7) {
                System.out.println("Introduceti noul an de lansare");
                scanner.nextLine();
                int newReleaseYear = scanner.nextInt();
                ((Series) production).setReleaseYear(newReleaseYear);
                return;
            } else if (alegere2 == 8) {
                Map<String, List<Episode>> seasons = new LinkedHashMap<>();

                System.out.println("Introduceti numarul de sezoane");
                scanner.nextLine();
                int numSeasons = scanner.nextInt();

                for (int i = 1; i <= numSeasons; i++) {
                    List<Episode> episodes = new ArrayList<>();

                    System.out.println("Introduceti numarul de episoade pentru sezonul " + i);
                    int numEpisodes = scanner.nextInt();

                    for (int j = 1; j <= numEpisodes; j++) {
                        System.out.println("Introduceti nume pentru episodul " + j);
                        scanner.nextLine();
                        String episodeName = scanner.nextLine();

                        System.out.println("Introduceti durata pentru episodul " + j);
                        int duration = scanner.nextInt();

                        Episode episode = new Episode(episodeName, duration);

                        episodes.add(episode);
                    }

                    seasons.put("Sezonul " + i, episodes);
                }

                ((Series) production).setSeasons(seasons);
                return;
            }

        }
    }

    @Override
    public void updateActor(Actor actor) {
        System.out.println("Ce modificati?");
        System.out.println("1. Numele");
        System.out.println("2. Performanetele");
        System.out.println("3. Biografia");

        int alegere2 = scanner.nextInt();
        if (alegere2 == 1) {
            System.out.println("Introduceti noul nume");
            scanner.nextLine();
            String newName = scanner.nextLine();
            actor.setName(newName);
        } else if (alegere2 == 2) {
            System.out.println("Selectati performanta de modificat:");
            scanner.nextLine();
            String movieToModify = scanner.nextLine();

            for (int i = 0; i < actor.getPerformances().size(); i++) {
                Map<String, String> performance = actor.getPerformances().get(i);
                if (performance.get("title").equals(movieToModify)) {
                    System.out.println("Introduceti noul titlu pentru performanta:");
                    String newTitle = scanner.nextLine();

                    actor.getPerformances().set(i, Map.of("title", newTitle, "type", "Movie"));

                    System.out.println("Performanta modificata cu succes.");
                    break;
                }
            }
        } else if (alegere2 == 3) {
            System.out.println("Introduceti noua biografie");
            scanner.nextLine();
            String newBiography = scanner.nextLine();
            actor.setBiography(newBiography);
        } else {
            System.out.println("Alegere incorecta!");
        }
    }

    public void updateProductionGUI(Production production, JFrame frame) {
        if (production instanceof Movie) {
            String[] options = {"Titlul", "Regizorii", "Actorii", "Genurile", "Descrierea", "Durata", "Anul lansarii"};

            JComboBox<String> updateOptionsComboBox = new JComboBox<>(options);
            JTextField userInputField = new JTextField();

            JPanel inputPanel = new JPanel(new GridLayout(0, 2));
            inputPanel.add(new JLabel("Selectati ce doriti sa modificati:"));
            inputPanel.add(updateOptionsComboBox);
            inputPanel.add(new JLabel("Introduceti noua valoare:"));
            inputPanel.add(userInputField);

            int result = JOptionPane.showConfirmDialog(
                    frame,
                    inputPanel,
                    "Actualizare Productie",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                int selectedIndex = updateOptionsComboBox.getSelectedIndex() + 1;
                String userInput = userInputField.getText().trim();

                switch (selectedIndex) {
                    case 1:
                        production.setTitle(userInput);
                        break;
                    case 2:
                        List<String> newDirectors = Arrays.asList(userInput.split(","));
                        production.setDirectors(newDirectors);
                        break;
                    case 3:
                        List<String> newActors = Arrays.asList(userInput.split(","));
                        production.setActors(newActors);
                        break;
                    case 4:
                        List<Genre> newGenres = Arrays.stream(userInput.split(","))
                                .map(Genre::valueOf)
                                .collect(Collectors.toList());
                        production.setGenres(newGenres);
                        break;
                    case 5:
                        production.setPlot(userInput);
                        break;
                    case 6:
                        try {
                            int newDuration = Integer.parseInt(userInput);
                            if (production instanceof Movie) {
                                ((Movie) production).setDuration(newDuration);
                            } else {
                                JOptionPane.showMessageDialog(frame, "Durata este specifica doar pentru filme!");
                            }
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(frame, "Durata introdusa nu este valida!");
                        }
                        break;
                    case 7:
                        try {
                            int newReleaseYear = Integer.parseInt(userInput);
                            if (production instanceof Movie) {
                                ((Movie) production).setReleaseYear(newReleaseYear);
                            } else {
                                JOptionPane.showMessageDialog(frame, "Anul de lansare este specific doar pentru filme!");
                            }
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(frame, "Anul introdus nu este valid!");
                        }
                        break;
                    default:
                        JOptionPane.showMessageDialog(frame, "Alegere incorecta!");
                }
            }
        } else if (production instanceof Series) {
                String[] options = {"Titlul", "Regizorii", "Actorii", "Genurile", "Descrierea", "Numarul de sezoane", "Anul lansarii", "Sezoanele si Episoadele"};

                JComboBox<String> updateOptionsComboBox = new JComboBox<>(options);
                JTextField userInputField = new JTextField();

                JPanel inputPanel = new JPanel(new GridLayout(0, 2));
                inputPanel.add(new JLabel("Selectati ce doriti sa modificati:"));
                inputPanel.add(updateOptionsComboBox);
                inputPanel.add(new JLabel("Introduceti noua valoare:"));
                inputPanel.add(userInputField);

                int result = JOptionPane.showConfirmDialog(
                        frame,
                        inputPanel,
                        "Actualizare Serial",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if (result == JOptionPane.OK_OPTION) {
                    int selectedIndex = updateOptionsComboBox.getSelectedIndex() + 1;
                    String userInput = userInputField.getText().trim();

                    switch (selectedIndex) {
                        case 1:
                            production.setTitle(userInput);
                            break;
                        case 2:
                            List<String> newDirectors = Arrays.asList(userInput.split(","));
                            production.setDirectors(newDirectors);
                            break;
                        case 3:
                            List<String> newActors = Arrays.asList(userInput.split(","));
                            production.setActors(newActors);
                            break;
                        case 4:
                            List<Genre> newGenres = Arrays.stream(userInput.split(","))
                                    .map(Genre::valueOf)
                                    .collect(Collectors.toList());
                            production.setGenres(newGenres);
                            break;
                        case 5:
                            production.setPlot(userInput);
                            break;
                        case 6:
                            try {
                                int newNumSeasons = Integer.parseInt(userInput);
                                ((Series)production).setNumSeasons(newNumSeasons);
                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(frame, "Numarul de sezoane introdus nu este valid!");
                            }
                            break;
                        case 7:
                            try {
                                int newReleaseYear = Integer.parseInt(userInput);
                                ((Series)production).setReleaseYear(newReleaseYear);
                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(frame, "Anul introdus nu este valid!");
                            }
                            break;
                        case 8:
                            Map<String, List<Episode>> seasons = new LinkedHashMap<>();

                            try {
                                int numSeasons = Integer.parseInt(JOptionPane.showInputDialog(frame, "Introduceti numarul de sezoane:"));

                                for (int i = 1; i <= numSeasons; i++) {
                                    List<Episode> episodes = new ArrayList<>();

                                    int numEpisodes = Integer.parseInt(JOptionPane.showInputDialog(frame, "Introduceti numarul de episoade pentru sezonul " + i));

                                    for (int j = 1; j <= numEpisodes; j++) {
                                        String episodeName = JOptionPane.showInputDialog(frame, "Introduceti nume pentru episodul " + j);
                                        int duration = Integer.parseInt(JOptionPane.showInputDialog(frame, "Introduceti durata pentru episodul " + j));

                                        Episode episode = new Episode(episodeName, duration);
                                        episodes.add(episode);
                                    }

                                    seasons.put("Sezonul " + i, episodes);
                                }

                                ((Series)production).setSeasons(seasons);
                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(frame, "Numarul introdus nu este valid!");
                            }
                            break;
                        default:
                            JOptionPane.showMessageDialog(frame, "Alegere incorecta!");
                    }
                }
            }

    }

    public void updateActorGUI(Actor actor, JFrame frame) {
        String[] options = {"Numele", "Performanetele", "Biografia"};

        JComboBox<String> updateOptionsComboBox = new JComboBox<>(options);
        JTextField userInputField = new JTextField();

        JPanel inputPanel = new JPanel(new GridLayout(0, 2));
        inputPanel.add(new JLabel("Selectati ce doriti sa modificati:"));
        inputPanel.add(updateOptionsComboBox);
        inputPanel.add(new JLabel("Introduceti noua valoare:"));
        inputPanel.add(userInputField);

        int result = JOptionPane.showConfirmDialog(
                frame,
                inputPanel,
                "Actualizare Actor",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            int selectedIndex = updateOptionsComboBox.getSelectedIndex() + 1;
            String userInput = userInputField.getText().trim();

            switch (selectedIndex) {
                case 1:
                    actor.setName(userInput);
                    break;
                case 2:
                    StringBuilder performancesInfo = new StringBuilder("Performante curente:\n");
                    for (Map<String, String> performance : actor.getPerformances()) {
                        performancesInfo.append("   - ").append(performance.get("title")).append(": ").append(performance.get("type")).append("\n");
                    }

                    JTextArea performancesTextArea = new JTextArea(performancesInfo.toString());
                    performancesTextArea.setEditable(false);

                    JOptionPane.showMessageDialog(frame, new JScrollPane(performancesTextArea), "Performante Actor", JOptionPane.INFORMATION_MESSAGE);

                    String movieToModify = JOptionPane.showInputDialog(frame, "Selectati performanta de modificat:");

                    for (int i = 0; i < actor.getPerformances().size(); i++) {
                        Map<String, String> performance = actor.getPerformances().get(i);
                        if (performance.get("title").equals(movieToModify)) {
                            String newTitle = JOptionPane.showInputDialog(frame, "Introduceti noul titlu pentru performanta:");
                            actor.getPerformances().set(i, Map.of("title", newTitle, "type", "Movie"));
                            JOptionPane.showMessageDialog(frame, "Performanta modificata cu succes.");
                            break;
                        }
                    }
                    break;
                case 3:
                    actor.setBiography(userInput);
                    break;
                default:
                    JOptionPane.showMessageDialog(frame, "Alegere incorecta!");
            }
        }
    }

    @Override
    public void resolveUserRequests() {
        if (this instanceof Contributor) {
            int requestNumber = 1;
            for (Object requestObj : ((Staff) this).getRequestList()) {
                System.out.println("Request Number: " + requestNumber);
                ((Request)requestObj).displayInfo();
                requestNumber++;
            }
            System.out.println("Pe care request doriti sa-l rezolvati?");
            int alegere = scanner.nextInt();

            requestNumber = 1;
            for (Object requestObj : ((Staff) this).getRequestList()) {
                if (requestNumber == alegere) {
                    if (((Request)requestObj).getType().equals(Request.RequestType.ACTOR_ISSUE)) {
                        System.out.println("Ce modificati?");
                        System.out.println("1. Numele");
                        System.out.println("2. Performanetele");
                        System.out.println("3. biografia");
                        int alegere2 = scanner.nextInt();
                        if (alegere2 == 1) {
                            for (Actor actor : imdb.actors) {
                                if (actor.getName().equals( ( (Request)requestObj).getActorName())) {
                                    System.out.println("Introduceti noul nume");
                                    scanner.nextLine();
                                    String newName = scanner.nextLine();
                                    actor.setName(newName);
                                    ((Request)requestObj).setResolved(true);

                                    String userUpdateExp = ((Request)requestObj).getUsername();
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

                        else if (alegere2 == 2) {
                            for (Actor actor : imdb.actors) {
                                if (actor.getName().equals(((Request) requestObj).getActorName())) {
                                    System.out.println("Performantele curenta:");
                                    for (Map<String, String> performance : actor.getPerformances()) {
                                        System.out.println("   - " + performance.get("title") + ": " + performance.get("type"));
                                    }

                                    System.out.println("Selectati performanta de modificat:");
                                    scanner.nextLine();
                                    String movieToModify = scanner.nextLine();

                                    for (int i = 0; i < actor.getPerformances().size(); i++) {
                                        Map<String, String> performance = actor.getPerformances().get(i);
                                        if (performance.get("title").equals(movieToModify)) {
                                            System.out.println("Introduceti noul titlu pentru performanta:");
                                            String newTitle = scanner.nextLine();

                                            actor.getPerformances().set(i, Map.of("title", newTitle, "type", "Movie"));

                                            System.out.println("Performanta modificata cu succes.");
                                            ((Request)requestObj).setResolved(true);
                                            break;
                                        }
                                    }

                                    String userUpdateExp = ((Request)requestObj).getUsername();
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
                        else if (alegere2 == 3) {
                            for (Actor actor : imdb.actors) {
                                if (actor.getName().equals(((Request) requestObj).getActorName())) {
                                    System.out.println("Introduceti noua biografie");
                                    scanner.nextLine();
                                    String newBiography = scanner.nextLine();
                                    actor.setName(newBiography);
                                    ((Request) requestObj).setResolved(true);

                                    String userUpdateExp = ((Request) requestObj).getUsername();
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
                        } else {
                            System.out.println("Alegere gresita");
                        }

                    } else if (((Request)requestObj).getType().equals(Request.RequestType.MOVIE_ISSUE)) {
                        System.out.println("Ce modificati?");
                        System.out.println("1. Titlul");
                        System.out.println("2. Regizorii");
                        System.out.println("3. Actorii");
                        System.out.println("4. Genurile");
                        System.out.println("5. Descrierea");
                        System.out.println("6. Durata");
                        System.out.println("7. Anul lansarii");
                        for (Production production : imdb.productions) {
                            if (production.getTitle().equals(((Request) requestObj).getMovieTitle())) {
                                scanner.nextLine();
                                int alegere2 = scanner.nextInt();
                                if (alegere2 == 1) {
                                    System.out.println("Introduceti noul nume");
                                    scanner.nextLine();
                                    String newTitle = scanner.nextLine();
                                    production.setTitle(newTitle);
                                    ((Request)requestObj).setResolved(true);

                                    String userUpdateExp = ((Request)requestObj).getUsername();
                                    for (User user : imdb.users) {
                                        if (user.getUsername().equals(userUpdateExp)) {
                                            CalculateExperienceContex calculateExperienceContex = new CalculateExperienceContex();
                                            calculateExperienceContex.setExperienceStrategy(new RequestStrategy(user.getExperienta()));
                                            user.setExperienta(calculateExperienceContex.calculateExp());
                                            break;
                                        }
                                    }

                                    break;
                                } else if (alegere2 == 2) {
                                    System.out.println("Introduceti noul regizor (separate by commas)");
                                    scanner.nextLine();
                                    String newDirectorsInput = scanner.nextLine();
                                    List<String> newDirectors = Arrays.asList(newDirectorsInput.split(","));
                                    production.setDirectors(newDirectors);
                                    ((Request)requestObj).setResolved(true);

                                    String userUpdateExp = ((Request)requestObj).getUsername();
                                    for (User user : imdb.users) {
                                        if (user.getUsername().equals(userUpdateExp)) {
                                            CalculateExperienceContex calculateExperienceContex = new CalculateExperienceContex();
                                            calculateExperienceContex.setExperienceStrategy(new RequestStrategy(user.getExperienta()));
                                            user.setExperienta(calculateExperienceContex.calculateExp());
                                            break;
                                        }
                                    }

                                    break;
                                } else if (alegere2 == 3) {
                                    System.out.println("Introduceti noul actor (separate by commas)");
                                    scanner.nextLine();
                                    String newActorsInput = scanner.nextLine();
                                    List<String> newActors = Arrays.asList(newActorsInput.split(","));
                                    production.setActors(newActors);
                                    ((Request)requestObj).setResolved(true);

                                    String userUpdateExp = ((Request)requestObj).getUsername();
                                    for (User user : imdb.users) {
                                        if (user.getUsername().equals(userUpdateExp)) {
                                            CalculateExperienceContex calculateExperienceContex = new CalculateExperienceContex();
                                            calculateExperienceContex.setExperienceStrategy(new RequestStrategy(user.getExperienta()));
                                            user.setExperienta(calculateExperienceContex.calculateExp());
                                            break;
                                        }
                                    }

                                    break;
                                } else if (alegere2 == 4) {
                                    System.out.println("Introduceti noul gen (separate by commas)");
                                    scanner.nextLine();
                                    String newGenresInput = scanner.nextLine();
                                    List<Genre> newGenres = Arrays.stream(newGenresInput.split(","))
                                            .map(Genre::valueOf)
                                            .collect(Collectors.toList());
                                    production.setGenres(newGenres);
                                    ((Request)requestObj).setResolved(true);

                                    String userUpdateExp = ((Request)requestObj).getUsername();
                                    for (User user : imdb.users) {
                                        if (user.getUsername().equals(userUpdateExp)) {
                                            CalculateExperienceContex calculateExperienceContex = new CalculateExperienceContex();
                                            calculateExperienceContex.setExperienceStrategy(new RequestStrategy(user.getExperienta()));
                                            user.setExperienta(calculateExperienceContex.calculateExp());
                                            break;
                                        }
                                    }

                                    break;
                                } else if (alegere2 == 5) {
                                    System.out.println("Introduceti noua descriere");
                                    scanner.nextLine();
                                    String newPlot = scanner.nextLine();
                                    production.setPlot(newPlot);
                                    ((Request)requestObj).setResolved(true);

                                    String userUpdateExp = ((Request)requestObj).getUsername();
                                    for (User user : imdb.users) {
                                        if (user.getUsername().equals(userUpdateExp)) {
                                            CalculateExperienceContex calculateExperienceContex = new CalculateExperienceContex();
                                            calculateExperienceContex.setExperienceStrategy(new RequestStrategy(user.getExperienta()));
                                            user.setExperienta(calculateExperienceContex.calculateExp());
                                            break;
                                        }
                                    }

                                    break;
                                } else if (alegere2 == 6) {
                                    System.out.println("Introduceti noua durata");
                                    scanner.nextLine();
                                    int newDuration = scanner.nextInt();
                                    ((Movie)production).setDuration(newDuration);
                                    ((Request)requestObj).setResolved(true);

                                    String userUpdateExp = ((Request)requestObj).getUsername();
                                    for (User user : imdb.users) {
                                        if (user.getUsername().equals(userUpdateExp)) {
                                            CalculateExperienceContex calculateExperienceContex = new CalculateExperienceContex();
                                            calculateExperienceContex.setExperienceStrategy(new RequestStrategy(user.getExperienta()));
                                            user.setExperienta(calculateExperienceContex.calculateExp());
                                            break;
                                        }
                                    }

                                    break;
                                } else if (alegere2 == 7) {
                                    System.out.println("Introduceti noul an de lansare");
                                    scanner.nextLine();
                                    int newReleaseYear = scanner.nextInt();
                                    ((Movie)production).setReleaseYear(newReleaseYear);
                                    ((Request)requestObj).setResolved(true);

                                    String userUpdateExp = ((Request)requestObj).getUsername();
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
                    }
                    break;
                }
                requestNumber++;
            }

        } else if (this instanceof Admin) {
            int requestNumber = 1;
            for (Request request : RequestsHolder.requestList) {
                System.out.println("Request Number: " + requestNumber);
                request.displayInfo();
                requestNumber++;;
            }
            System.out.println("Pe care request doriti sa-l rezolvati?");
            int alegere = scanner.nextInt();

            requestNumber = 1;
            for (Request requestObj : RequestsHolder.requestList) {
                if (requestNumber == alegere) {
                    if (((Request)requestObj).getType().equals(Request.RequestType.DELETE_ACCOUNT)) {
                        for (User user1 : imdb.users) {
                            if (requestObj.getUsername().equals(user1.getUsername())) {
                                imdb.users.remove(user1);
                                requestObj.setResolved(true);
                                break;
                            }
                        }
                    } else if (((Request)requestObj).getType().equals(Request.RequestType.OTHERS)) {
                        requestObj.setResolved(true);
                        System.out.println("Ce modificati?");
                        System.out.println("1. Username-ul");
                        System.out.println("2. Experienta");
                        System.out.println("3. Email-ul");
                        System.out.println("4. Parola");
                        System.out.println("5. Numele");
                        System.out.println("6. Tara");
                        System.out.println("7. Varsta");
                        System.out.println("8. Genul");
                        System.out.println("9. Data nasterii");


                        int alegere2 = scanner.nextInt();

                        for (User user1 : imdb.users) {
                            if (requestObj.getUsername().equals(user1.getUsername())) {
                                if (alegere2 == 1) {
                                    System.out.println("Introduceti noul username");
                                    scanner.nextLine();
                                    String newUsername = scanner.nextLine();
                                    user1.setUsername(newUsername);
                                    break;
                                }
                                else if (alegere2 == 2) {
                                    System.out.println("Introduceti noua experienta");
                                    scanner.nextLine();
                                    int newExperience = scanner.nextInt();
                                    user1.setExperienta(newExperience);
                                    break;
                                } else if (alegere2 == 3) {
                                    System.out.println("Introduceti noul email");
                                    scanner.nextLine();
                                    String newEmail = scanner.nextLine();
                                    user1.information.getCredentials().setUsername(newEmail);
                                    break;
                                } else if (alegere2 == 4) {
                                    System.out.println("Introduceti noua parola");
                                    scanner.nextLine();
                                    String newPassword = scanner.nextLine();
                                    user1.information.getCredentials().setPassword(newPassword);
                                    break;
                                } else if (alegere2 == 5) {
                                    System.out.println("Introduceti noul nume");
                                    scanner.nextLine();
                                    String newName = scanner.nextLine();
                                    user1.information.setName(newName);
                                    break;
                                } else if (alegere2 == 6) {
                                    System.out.println("Introduceti noua tara");
                                    scanner.nextLine();
                                    String newCountry = scanner.nextLine();
                                    user1.information.setCountry(newCountry);
                                    break;
                                } else if (alegere2 == 7) {
                                    System.out.println("Introduceti noua varsta");
                                    scanner.nextLine();
                                    int newAge = scanner.nextInt();
                                    user1.information.setAge(newAge);
                                    break;
                                } else if (alegere2 == 8) {
                                    System.out.println("Introduceti noul gen(M/F)");
                                    scanner.nextLine();
                                    char newGender = scanner.next().charAt(0);
                                    user1.information.setGender(newGender);
                                    break;
                                } else if (alegere2 == 9) {
                                    System.out.println("Introduceti noua data de nastere");
                                    scanner.nextLine();
                                    String birthDateInput = scanner.nextLine();
                                    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                    LocalDate birthDate = LocalDate.parse(birthDateInput, (formatter));
                                    user1.information.setBirthDate(birthDate);
                                    break;
                                } else {
                                    System.out.println("Alegere incorecta!");
                                }
                            }
                        }

                    }
                    break;
                }
                requestNumber++;
            }
        }

    }

    public void resolveUserRequestGUI() {
        PaginaResolveRequest paginaResolveRequest = new PaginaResolveRequest(Staff.this);
        paginaResolveRequest.setVisible(true);
    }

}
