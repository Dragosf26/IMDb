package org.example.Interfaces;

import org.example.Actor;
import org.example.Production;

public interface StaffInterface {
    void addProductionSystem(Production p);

    void addActorSystem(Actor a);

    void removeProductionSystem(String name);

    void removeActorSystem(String name);

    void updateProduction(Production p);

    void updateActor(Actor a);

    void resolveUserRequests();
}
