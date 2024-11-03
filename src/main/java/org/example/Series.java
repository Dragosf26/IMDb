package org.example;

import org.example.Enum.Genre;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Series extends Production{
    int releaseYear;
    int numSeasons;

    //String = seazonName; List<Episode> = episodes;
    private Map<String, List<Episode>> seasons;

    public Series(JSONObject jsonObject) {
        super(jsonObject);

        Map<String, List<Episode>> seasons = new LinkedHashMap<>();
        JSONObject seasonsJSON = (JSONObject) jsonObject.get("seasons");


        if(jsonObject.containsKey("releaseYear")) {
            Long releaseYearLong = ((Long) jsonObject.get("releaseYear"));
            this.releaseYear = releaseYearLong.intValue();
        }
        if(jsonObject.containsKey("numSeasons")) {
            Long numSeasonsLong = ((Long) jsonObject.get("numSeasons"));
            this.numSeasons = numSeasonsLong.intValue();
        }

        for (int i = 1; i <= numSeasons; i++) {
            List<Episode> episodes = new ArrayList<>();

            String seasonKey = "Season " + i;
            JSONArray seasonArray = (JSONArray) seasonsJSON.get(seasonKey);

            for (Object episodeObj : seasonArray) {
                JSONObject episodeJSON = (JSONObject) episodeObj;
                Episode episode = new Episode(episodeJSON);
                episodes.add(episode);
            }
            seasons.put(seasonKey, episodes);
        }

        this.seasons = seasons;
    }

    public Series(String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings, String plot, double averageRating, int releaseYear, int numSeasons, Map<String, List<Episode>> seasons) {
        super(title, directors, actors, genres, ratings, plot, averageRating);
        this.releaseYear = releaseYear;
        this.numSeasons = numSeasons;
        this.seasons = seasons;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setNumSeasons(int numSeasons) {
        this.numSeasons = numSeasons;
    }

    public void setSeasons(Map<String, List<Episode>> seasons) {
        this.seasons = seasons;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public int getNumSeasons() {
        return numSeasons;
    }

    public Map<String, List<Episode>> getSeasons() {
        return seasons;
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
        System.out.println("Release year: " + releaseYear);
        System.out.println("Number of seasons: " + numSeasons);
        System.out.println("Seasons: ");
        for (Map.Entry<String, List<Episode>> entry : seasons.entrySet()) {
            System.out.println("\tSezon: " + entry.getKey());
            for (Episode episode : entry.getValue()) {
                episode.displayInfo();
            }
        }
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
            info.append("   ");
            info.append(rating.displayInfoGUI());
        }
        info.append("Plot: " + this.plot + "\n");
        info.append("Average rating: " + this.averageRating + "\n");

        if (this instanceof Series) {
            Series series = (Series) this;
            info.append("Release year: " + series.releaseYear + "\n");
            info.append("Number of seasons: " + series.numSeasons + "\n");
            info.append("Seasons: \n");
            for (Map.Entry<String, List<Episode>> entry : series.seasons.entrySet()) {
                info.append("\tSeason: " + entry.getKey() + "\n");
                for (Episode episode : entry.getValue()) {
                    info.append(episode.displayInfoGUI());
                }
            }
        }

        info.append("-----------------------------------------\n");

        return info.toString();
    }

}