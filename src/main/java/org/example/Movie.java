package org.example;

import org.example.Enum.Genre;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Movie extends Production{
    int duration;
    int releaseYear;

    public Movie(JSONObject jsonObject) {
        super(jsonObject);
        String durationString = (String) jsonObject.get("duration");
        String durationNumericString = (String) durationString.replaceAll("[^\\d]", "");
        this.duration = Integer.parseInt(durationNumericString);
        if(jsonObject.containsKey("releaseYear")) {
            Long releaseYearLong = (Long) jsonObject.get("releaseYear");

            this.releaseYear = releaseYearLong.intValue();
        }
    }

    public Movie(int duration, int releaseYear, String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings, String plot, double averageRating) {
        super(title, directors, actors, genres, ratings, plot, averageRating);
        this.duration = duration;
        this.releaseYear = releaseYear;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getDuration() {
        return duration;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    @Override
    public void displayInfo() {
        System.out.println("Title: " + this.title);
        System.out.println("Directors: " + this.directors);
        System.out.println("Actors: " + this.actors);
        System.out.println("Genres: " + this.genres);
        System.out.println("Ratings: ");
        for (Rating rating : ratings) {
            rating.displayInfo();
        }
        System.out.println("Plot: " + this.plot);
        System.out.println("Average rating: " + this.averageRating);
        System.out.println("Duration " + duration);
        System.out.println("Release Year: " + releaseYear);
        System.out.println("-----------------------------------------");
    }

    @Override
    public String displayInfoGUI() {

        StringBuilder info = new StringBuilder();

        info.append("Title: " + this.title + "\n");
        info.append("Directors: " + this.directors + "\n");
        info.append("Actors: " + this.actors + "\n");
        info.append("Genres: " + this.genres + "\n");
        info.append("Ratings: \n");
        for (Rating rating : ratings) {
            info.append(rating.displayInfoGUI() + "\n");
        }
        info.append("Plot: " + this.plot + "\n");
        info.append("Average rating: " + this.averageRating + "\n");
        info.append("Duration " + duration + "\n");
        info.append("Release Year: " + releaseYear + "\n");
        info.append("-----------------------------------------\n");

        return info.toString();
    }
}