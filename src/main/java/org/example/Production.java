package org.example;

import org.example.Enum.Genre;
import org.example.Interfaces.ObserverProduction;
import org.example.Interfaces.SubjectProduction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Production implements Comparable, SubjectProduction {

    private List<ObserverProduction> observers = new ArrayList<>();
    String title;
    List<String> directors;
    List<String> actors;
    List<Genre> genres;
    List<Rating> ratings;
    String plot;
    double averageRating;

    public Production(JSONObject jsonObject) {
        List<String> directors = new ArrayList<>();
        List<String> actors = new ArrayList<>();
        List<Genre> genres = new ArrayList<>();
        List<Rating> ratings = new ArrayList<>();

        JSONArray directorsJSONArray = (JSONArray) jsonObject.get("directors");
        for(Object object : directorsJSONArray) {
            directors.add((String) object);
        }

        JSONArray actorsJSONArray = (JSONArray) jsonObject.get("actors");
        for(Object object : actorsJSONArray) {
            actors.add((String) object);
        }

        JSONArray genreJSONArray = (JSONArray) jsonObject.get("genres");
        for(Object object : genreJSONArray) {
            String genreString = (String) object;
            Genre genre = Genre.valueOf(genreString);
            genres.add(genre);
        }

        JSONArray ratingsJSONArray = (JSONArray) jsonObject.get("ratings");
        for(Object object : ratingsJSONArray) {
            Rating rating = new Rating((JSONObject) object);
            ratings.add((Rating) rating);
        }

        this.title = (String) jsonObject.get("title");
        this.directors = directors;
        this.actors = actors;
        this.genres = genres;
        this.ratings = ratings;
        this.plot = (String) jsonObject.get("plot");
        this.averageRating = (double) jsonObject.get("averageRating");
    }

    public Production(String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings, String plot, double averageRating) {
        this.title = title;
        this.directors = directors;
        this.actors = actors;
        this.genres = genres;
        this.ratings = ratings;
        this.plot = plot;
        this.averageRating = averageRating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public List<String> getActors() {
        return actors;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public String getPlot() {
        return plot;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public List<ObserverProduction> getObservers() {
        return observers;
    }

    public abstract void displayInfo();

    public abstract String displayInfoGUI();

    public int compareTo(Object o) {
        Production p = (Production) o;
        return this.title.compareTo(p.title);
    }

    @Override
    public void addObserverProduction(ObserverProduction observer) {
        observers.add(observer);
    }
    @Override
    public void removeObserverProduction(ObserverProduction observer) {
        observers.remove(observer);
    }
    @Override
    public void notifyObserversProduction() {
        for (ObserverProduction observer : observers) {
            observer.updateProductionActor(this);
        }
    }
}