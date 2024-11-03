package org.example.Interfaces;

public interface SubjectProduction {

    void addObserverProduction(ObserverProduction observer);

    void removeObserverProduction(ObserverProduction observer);

    void notifyObserversProduction();
}
