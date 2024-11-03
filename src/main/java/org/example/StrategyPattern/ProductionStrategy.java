package org.example.StrategyPattern;

import org.example.Interfaces.ExperienceStrategy;

public class ProductionStrategy implements ExperienceStrategy {
    int experience;

    public ProductionStrategy(int experience) {
        this.experience = experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    @Override
    public int calculateExperience() {
        experience += 3;
        return experience;
    }
}
