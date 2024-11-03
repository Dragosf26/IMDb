package org.example;

import org.example.Interfaces.Observer;
import org.example.Interfaces.Subject;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Rating implements Subject {

    private List<Observer> observers = new ArrayList<>();
    String username;
    int rating;
    String comment;

    public Rating(JSONObject jsonObject) {
        this.username = (String) jsonObject.get("username");
        this.rating = ((Long) jsonObject.get("rating")).intValue();
        this.comment = (String) jsonObject.get("comment");
    }

    public Rating(String username, int rating, String comment) {
        this.username = username;
        this.rating = rating;
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }


    public List<Observer> getObservers() {
        return observers;
    }

    public void displayInfo() {
        System.out.println("\tUsername: " + this.username);
        System.out.println("\tRating: " + this.rating);
        System.out.println("\tComment: " + this.comment + "\n");
    }

    public String displayInfoGUI() {
        StringBuilder info = new StringBuilder();
        info.append("\tUsername: ").append(this.username).append("\n");
        info.append("\tRating: ").append(this.rating).append("\n");
        info.append("\tComment: ").append(this.comment).append("\n");
        return info.toString();
    }

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
        for (Observer observer : observers) {
            observer.updateRating(notification);
        }
    }

    @Override
    public void notifyObserversRequest() {
    }
}