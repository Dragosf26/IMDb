package org.example;

import org.example.Enum.AccountType;
import org.example.Interfaces.Observer;
import org.example.Interfaces.Subject;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Request implements Subject {
    IMDB imdb = IMDB.getInstance();
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {

        observers.remove(observer);
    }

    @Override
    public void notifyObserversRating(String notification) {
    }

    @Override
    public void notifyObserversRequest() {
        for (Observer observer : observers) {
            if (((User)observer).getUsername().equals(to)) {
                ((Contributor)observer).updateRequestContributor();
            }

            observer.updateRequest();
        }
    }


    public enum RequestType {
        DELETE_ACCOUNT,
        ACTOR_ISSUE,
        MOVIE_ISSUE,
        OTHERS
    }
    private RequestType type;
    private LocalDateTime createdDate;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private String username;
    private String to;

    private String description;
    private String actorName;
    private String movieTitle;

    private boolean resolved;

    private List<Observer> observers = new ArrayList<>();

    public Request(JSONObject jsonObject) {
        resolved = false;
        String RequestTypeString = (String) jsonObject.get("type");
        this.type = RequestType.valueOf(RequestTypeString);
        this.createdDate = LocalDateTime.parse(((String) jsonObject.get("createdDate")), (DATE_TIME_FORMATTER));
        this.username = (String) jsonObject.get("username");
        this.to = (String) jsonObject.get("to");
        this.description = (String) jsonObject.get("description");
        if(jsonObject.containsKey("actorName")) {
            this.actorName = (String) jsonObject.get("actorName");
        } else {
            this.actorName = null;
        }
        if(jsonObject.containsKey("movieTitle")) {
            this.movieTitle = (String) jsonObject.get("movieTitle");
        } else  {
            this.movieTitle = null;
        }

    }

    public Request(RequestType type, LocalDateTime createdDate, String username, String to, String description, String actorName, String movieTitle) {
        this.resolved = false;
        this.type = type;
        this.createdDate = createdDate;
        this.username = username;
        this.to = to;
        this.description = description;
        this.actorName = actorName;
        this.movieTitle = movieTitle;

        for (Production production : imdb.productions) {
            if (production.getTitle().equals(actorName) || production.getTitle().equals(movieTitle)) {
                production.notifyObserversProduction();
                break;
            }
        }
    }

    //GETTERS

    public RequestType getType() {
        return type;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public String getUsername() {
        return username;
    }

    public String getTo() {
        return to;
    }

    public String getDescription() {
        return description;
    }

    public String getActorName() {
        return actorName;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
        notifyObserversRequest();
    }

    public void displayInfo() {
        System.out.println("Resolved: " + resolved);
        System.out.println("Request Type: " + type);
        System.out.println("Created Date: " + createdDate.format(DATE_TIME_FORMATTER));
        System.out.println("Username: " + username);
        System.out.println("To: " + to);
        System.out.println("Description: " + description);

        switch (type) {
            case ACTOR_ISSUE:
                System.out.println("Actor Name: " + actorName);
                break;
            case MOVIE_ISSUE:
                System.out.println("Movie Title: " + movieTitle);
                break;
            default:
                break;
        }
    }

    public String displayInfoGUI() {
        StringBuilder info = new StringBuilder();
        info.append("Resolved: ").append(resolved).append("\n");
        info.append("Request Type: ").append(type).append("\n");
        info.append("Created Date: ").append(createdDate.format(DATE_TIME_FORMATTER)).append("\n");
        info.append("Username: ").append(username).append("\n");
        info.append("To: ").append(to).append("\n");
        info.append("Description: ").append(description).append("\n");

        switch (type) {
            case ACTOR_ISSUE:
                info.append("Actor Name: ").append(actorName).append("\n");
                break;
            case MOVIE_ISSUE:
                info.append("Movie Title: ").append(movieTitle).append("\n");
                break;
            default:
                break;
        }

        return info.toString();
    }

}