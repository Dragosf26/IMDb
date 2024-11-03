package org.example;

import org.example.Interfaces.ObserverProduction;
import org.example.Interfaces.SubjectProduction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Actor implements SubjectProduction {
    private List<ObserverProduction> observers = new ArrayList<>();
    String name;
    List<Map<String, String>> performances;
    String biography;

    List<Rating> ratings;

    public Actor(JSONObject jsonObject) {
        List<Map<String, String>> performances = new ArrayList<>();
        JSONArray performancesJSON = (JSONArray) jsonObject.get("performances");
        for(Object object : performancesJSON) {
            String title = (String) ((JSONObject) object).get("title");
            String type = (String) ((JSONObject) object).get("type");

            performances.add(Map.of("title", title, "type", type));
        }

        this.name = (String) jsonObject.get("name");
        this.performances = performances;
        this.biography = (String) jsonObject.get("biography");
        this.ratings = new ArrayList<>();
    }

    public Actor(String name, List<Map<String, String>> performances, String biography) {
        this.name = name;
        this.performances = performances;
        this.biography = biography;
        this.ratings = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Map<String, String>> getPerformances() {
        return performances;
    }

    public String getBiography() {
        return biography;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public List<ObserverProduction> getObservers() {
        return observers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPerformances(List<Map<String, String>> performances) {
        this.performances = performances;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public void displayInfo() {
        System.out.println("Name: " + name);

        System.out.println("Performances:");
        for (Map<String, String> performance : performances) {
            for (Map.Entry<String, String> entry : performance.entrySet()) {
                System.out.println("\t" + entry.getKey() + ": " + entry.getValue());
            }
            System.out.println();
        }
        System.out.println("Biography: " + biography);
        System.out.println("Ratings: ");
        for (Rating rating : ratings) {
            rating.displayInfo();
        }
        System.out.println("---------------------------------------------------------------------------");
    }

    public String displayInfoGUI() {
        StringBuilder infoBuilder = new StringBuilder();
        infoBuilder.append("Name: ").append(name).append("\n\n");
        infoBuilder.append("Performances:\n");
        if (performances != null) {
            for (Map<String, String> performance : performances) {
                for (Map.Entry<String, String> entry : performance.entrySet()) {
                    infoBuilder.append("\t").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }
            }
        }
        infoBuilder.append("\nBiography: ").append(biography).append("\n");
        infoBuilder.append("Ratings: \n");
        for (Rating rating : ratings) {
            infoBuilder.append(rating.displayInfoGUI());
        }
        infoBuilder.append("---------------------------------------------------------------------------\n");

        return infoBuilder.toString();
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