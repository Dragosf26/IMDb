package org.example;

import org.example.Enum.AccountType;
import org.example.Interfaces.Observer;
import org.example.Interfaces.ObserverProduction;
import org.example.Interfaces.RequestsManager;
import org.example.Interfaces.SubjectProduction;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.SortedSet;

public class Contributor extends Staff implements RequestsManager, Observer, ObserverProduction {
    IMDB imdb = IMDB.getInstance();

    public Contributor(JSONObject jsonObject) {
        super(jsonObject);
    }

    public Contributor(Information information, AccountType accountType, String username, int experienta, List notificari, SortedSet productieActoriPreferati, List requestList, SortedSet productieActoriAdaugati) {
        super(information, accountType, username, experienta, notificari, productieActoriPreferati, requestList, productieActoriAdaugati);
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
                    r.addObserver((Observer) user2);
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

    @Override
    public void updateRating(String notification) {

    }

    @Override
    public void updateRequest() {
        String notificare = "Cererea dumneavoastra a fost rezolvata!";
        this.getNotificari().add(notificare);
    }

    @Override
    public void updateProductionActor(SubjectProduction rated) {
        String notificare = null;
        if (rated instanceof Production) {
            notificare = "Productia pe care ati adaugat-o a primit o recenzie!";
        } else if (rated instanceof Actor) {
            notificare = "Actorul pe care l-ati adaugati a primit o recenzie";
        }
        this.getNotificari().add(notificare);
    }

    public void updateRequestContributor() {
        String notificare = "Ati primit o cerere pe care trebuie sa o rezolvati!";
        this.getNotificari().add(notificare);

    }
}
