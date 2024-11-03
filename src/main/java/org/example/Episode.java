package org.example;

import org.json.simple.JSONObject;

import javax.swing.*;

public class Episode {
    String episodeName;
    int duration;

    public Episode(JSONObject jsonObject) {
        this.episodeName = (String) jsonObject.get("episodeName");
        String durationString = (String) jsonObject.get("duration");
        String durationNumericString = (String) durationString.replaceAll("[^\\d]", "");
        this.duration = Integer.parseInt(durationNumericString);
    }

    public Episode(String episodeName, int duration) {
        this.episodeName = episodeName;
        this.duration = duration;
    }

    public String getEpisodeName() {
        return episodeName;
    }

    public int getDuration() {
        return duration;
    }

    void displayInfo() {
        System.out.println("\tEpisode name: " + episodeName);
        System.out.println("\tDuration: " + duration + "\n");
    }

    public String displayInfoGUI() {
        StringBuilder info = new StringBuilder();
        if (this instanceof Episode) {
            Episode episode = (Episode) this;
            info.append("\tEpisode name: " + episode.episodeName + "\n");
            info.append("\tDuration: " + episode.duration + "\n");
        }
        return info.toString();
    }
}