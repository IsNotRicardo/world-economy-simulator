package model.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Person {
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