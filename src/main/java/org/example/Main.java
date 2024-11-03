package org.example;

import org.example.Exceptions.InvalidCommandException;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws InvalidCommandException {
        IMDB imdb = IMDB.getInstance();
        imdb.run();
    }
}
