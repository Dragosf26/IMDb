package org.example;

import org.example.Enum.AccountType;
import org.example.Enum.Genre;
import org.example.Exceptions.InformationIncompleteException;
import org.example.Exceptions.InvalidCommandException;
import org.example.StrategyPattern.CalculateExperienceContex;
import org.example.StrategyPattern.ProductionStrategy;
import org.example.StrategyPattern.RequestStrategy;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.lang.reflect.Constructor;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class IMDB {
    private static Scanner scanner = new Scanner(System.in);
    private static IMDB instance = null;

    public List<User> users;
    public List<Actor> actors;
    public List<Production> productions;
    public List<Request> requests;

    //SINGLETON

    private IMDB() {
        users = new ArrayList<>();
        actors = new ArrayList<>();
        productions = new ArrayList<>();
        requests = new ArrayList<>();
    }

    public static IMDB getInstance() {
        if(instance == null) {
            instance = new IMDB();
        }
        return instance;
    }

    //END SINGLETON

    //GETTERS

    public List<User> getUsers() { return users; }

    public List<Actor> getActors() {
        return actors;
    }

    public List<Production> getProductions() {
        return productions;
    }

    public List<Request> getRequests() {
        return requests;
    }

    //END GETTERS

    public void run() throws InvalidCommandException {
        // incarcarea datelor din fiaierele JSON
        loadDataFromJSON();

        System.out.println("Doriti sa folositi aplicatia prin terminal sau prin interfata grafica?");
        System.out.println("1. Terminal");
        System.out.println("2. Interfata grafica");
        try {
            int alegere = scanner.nextInt();

            if (alegere == 1) {
                logare();
            } else if (alegere == 2) {
                PaginaAutentificare paginaAutentificare = new PaginaAutentificare();
            } else {
                throw new InvalidCommandException("Optiune invalida!");
            }
        } catch (InputMismatchException e) {
            throw new InvalidCommandException("Optiune invalida!");
        }

        scanner.close();

    }

    private void logare() throws InvalidCommandException {
        User userAutentificat;

        do {
            userAutentificat = autentificare();
            if (userAutentificat == null) {
                System.out.println("Credentiale gresite! Introdu din nou credentialele");
            }
        } while (userAutentificat == null);

        System.out.println("Autentificat cu succes");
        if(userAutentificat instanceof Regular) {
            System.out.println("Regular user:\nWelcome back, " + userAutentificat.getUsername());
            System.out.println("Experienta: " + userAutentificat.getExperienta());
            optiuniRegular(userAutentificat);
        } else if (userAutentificat instanceof Contributor) {
            System.out.println("Contributor user:\nWelcome back, " + userAutentificat.getUsername());
            System.out.println("Experienta: " + userAutentificat.getExperienta());
            optiuniContributor(userAutentificat);
        } else {
            System.out.println("Admin user:\nWelcome back, " + userAutentificat.getUsername());
            optiuniAdmin(userAutentificat);
        }
    }

    private void loadDataFromJSON() {
        try {
            // incarcarea datelor despre utilizatori
            loadUsersFromJSON("accounts.json", users);

            // incarcarea datelor despre actori
            loadActorsFromJSON("actors.json", actors);

            // incarcarea datelor despre productii
            loadProductionsFromJSON("production.json", productions);

            // incarcarea datelor despre cereri
            loadRequestsFromJSON("requests.json", requests);
            for(Request request : requests) {
                if (request.getType().equals(Request.RequestType.DELETE_ACCOUNT) || request.getType().equals(Request.RequestType.OTHERS)) {
                    RequestsHolder.requestList.add(request);
                } else {
                    for(User user : users) {
                        if(user.getUsername().equals(request.getTo())) {
                            ((Staff)user).getRequestList().add(request);
                        }
                    }
                }
            }

            // Asocierea actorilor cu productiile
            associateActorsWithMovies(productions, actors);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.out.println("Eroare la incarcarea datelor din fiaierele JSON.");
        }
    }

    private void loadUsersFromJSON(String filename, List<User> users) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();


        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("input/" + filename)) {
            if (inputStream == null) {
                System.out.println("Fisierul nu a fost gasit: input/" + filename);
                return;
            }

            try (InputStreamReader reader = new InputStreamReader(inputStream)) {
                JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

                for (Object obj : jsonArray) {
                    JSONObject jsonObject = (JSONObject) obj;

                    if (jsonObject.containsKey("userType")) {
                        String userTypeString = (String) jsonObject.get("userType");
                        AccountType userType = AccountType.valueOf(userTypeString);

                        Factory factory = new Factory();
                        User user = factory.createUser(userType, jsonObject);

                        users.add(user);
                    }
                }
            }
        }
    }

    //FACTORY PATTERN
    public class Factory {
        public User createUser(AccountType userType, JSONObject jsonObject) {
            switch (userType) {
                case Regular -> {
                    return new Regular(jsonObject);
                }
                case Contributor -> {
                    return new Contributor(jsonObject);
                }
                case Admin ->{
                    return new Admin(jsonObject);
                }
                default -> {
                    return null;
                }
            }
        }
    }

    //END FACTORY PATTERN

    private void loadActorsFromJSON(String filename, List<Actor> actors) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("input/" + filename)) {
            if (inputStream == null) {
                System.out.println("Fisierul nu a fost gasit: input/" + filename);
                return;
            }

            try (InputStreamReader reader = new InputStreamReader(inputStream)) {
                JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

                for (Object obj : jsonArray) {
                    JSONObject jsonObject = (JSONObject) obj;

                    Actor actor = new Actor(jsonObject);
                    actors.add(actor);
                }
            }
        }
    }

    private void loadProductionsFromJSON(String filename, List<Production> productions) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("input/" + filename)) {
            if (inputStream == null) {
                System.out.println("Fisierul nu a fost gasit: input/" + filename);
                return;
            }

            try (InputStreamReader reader = new InputStreamReader(inputStream)) {
                JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

                for (Object obj : jsonArray) {
                    JSONObject jsonObject = (JSONObject) obj;

                    String type = (String) jsonObject.get("type");
                    if ("Movie".equals(type)) {
                        Movie movie = new Movie(jsonObject);
                        productions.add(movie);
                    } else if ("Series".equals(type)) {
                        Series series = new Series(jsonObject);
                        productions.add(series);
                    }
                }
            }
        }
    }

    private void loadRequestsFromJSON(String filename, List<Request> requests) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("input/" + filename)) {
            if (inputStream == null) {
                System.out.println("Fisierul nu a fost gasit: input/" + filename);
                return;
            }

            try (InputStreamReader reader = new InputStreamReader(inputStream)) {
                JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

                for (Object obj : jsonArray) {
                    JSONObject jsonObject = (JSONObject) obj;
                    Request request = new Request(jsonObject);
                    requests.add(request);
                }
            }
        }
    }

    private void associateActorsWithMovies(List<Production> productions, List<Actor> actors) {
        for (Production production : productions) {
            for (String actorName : production.getActors()) {
                boolean actorFound = false;

                for (Actor actor : actors) {
                    if (actorName.equals(actor.getName())) {
                        actorFound = true;
                        break;
                    }
                }

                if (!actorFound) {
                    List<Map<String, String>> performances = new ArrayList<>();
                    performances.add(Map.of("title", production.getTitle(), "type", "Movie"));
                    Actor newActor = new Actor(actorName, performances, null);
                    actors.add(newActor);
                }
            }
        }
    }


    private User autentificare() {
        System.out.println("Introduceti numele de utilizator:");
        String username = scanner.nextLine().trim();
        if (username.isEmpty()) {
            username = scanner.nextLine().trim();
        }
        System.out.println("Introduceti parola:");
        String password = scanner.nextLine().trim();

        return verificaCredentiale(username, password);
    }

    private User verificaCredentiale(String username, String password) {
        for (User user : users) {
            if(user.getInformation().getCredentials().getUsername().equals(username) && user.getInformation().getCredentials().getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }


    private void optiuniRegular(User user) throws InvalidCommandException {
        System.out.println("Alegeti optiunea");
        System.out.println("1. Vizualizarea detaliilor tuturor productiilor din sistem ");
        System.out.println("2. Vizualizarea detaliilor tuturor actorilor din sistem ");
        System.out.println("3. Vizualizarea notificarilor primite ");
        System.out.println("4. Cautarea unui anumit film/serial/actor");
        System.out.println("5. Adaugarea/Stergerea unei productii/actor din lista de favorite");
        System.out.println("6. Crearea/Retragerea unei cereri ");
        System.out.println("7. Adaugarea/Stergerea unei recenzii pentru o productie ");
        System.out.println("8. Delogare");

        alegeOptiuneRegular(user);
    }

    private void alegeOptiuneRegular(User user) throws InvalidCommandException {
        int optiune;

        try {
            do {
                System.out.print("Introduceti numarul optiunii: ");

                optiune = scanner.nextInt();

                switch (optiune) {
                    case 1:
                        System.out.println("Ati ales Vizualizarea detaliilor tuturor productiilor din sistem");
                        optiune1();
                        break;
                    case 2:
                        System.out.println("Ati ales Vizualizarea detaliilor tuturor actorilor din sistem");
                        optiune2();
                        break;
                    case 3:
                        System.out.println("Ati ales Vizualizarea notificarilor primite");
                        optiune3(user);
                        break;
                    case 4:
                        System.out.println("Ati ales Cautarea unui anumit film/serial/actor");
                        optiune4();
                        break;
                    case 5:
                        System.out.println("Ati ales Adaugarea/Stergerea unei productii/actor din lista de favorite");
                        optiune5(user);
                        break;
                    case 6:
                        System.out.println("Ati ales Crearea/Retragerea unei cereri");
                        optiune6RegularContributor(user);
                        break;
                    case 7:
                        System.out.println("Ati ales Adaugarea/Stergerea unei recenzii pentru o productie");
                        optiune7Regular(user);
                        break;
                    case 8:
                        System.out.println("Ati ales Delogare");
                        break;
                    default:
                        System.out.println("Optiune invalida");
                }
            } while (optiune != 8);
        } catch (InputMismatchException e) {
            throw new InvalidCommandException("Optiune invalida");
        }

        logare();
    }

    private void optiune1() {
        for(Production production : productions) {
            production.displayInfo();
        }
    }

    private void optiune2() {
        for (Actor actor : actors) {
            actor.displayInfo();
        }
    }

    private void optiune3(User user) {

        user.displayInfo();
    }

    private void optiune4() {
        System.out.println("Scrie-ti un film/serial/actor");
        String search = scanner.nextLine();
        if(search.isEmpty()) {
            search = scanner.nextLine();
        }

        boolean found = false;

        for (Production production : productions) {
            if (search.equalsIgnoreCase(production.title)) {
                production.displayInfo();
                found = true;
                break;
            }
        }

        if (!found) {
            for (Actor actor : actors) {
                if (search.equalsIgnoreCase(actor.getName())) {
                    actor.displayInfo();
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            System.out.println("Nu s-a gasit nicio informatie pentru: " + search);
        }
    }

    private void optiune5(User user) throws InvalidCommandException {
        System.out.println("Productie/Actori favoriti:");
        for (Object productieActori : user.getProductieActoriPreferati()) {
            System.out.println((String) productieActori);
        }

        System.out.println("1. Adaugati");
        System.out.println("2. Stergeti");
        try {
            int alegere = scanner.nextInt();
            if (alegere == 1) {
                System.out.println("Scrie-ti un film/serial/actor pe care doriti sa-l stergeti");
                scanner.nextLine();
                String search = scanner.nextLine();

                boolean found = false;

                for (Production productieActori : productions) {
                    if (productieActori.getTitle().equals(search)) {
                        user.getProductieActoriPreferati().add(productieActori.getTitle());
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    for (Actor actor : actors) {
                        if (actor.getName().equals(search)) {
                            user.getProductieActoriPreferati().add(actor.getName());
                            found = true;
                            break;
                        }
                    }
                }

                if (!found) {
                    System.out.println("Productia/Actorul nu a fost gasit");
                }
            } else if (alegere == 2) {
                System.out.println("Scrie-ti un film/serial/actor pe care doriti sa-l stergeti");
                scanner.nextLine();
                String search = scanner.nextLine();

                boolean found = false;

                for (Production productie : productions) {
                    if (productie.getTitle().equals(search)) {
                        user.getProductieActoriPreferati().remove(productie.getTitle());
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    for (Actor actor : actors) {
                        if (actor.getName().equals(search)) {
                            user.getProductieActoriPreferati().remove(actor.getName());
                            found = true;
                            break;
                        }
                    }
                }

                if (!found) {
                    System.out.println("Productia/Actorul nu a fost gasit");
                }
            } else {
                System.out.println("Optiune invalida!");
            }
        } catch (InputMismatchException e) {
            throw new InvalidCommandException("Optiune invalida!");
        }

    }

    private void optiune6RegularContributor(User user) throws InvalidCommandException {
        System.out.println("Alegeti:");
        System.out.println("1. Pentru a crea o cerere");
        System.out.println("2. Pentru a sterge o cerere");
        int result = scanner.nextInt();
        try {
            if (result == 1) {
                System.out.println("Alegeti tipul de cerere:");
                System.out.println("1. DELETE_ACCOUNT");
                System.out.println("2. ACTOR_ISSUE");
                System.out.println("3. MOVIE_ISSUE");
                System.out.println("4. OTHER");
                int type = scanner.nextInt();

                LocalDateTime createdDate = LocalDateTime.now();
                DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                String formattedDate = createdDate.format(DATE_TIME_FORMATTER);
                LocalDateTime parsedDate = LocalDateTime.parse(formattedDate, DATE_TIME_FORMATTER);

                String username = user.getUsername();


                String actorName = null;
                String movieTitle = null;
                if (type == 2) {
                    System.out.println("Actor name:");
                    actorName = scanner.nextLine();
                    if (actorName.isEmpty()) {
                        actorName = scanner.nextLine();
                    }
                } else if (type == 3) {
                    System.out.println("Movie title:");
                    movieTitle = scanner.nextLine();
                    if (movieTitle.isEmpty()) {
                        movieTitle = scanner.nextLine();
                    }
                }

                String to = null;
                boolean found = false;
                if (type == 1 || type == 4) {
                    to = "ADMIN";
                } else {
                    for (User user1 : users) {
                        if (user1 instanceof Contributor) {
                            for (Object productieActori : ((Contributor) user1).productieActoriAdaugati) {
                                if (type == 2) {
                                    if (((String) productieActori).equals(actorName)) {
                                        to = user1.getUsername();
                                        found = true;
                                        break;
                                    }
                                } else if (type == 3) {
                                    if (((String) productieActori).equals(movieTitle)) {
                                        to = user1.getUsername();
                                        found = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (found) {
                            break;
                        }
                    }
                }

                if ((type == 2 || type == 3) && !found) {
                    System.out.println("Nu a fost gasit");
                    return;
                }

                System.out.println("Description:");
                String description = scanner.nextLine();
                if (description.isEmpty()) {
                    description = scanner.nextLine();
                }

                Request.RequestType requestType = null;
                if (type == 1) {
                    requestType = Request.RequestType.DELETE_ACCOUNT;
                } else if (type == 2) {
                    requestType = Request.RequestType.ACTOR_ISSUE;
                } else if (type == 3) {
                    requestType = Request.RequestType.MOVIE_ISSUE;
                } else if (type == 4) {
                    requestType = Request.RequestType.OTHERS;
                }
                Request request = new Request(requestType, parsedDate, username, to, description, actorName, movieTitle);
                if (user instanceof Regular) {
                    ((Regular) user).createRequest(request);
                } else if (user instanceof Contributor) {
                    ((Contributor) user).createRequest(request);
                }

            } else if (result == 2) {
                int indexRequest = 0;
                Iterator<Request> iterator = requests.iterator();

                while (iterator.hasNext()) {
                    Request request = iterator.next();
                    if (request.getUsername().equals(user.getUsername())) {
                        indexRequest++;
                        System.out.println("----------" + (indexRequest) + "----------");
                        request.displayInfo();
                    }
                }

                System.out.println("Scrieti numarul cererii pe care doriti sa o stergeti");
                int indexCerere = scanner.nextInt();
                int counter = 0;

                iterator = requests.iterator();

                while (iterator.hasNext()) {
                    Request request = iterator.next();
                    if (request.getUsername().equals(user.getUsername())) {
                        counter++;
                        if (indexCerere == counter) {
                            ((Iterator<?>) iterator).remove();
                            if (user instanceof Regular) {
                                ((Regular) user).removeRequest(request);
                            } else if (user instanceof Contributor) {
                                ((Contributor) user).removeRequest(request);
                            }
                        }
                    }
                }

            } else {
                System.out.println("Ati introdus un numar gresit!");
            }
        } catch (InputMismatchException e) {
            throw new InvalidCommandException("Optiune invalida!");
        }
    }

    private void optiune7Regular(User user) throws InvalidCommandException {
        System.out.println("Alege o producte/un actor pentru a-i adauga un rating");
        String productie = scanner.nextLine();
        if (productie.isEmpty()) {
            productie = scanner.nextLine();
        }
        Production ratedProduction = null;
        boolean foundProduction = false;
        for(Production production : productions) {
            if (productie.equals(production.title)) {
                ratedProduction = production;
                foundProduction = true;
                break;
            }
        }

        Actor actorFound = null;
        if (!foundProduction) {
            boolean foundActor = false;
            for (Actor actor : actors) {
                if (productie.equals(actor.getName())) {
                    actorFound = actor;
                    foundActor = true;
                    break;
                }
            }

            if (!foundActor) {
                System.out.println("Productia/Actorul nu au fost gasite");
                return;
            }

            for (Rating rating : actorFound.ratings) {
                rating.displayInfo();
            }

        } else {
            for (Rating rating : ratedProduction.ratings) {
                rating.displayInfo();
            }
        }

            String username = user.getUsername();

            int rating;
            System.out.println("Alege un rating(1-10)");
            try {
                do {
                    rating = scanner.nextInt();
                    if (rating < 1 || rating > 10) {
                        System.out.println("Alege din nou rating(1-10)");
                    }
                } while (rating < 1 || rating > 10);
            } catch (InputMismatchException e) {
                throw new InvalidCommandException("Optiune invalida!");
            }

            System.out.println("Scrie comentariu:");
            String comment = scanner.nextLine();
            if (comment.isEmpty()) {
                comment = scanner.nextLine();
            }

            Rating rating1 = new Rating(username, rating, comment);
            if (foundProduction) {
                ((Regular) user).addRatingProduction(rating1, ratedProduction);
                return;
            }
            ((Regular) user).addRatingActor(rating1, actorFound);
    }

    private void optiuniContributor(User user) throws InvalidCommandException {
        System.out.println("Alegeti optiunea");
        System.out.println("1. Vizualizarea detaliilor tuturor productiilor din sistem");
        System.out.println("2. Vizualizarea detaliilor tuturor actorilor din sistem");
        System.out.println("3. Vizualizarea notificarilor primite");
        System.out.println("4. Cautarea unui anumit film/serial/actor");
        System.out.println("5. Adaugarea/Stergerea unei productii/actor din lista de favorite");
        System.out.println("6. Crearea/Retragerea unei cereri");
        System.out.println("7. Adaugarea/Stergerea unei productii/actor din sistem");
        System.out.println("8. Vizualizarea si rezolvarea cererilor primite");
        System.out.println("9. Actualizarea informatiilor despre productii/actori");
        System.out.println("10. Delogare");

        alegeOptiuneContributor(user);
    }

    private void alegeOptiuneContributor(User user) throws InvalidCommandException {
        int optiune;

        try {
            do {
                System.out.print("Introduceti numarul optiunii: ");

                optiune = scanner.nextInt();

                switch (optiune) {
                    case 1:
                        System.out.println("Ati ales Vizualizarea detaliilor tuturor productiilor din sistem");
                        optiune1();
                        break;
                    case 2:
                        System.out.println("Ati ales Vizualizarea detaliilor tuturor actorilor din sistem");
                        optiune2();
                        break;
                    case 3:
                        System.out.println("Ati ales Vizualizarea notificarilor primite");
                        optiune3(user);
                        break;
                    case 4:
                        System.out.println("Ati ales Cautarea unui anumit film/serial/actor");
                        optiune4();
                        break;
                    case 5:
                        System.out.println("Ati ales Adaugarea/Stergerea unei productii/actor din lista de favorite");
                        optiune5(user);
                        break;
                    case 6:
                        System.out.println("Ati ales Crearea/Retragerea unei cereri");
                        optiune6RegularContributor(user);
                        break;
                    case 7:
                        System.out.println("Ati ales Adaugarea/Stergerea unei productii/actor din sistem");
                        optiune7ContributorAdmin(user);
                        break;
                    case 8:
                        System.out.println("Ati ales Vizualizarea si rezolvarea cererilor primite");
                        optiune8Contributor7Admin(user);
                        break;
                    case 9:
                        System.out.println("Ati ales Actualizarea informatiilor despre productii/actori");
                        optiune9Contributor8Admin(user);
                        break;
                    case 10:
                        System.out.println("Ati ales Delogare");
                        break;
                    default:
                        System.out.println("Optiune invalida");
                }
            } while (optiune != 10);
        }  catch (InputMismatchException e) {
            throw new InvalidCommandException("Optiune invalida!");
        }

        logare();
    }

    private void optiune7ContributorAdmin(User user) throws InvalidCommandException {
        System.out.println("Doriti sa:");
        System.out.println("1. Adaugati un actor/productie");
        System.out.println("2. Stergeti un actor/productie");
        int alegere = scanner.nextInt();

        try {
            if (alegere == 1) {
                String title;
                List<String> directors = new ArrayList<>();
                List<String> actorsArray = new ArrayList<>();
                List<Genre> genres = new ArrayList<>();
                List<Rating> ratings = new ArrayList<>();
                String plot;
                double averageRating = 0;

                System.out.println("Film sau serial?");
                System.out.println("1. Film");
                System.out.println("2. Serial");
                int alegere2 = scanner.nextInt();

                if (alegere2 == 1) {
                    scanner.nextLine();
                    System.out.println("Introduceti titlul:");
                    title = scanner.nextLine();

                    System.out.println("Introduceti regizorii (separati cu virgula):");
                    String directorsInput = scanner.nextLine();
                    directors.addAll(List.of(directorsInput.split(",")));

                    System.out.println("Introduceti actorii (separati cu virgula):");
                    String actorsInput = scanner.nextLine();
                    actorsArray.addAll(List.of(actorsInput.split(",")));

                    System.out.println("Introduceti durata (in minute):");
                    int duration = scanner.nextInt();

                    System.out.println("Introduceti anul lansarii:");
                    int releaseYear = scanner.nextInt();

                    scanner.nextLine();
                    System.out.println("Introduceti genurile (separati cu virgula):");
                    String genresInput = scanner.nextLine();
                    String[] genreNames = genresInput.split(",");

                    for (String genreName : genreNames) {
                        try {
                            Genre genre = Genre.valueOf(genreName.trim());
                            genres.add(genre);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Genul " + genreName.trim() + " nu este valid.");
                        }
                    }

                    System.out.println("Introduceti povestea:");
                    plot = scanner.nextLine();

                    Movie movie = new Movie(duration, releaseYear, title, directors, actorsArray, genres, ratings, plot, averageRating);
                    ((Staff) user).addProductionSystem(movie);
                    movie.addObserverProduction((Staff) user);
                    ((Staff) user).getProductieActoriAdaugati().add(movie.getTitle());

                    CalculateExperienceContex calculateExperienceContex = new CalculateExperienceContex();
                    calculateExperienceContex.setExperienceStrategy(new ProductionStrategy(user.getExperienta()));
                    user.setExperienta(calculateExperienceContex.calculateExp());

                } else if (alegere2 == 2) {
                    scanner.nextLine();
                    System.out.println("Introduceti titlul:");
                    title = scanner.nextLine();

                    System.out.println("Introduceti regizorii (separati cu virgula):");
                    String directorsInput = scanner.nextLine();
                    directors.addAll(List.of(directorsInput.split(",")));

                    System.out.println("Introduceti actorii (separati cu virgula):");
                    String actorsInput = scanner.nextLine();
                    actorsArray.addAll(List.of(actorsInput.split(",")));

                    System.out.println("Introduceti anul lansarii:");
                    int releaseYear = scanner.nextInt();

                    System.out.println("Introduceti numarul de sezoane:");
                    int numSeasons = scanner.nextInt();

                    Map<String, List<Episode>> seasons = new HashMap<>();

                    for (int i = 1; i <= numSeasons; i++) {
                        scanner.nextLine();
                        System.out.println("Introduceti numarul de episoade pentru sezonul " + i + ":");
                        int numEpisodes = scanner.nextInt();

                        List<Episode> episodeList = new ArrayList<>();

                        for (int j = 1; j <= numEpisodes; j++) {
                            scanner.nextLine();
                            System.out.println("Introduceti titlul episodului " + j + " din sezonul " + i + ":");
                            String episodeTitle = scanner.nextLine();

                            System.out.println("Introduceti durata episodului " + j + " din sezonul " + i + " (in minute):");
                            int episodeDuration = scanner.nextInt();

                            Episode episode = new Episode(episodeTitle, episodeDuration);
                            episodeList.add(episode);
                        }

                        seasons.put("Sezonul " + i, episodeList);
                    }

                    System.out.println("Introduceti genurile (separati cu virgula):");
                    scanner.nextLine();
                    String genresInput = scanner.nextLine();
                    String[] genreNames = genresInput.split(",");

                    for (String genreName : genreNames) {
                        try {
                            Genre genre = Genre.valueOf(genreName.trim());
                            genres.add(genre);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Genul " + genreName.trim() + " nu este valid.");
                        }
                    }

                    System.out.println("Introduceti povestea:");
                    plot = scanner.nextLine();

                    Series series = new Series(title, directors, actorsArray, genres, ratings, plot, averageRating, releaseYear, numSeasons, seasons);
                    ((Staff) user).addProductionSystem(series);
                    series.addObserverProduction((Staff) user);
                    ((Staff) user).getProductieActoriAdaugati().add(series.getTitle());

                    CalculateExperienceContex calculateExperienceContex = new CalculateExperienceContex();
                    calculateExperienceContex.setExperienceStrategy(new ProductionStrategy(user.getExperienta()));
                    user.setExperienta(calculateExperienceContex.calculateExp());
                } else {
                    System.out.println("Alegere incorecta");
                }
            } else if (alegere == 2) {
                for (Production production : productions) {
                    production.displayInfo();
                }

                System.out.println("Introduceti numele filmului/serialului");
                scanner.nextLine();
                String numeProductie = scanner.nextLine();
                for (Production production : productions) {
                    if (production.getTitle().equals(numeProductie)) {
                        productions.remove(production);
                        break;
                    }
                }
            } else {
                System.out.println("Alegere incorecta");
            }
        } catch (InputMismatchException e) {
            throw new InvalidCommandException("Optiune invalida!");
        }
    }

    private void optiune8Contributor7Admin(User user) {
        ((Staff)user).resolveUserRequests();
    }


private void optiune9Contributor8Admin(User user) throws InvalidCommandException {
    System.out.println("Ce actualizati?");
    System.out.println("1. Productie");
    System.out.println("2. Actor");

    try {
        int alegere = scanner.nextInt();
        if (alegere == 1) {
            System.out.println("Introduceti numele productiei");
            scanner.nextLine();
            String numeProductie = scanner.nextLine();

            for (Production production : productions) {
                if (production.getTitle().equals(numeProductie)) {
                    ((Staff) user).updateProduction(production);
                }
            }
        } else if (alegere == 2) {
            System.out.println("Introduceti numele actorului");
            scanner.nextLine();
            String numeActor = scanner.nextLine();
            for (Actor actor : actors) {
                if (actor.getName().equals(numeActor)) {
                    ((Staff) user).updateActor(actor);
                }
            }
        } else {
            System.out.println("Alegere incorecta!");
        }
    } catch (InputMismatchException e) {
        throw new InvalidCommandException("Optiune invalida!");
    }
}
    private void optiuniAdmin(User user) throws InvalidCommandException {
        System.out.println("Alegeti optiunea");
        System.out.println("1. Vizualizarea detaliilor tuturor productiilor din sistem");
        System.out.println("2. Vizualizarea detaliilor tuturor actorilor din sistem");
        System.out.println("3. Vizualizarea notificarilor primite");
        System.out.println("4. Cautarea unui anumit film/serial/actor");
        System.out.println("5. Adaugarea/Stergerea unei productii/actor din lista de favorite");
        System.out.println("6. Adaugarea/Stergerea unei productii/actor din sistem");
        System.out.println("7. Vizualizarea si rezolvarea cererilor primite");
        System.out.println("8. Actualizarea informatiilor despre productii/actori");
        System.out.println("9. Adaugarea/Stergerea unui utilizator din sistem");
        System.out.println("10. Delogare");

        alegeOptiuneAdmin(user);
    }

    private void alegeOptiuneAdmin(User user) throws InvalidCommandException {
        int optiune;

        try {
            do {
                System.out.print("Introduceti numarul optiunii: ");

                optiune = scanner.nextInt();

                switch (optiune) {
                    case 1:
                        System.out.println("Ati ales Vizualizarea detaliilor tuturor productiilor din sistem");
                        optiune1();
                        break;
                    case 2:
                        System.out.println("Ati ales Vizualizarea detaliilor tuturor actorilor din sistem");
                        optiune2();
                        break;
                    case 3:
                        System.out.println("Ati ales Vizualizarea notificarilor primite");
                        optiune3(user);
                        break;
                    case 4:
                        System.out.println("Ati ales Cautarea unui anumit film/serial/actor");
                        optiune4();
                        break;
                    case 5:
                        System.out.println("Ati ales Adaugarea/Stergerea unei productii/actor din lista de favorite");
                        optiune5(user);
                        break;
                    case 6:
                        System.out.println("Ati ales Adaugarea/Stergerea unei productii/actor din sistem");
                        optiune7ContributorAdmin(user);
                        break;
                    case 7:
                        System.out.println("Ati ales Vizualizarea si rezolvarea cererilor primite");
                        optiune8Contributor7Admin(user);
                        break;
                    case 8:
                        System.out.println("Ati ales Actualizarea informatiilor despre productii/actori");
                        optiune9Contributor8Admin(user);
                        break;
                    case 9:
                        System.out.println("Ati ales Adaugarea/Stergerea unui utilizator din sistem");
                        optiune9Admin(user);
                        break;
                    case 10:
                        System.out.println("Ati ales Delogare");
                        break;
                    default:
                        System.out.println("Optiune invalida");
                }
            } while (optiune != 10);
        } catch (InputMismatchException e) {
            throw new InvalidCommandException("Optiune invalida!");
        }

        logare();
    }

    private void optiune9Admin(User user) throws InvalidCommandException {
        System.out.println("Adaugati sau stergeti un utilizator?");
        System.out.println("1. Adaugati");
        System.out.println("2. Stergeti");
        scanner.nextLine();
        int alegere = scanner.nextInt();

        try {
            if (alegere == 1) {

                System.out.println("Introduceti tipul de utilizator");
                scanner.nextLine();
                String accountTypeString = scanner.nextLine();
                AccountType accountType = AccountType.valueOf(accountTypeString);

                System.out.println("Introduceti username");
                String username = scanner.nextLine();

                int experienta = 0;

                List<String> notificari = new ArrayList<>();

                SortedSet<Object> productieActoriPreferati = new TreeSet<>();

                System.out.println("Introduceti email");
                String email = scanner.nextLine();

                if (email == "") {
                    throw new InformationIncompleteException("Email invalid!");
                }

                System.out.println("Introduceti parola");
                String password = scanner.nextLine();

                if (password == "") {
                    throw new InformationIncompleteException("Parola invalida!");
                }

                Credentials credentials = new Credentials(email, password);

                System.out.println("Introduceti numele");
                String name = scanner.nextLine();

                if (name == "") {
                    throw new InformationIncompleteException("Nume invalid!");
                }

                System.out.println("Introduceti tara");
                String country = scanner.nextLine();

                System.out.println("Introduceti varsta");
                int age = scanner.nextInt();

                System.out.println("Introduceti noul genul(M/F)");
                char gender = scanner.next().charAt(0);

                System.out.println("Introduceti data de nastere(\"yyyy-MM-dd\")");
                scanner.nextLine();
                String birthDateInput = scanner.nextLine();
                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate birthDate = LocalDate.parse(birthDateInput, (formatter));


                User.Information information = new User.Information.Builder()
                        .setCredentials(credentials)
                        .setName(name)
                        .setCountry(country)
                        .setAge(age)
                        .setGender(gender)
                        .setBirthDate(birthDate)
                        .build();


                if (accountType.equals(AccountType.Regular)) {
                    Regular regular = new Regular(information, accountType, username, experienta, notificari, productieActoriPreferati);
                    users.add(regular);
                } else if (accountType.equals(AccountType.Contributor)) {
                    List<Request> requestList = new ArrayList<>();
                    SortedSet<Object> productieActoriAdaugati = new TreeSet<>();

                    Contributor contributor = new Contributor(information, accountType, username, experienta, notificari, productieActoriPreferati, requestList, productieActoriAdaugati);
                    users.add(contributor);
                } else {
                    List<Request> requestList = new ArrayList<>();
                    SortedSet<Object> productieActoriAdaugati = new TreeSet<>();

                    Admin admin = new Admin(information, accountType, username, experienta, notificari, productieActoriPreferati, requestList, productieActoriAdaugati);
                    users.add(admin);
                }
            } else if (alegere == 2) {
                scanner.nextLine();
                String deleteUser = scanner.nextLine();
                for (User user1 : users) {
                    if (user1.getUsername().equals(deleteUser)) {
                        users.remove(user1);
                        break;
                    }
                }
            } else {
                System.out.println("Alegere incorecta!");
            }
        } catch (InputMismatchException e) {
            throw new InvalidCommandException("Optiune invalida!");
        } catch (InformationIncompleteException e) {
            System.out.println(e.getMessage());
        }
    }

}
