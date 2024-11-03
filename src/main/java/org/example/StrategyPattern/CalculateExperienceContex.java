package org.example.StrategyPattern;

import org.example.Interfaces.ExperienceStrategy;

public class CalculateExperienceContex {
    private ExperienceStrategy experienceStrategy;


    public void setExperienceStrategy(ExperienceStrategy experienceStrategy) {
        this.experienceStrategy = experienceStrategy;
    }

    public int calculateExp() {
        return experienceStrategy.calculateExperience();
    }
}
