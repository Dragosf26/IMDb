package org.example;

import org.example.Enum.AccountType;
import org.example.Interfaces.Observer;
import org.example.Interfaces.ObserverProduction;
import org.example.Interfaces.RequestsManager;
import org.example.StrategyPattern.CalculateExperienceContex;
import org.example.StrategyPattern.RatingStrategy;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class Regular extends User implements RequestsManager, Observer {
    IMDB imdb = IMDB.getInstance();

    public Regular(JSONObject jsonObject) {
        super(jsonObject);

    }

    public Regular(Information information, AccountType accountType, String username, int experienta, List<String> notificari, SortedSet<Object> productieActoriPreferati) {
        super(information, accountType, username, experienta, notificari, productieActoriPreferati);
    }

    public Credentials getCredentials() {
        return information.getCredentials();
    }



    @Override
    public void createRequest(Request r) {
        r.addObserver(this);
        if (r.getType() == Request.RequestType.DELETE_ACCOUNT || r.getType() == Request.RequestType.OTHERS) {
            RequestsHolder.requestList.add(r);
            imdb.requests.add(r);
            for (User user : imdb.users) {
                if (user instanceof Admin) {
                    user.getNotificari().add("Aveti o cerere noua de rezolvat!");
                }
            }
        } else {
            for(User user2 : imdb.users) {
                if(user2.getUsername().equals(r.getTo())) {
                    ((Staff)user2).getRequestList().add(r);
                    imdb.requests.add(r);
                }
            }
        }
    }

    @Override
    public void removeRequest(Request r) {
        r.removeObserver(this);
        if (r.getType().equals(Request.RequestType.DELETE_ACCOUNT) || r.getType().equals(Request.RequestType.OTHERS)) {
            RequestsHolder.requestList.remove(r);
        } else {
            for (User user1 : imdb.users) {
                if (user1.getUsername().equals(r.getTo())) {
                    ((Staff) user1).getRequestList().remove(r);
                }
            }
        }
    }

    public void addRatingProduction(Rating rating, Production ratedProduction) {
        String ratingNotification = null;
        if (ratedProduction instanceof Movie) {
            ratingNotification = "Filmul \"" + ratedProduction.getTitle() + "\" pe care l-ai evaluat a primit un rating de la utilizatorul " + rating.getUsername() + " -> " + rating.getRating();
        } else if (ratedProduction instanceof Series) {
            ratingNotification = "Serialul \"" + ratedProduction.getTitle() + "\" pe care l-ai evaluat a primit un rating de la utilizatorul \"" + rating.getUsername() + "\" -> " + rating.getRating();
        }

        for (Rating rating1 : ratedProduction.getRatings()) {
            rating1.notifyObserversRating(ratingNotification);
        }


        rating.addObserver(this);
        ratedProduction.getRatings().add(rating);

        for (ObserverProduction observerProduction : ratedProduction.getObservers()) {
            observerProduction.updateProductionActor(ratedProduction);
        }

        CalculateExperienceContex calculateExperienceContex = new CalculateExperienceContex();
        calculateExperienceContex.setExperienceStrategy(new RatingStrategy(experienta));
        experienta = calculateExperienceContex.calculateExp();
    }

    public void addRatingActor(Rating rating, Actor ratedActor) {
        String ratingNotification = null;


        for (Rating rating1 : ratedActor.getRatings()) {
            rating1.notifyObserversRating(ratingNotification);
        }


        rating.addObserver(this);
        ratedActor.getRatings().add(rating);

        for (ObserverProduction observerProduction : ratedActor.getObservers()) {
            observerProduction.updateProductionActor(ratedActor);
        }

        CalculateExperienceContex calculateExperienceContex = new CalculateExperienceContex();
        calculateExperienceContex.setExperienceStrategy(new RatingStrategy(experienta));
        experienta = calculateExperienceContex.calculateExp();
    }
    public void updateRating(String notification) {
        this.getNotificari().add(notification);
    }

    @Override
    public void updateRequest() {
        String notificare = "Cererea dumneavoastra a fost rezolvata!";
        this.getNotificari().add(notificare);
    }

}
