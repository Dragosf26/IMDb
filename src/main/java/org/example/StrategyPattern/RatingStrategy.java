package org.example.StrategyPattern;

import org.example.Interfaces.ExperienceStrategy;

public class RatingStrategy implements ExperienceStrategy {
    int experience;

    public RatingStrategy(int experience) {
        this.experience = experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    @Override
    public int calculateExperience() {
        experience += 1;
        return experience;
    }
}
