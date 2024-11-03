package org.example;

import org.example.Enum.AccountType;
import org.example.Interfaces.SubjectProduction;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.SortedSet;

class Admin extends Staff{

    public Admin(JSONObject jsonObject) {
        super(jsonObject);
    }

    public Admin(Information information, AccountType accountType, String username, int experienta, List notificari, SortedSet productieActoriPreferati, List requestList, SortedSet productieActoriAdaugati) {
        super(information, accountType, username, experienta, notificari, productieActoriPreferati, requestList, productieActoriAdaugati);
    }

    @Override
    public void updateProductionActor(SubjectProduction rated) {
        String notificare = null;
        if (rated instanceof Production) {
            notificare = "Productia pe care ati adaugat-o a primit o recenzie!";
        } else if (rated instanceof Actor) {
            notificare = "Actorul pe care l-ati adaugat a primit o recenzie!";
        }
        this.getNotificari().add(notificare);
    }
}