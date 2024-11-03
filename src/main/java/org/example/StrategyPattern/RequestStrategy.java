package org.example.StrategyPattern;

import org.example.Interfaces.ExperienceStrategy;

public class RequestStrategy implements ExperienceStrategy {

    int experience;

    public RequestStrategy(int experience) {
        this.experience = experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    @Override
    public int calculateExperience() {
        experience += 2;
        return experience;
    }
}
