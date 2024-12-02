package model.core;

import java.util.*;

public class Person {
    // Constants
    private final static double PREFERENCE_ADJUSTMENT_PROBABILITY = 0.2;
    private final static double PREFERENCE_ADJUSTMENT_RANGE = 0.1;

    // Variables immediately initialized
    private Map<Resource, Double> preferences = new HashMap<>();

    // Variables initialized in the constructor
    private double happiness;
    private double budget;

    public Person(double initialHappiness, double initialBudget, Set<Resource> availableResources) {
        this.happiness = initialHappiness;
        this.budget = initialBudget;

        // Initialize the preferences map with random values
        Random random = new Random();
        for (Resource resource : availableResources) {
            preferences.put(resource, random.nextDouble());
        }
    }

    // Start of Getters
    public double getHappiness() {
        return happiness;
    }

    public double getBudget() {
        return budget;
    }

    public Map<Resource, Double> getPreferences() {
        return preferences;
    }
    // End of Getters

    private void adjustPreferences() {
        Random random = new Random();
        int totalPreferences = preferences.size();
        int preferencesToAdjust = (int) Math.ceil(PREFERENCE_ADJUSTMENT_PROBABILITY * totalPreferences);

        Set<Resource> resourceSet = preferences.keySet();
        List<Resource> resourceList = new ArrayList<>(resourceSet);
        Collections.shuffle(resourceList, random);

        for (int i = 0; i < preferencesToAdjust; i++) {
            Resource resource = resourceList.get(i);
            double currentPreference = preferences.get(resource);
            double adjustment = (random.nextDouble() - 0.5) * PREFERENCE_ADJUSTMENT_RANGE;
            double newPreference = Math.max(0.0, Math.min(1.0, currentPreference + adjustment));
            preferences.put(resource, newPreference);
        }
    }

    /*
    public void consumeResource(Resource resource) {
        // Simulate resource consumption logic
        // For example, reduce the quantity of the resource based on demand
    }

    public void generateDemand() {
        // Create demand for resources
        // For example, populate the demand map with resource types and quantities
    }
    */
}