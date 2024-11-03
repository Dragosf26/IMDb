package org.example;

import org.example.Enum.AccountType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class User {
    Information information;
    AccountType accountType;
    String username;
    int experienta;
    List<String> notificari;
    SortedSet<Object>  productieActoriPreferati;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public User(JSONObject jsonObject) {
        JSONObject informationJson = (JSONObject) jsonObject.get("information");
        JSONObject credentialsJSON = (JSONObject) informationJson.get("credentials");
        Credentials credentials = new Credentials((String) credentialsJSON.get("email"), (String) credentialsJSON.get("password"));

        String birthDateStr = (String) informationJson.get("birthDate");

        LocalDate birthDate = LocalDate.parse(((String) informationJson.get("birthDate")), (formatter));

        Information information = new Information.Builder()
                .setCredentials(credentials)
                .setName((String) informationJson.get("name"))
                .setCountry((String) informationJson.get("country"))
                .setAge( ( (Long) informationJson.get("age")).intValue())
                .setGender(((String) informationJson.get("gender")).charAt(0))
                .setBirthDate(birthDate)
                .build();

        String userTypeString = (String) jsonObject.get("userType");
        AccountType userType = AccountType.valueOf(userTypeString);

        String username = (String) jsonObject.get("username");

        String experienceStr = (String) jsonObject.get("experience");
        int experience = 0;
        if(experienceStr != null)
            experience = Integer.parseInt(experienceStr);


        List<String> notifications = new ArrayList<>();
        if(jsonObject.containsKey("notifications")) {
            JSONArray JSONnotifications = (JSONArray) jsonObject.get("notifications");
            for(Object item : JSONnotifications) {
                notifications.add((String) item);
            }
        }

        SortedSet<Object> favs = new TreeSet<>();
        if(jsonObject.containsKey("favoriteProductions")) {
            JSONArray JSONfavoriteProducetions = (JSONArray) jsonObject.get("favoriteProductions");
            for(Object item : JSONfavoriteProducetions) {
                favs.add((Object) item);
            }
        }

        if(jsonObject.containsKey("favoriteActors")) {
            JSONArray JSONfavoriteActors = (JSONArray) jsonObject.get("favoriteActors");
            for(Object item : JSONfavoriteActors) {
                favs.add((Object) item);
            }
        }

        this.information = information;
        this.accountType = userType;
        this.username = username;
        this.experienta = experience;
        this.notificari = notifications;
        this.productieActoriPreferati = favs;
    }

    public User(Information information, AccountType accountType, String username, int experienta, List<String> notificari, SortedSet<Object> productieActoriPreferati) {
        this.information = information;
        this.accountType = accountType;
        this.username = username;
        this.experienta = experienta;
        this.notificari = notificari;
        this.productieActoriPreferati = productieActoriPreferati;
    }

    public void displayInfo() {
        System.out.println("Notifications:");
        for (String notification : notificari) {
            System.out.println("    " + notification);
        }
    }

    public void displayInfoGUI(JFrame frame) {
        StringBuilder info = new StringBuilder();
        for (String notification : notificari) {
            info.append(notification + "\n");
        }
        JOptionPane.showMessageDialog(frame, "Notificari:\n" + info);
    }

    //GETTERS AND SETTERS
    public Information getInformation() {
        return information;
    }

    public void setInformation(Information information) {
        this.information = information;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getExperienta() {
        return experienta;
    }

    public void setExperienta(int experienta) {
        this.experienta = experienta;
    }

    public List<String> getNotificari() {
        return notificari;
    }

    public void setNotificari(List<String> notificari) {
        this.notificari = notificari;
    }

    public SortedSet<Object> getProductieActoriPreferati() {
        return productieActoriPreferati;
    }

    public void setProductieActoriPreferati(SortedSet<Object> productieActoriPreferati) {
        this.productieActoriPreferati = productieActoriPreferati;
    }

    public void adaugaProductiePreferata(Object productie) {
        this.productieActoriPreferati.add(productie);
    }
    public void stergereProductiePreferata(Object productie) {
        this.productieActoriPreferati.remove(productie);
    }

    //END GETTER AND SETTER
    public void actualizareExperienta() {
        this.experienta += 1;
    }
    public static class Information {
        private Credentials credentials;
        private String name;
        private String country;
        private int age;
        private char gender;
        private LocalDate birthDate;

        private Information(Builder builder) {
            this.credentials = builder.credentials;
            this.name = builder.name;
            this.country = builder.country;
            this.age = builder.age;
            this.gender = builder.gender;
            this.birthDate = builder.birthDate;
        }

        public Credentials getCredentials() {
            return credentials;
        }

        public String getName() {
            return name;
        }

        public String getCountry() {
            return country;
        }

        public int getAge() {
            return age;
        }

        public char getGender() {
            return gender;
        }

        public LocalDate getBirthDate() {
            return birthDate;
        }

        public void setCredentials(Credentials credentials) {
            this.credentials = credentials;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public void setGender(char gender) {
            this.gender = gender;
        }

        public void setBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
        }

        //BUILDER PATTERN
        public static class Builder {
            private Credentials credentials;
            private String name;
            private String country;
            private int age;
            private char gender;
            private LocalDate birthDate;

            public Builder(){}

            public Builder setCredentials(Credentials credentials) {
                this.credentials = credentials;
                return this;
            }

            public Builder setName(String name) {
                this.name = name;
                return this;
            }

            public Builder setCountry(String country) {
                this.country = country;
                return this;
            }

            public Builder setAge(int age) {
                this.age = age;
                return this;
            }

            public Builder setGender(char gender) {
                this.gender = gender;
                return this;
            }

            public Builder setBirthDate(LocalDate birthDate) {
                this.birthDate = birthDate;
                return this;
            }

            public Information build() {
                return new Information(this);
            }
        }

        //END BUILDER PATTERN
    }
}